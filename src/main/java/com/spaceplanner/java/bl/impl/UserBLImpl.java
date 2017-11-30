package com.spaceplanner.java.bl.impl;

import com.spaceplanner.java.bl.UserBL;
import com.spaceplanner.java.dao.UserDao;
import com.spaceplanner.java.exception.DuplicateObjectException;
import com.spaceplanner.java.exception.NoRecordFoundException;
import com.spaceplanner.java.exception.SpacePlannerException;
import com.spaceplanner.java.exception.SpacePlannerResponseStatus;
import com.spaceplanner.java.model.UserEntity;
import com.spaceplanner.java.model.type.PasswordType;
import com.spaceplanner.java.security.PasswordEncoder;
import com.spaceplanner.java.task.NotifyUserTask;
import com.spaceplanner.java.task.SpacePlannerBeans;
import com.spaceplanner.java.util.CommonUtil;
import com.spaceplanner.java.util.Constants;
import com.spaceplanner.java.util.SpacePlannerUtil;
import com.spaceplanner.java.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 20/8/14
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("userBL")
@Transactional
public class UserBLImpl extends BaseBL implements UserBL, UserDetailsService, Constants {

    private Logger logger = LoggerFactory.getLogger(UserBLImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private ValidatorUtil validatorUtil;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SpacePlannerBeans spacePlannerBeans;

    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity users = userDao.getUserByUsername(username);
        if (null == users)
            throw new UsernameNotFoundException(commonUtil.getText("404.username.no.record.found", username));
        return users;
    }

    public List<UserEntity> getUser() {
        return userDao.getUser();
    }

    public UserEntity getUser(Long id) {
        UserEntity userEntity = userDao.getUser(id);
        if (null == userEntity)
            throw new NoRecordFoundException(commonUtil.getText("404.user.no.record.found", id + ""));
        return userEntity;
    }

    public UserEntity save(UserEntity userEntity, SpacePlannerResponseStatus status) {
        validatorUtil.validate(userEntity, status);
        if (status.hasError()) {
            saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
            return userEntity;
        }
        SpacePlannerUtil.processRole(userEntity);
        try {
            if(null != userEntity.getId()){
                UserEntity user = userDao.getUser(userEntity.getId());
                user.setFirstName(userEntity.getFirstName());
                user.setLastName(userEntity.getLastName());
                user.setMobile(userEntity.getMobile());
                user.setRoles(userEntity.getRoles());
                userDao.save(user);
            }else{
                userEntity.setUsername(userEntity.getEmail());
                userEntity.setPassword(passwordEncoder.encodePassword(userEntity.getPassword(), null));
                userDao.saveUser(userEntity);
            }
            saveSuccessMessage(status, commonUtil.getText("success.save", status.getLocale()));
        } catch (DuplicateObjectException e) {
            userEntity.setPassword(userEntity.getConfirmPassword());
            status.setCode(HttpStatus.CONFLICT.value());
            status.setMessage(commonUtil.getText("error.email.or.mobile.already.exist",
                    new Object[]{userEntity.getEmail(),userEntity.getMobile()}, status.getLocale()));
        }
        return userEntity;
    }


    public void delete(Long id) {
        UserEntity userEntity = userDao.getUser(id);
        userEntity.setEnabled(false);
        userDao.save(userEntity);
    }

    public void updateProfile(String name, String value, Long id, SpacePlannerResponseStatus status) {
        validatorUtil.validateUserUpdateAbleFiled(name, value, status);
        if (status.hasError()) {
            saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
            return;
        }
        try {
            userDao.updateProfile(name, value, id);
        } catch (SQLException e) {
            throw new SpacePlannerException(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    
    
    public void forgotPassword(String username, SpacePlannerResponseStatus status){
        UserEntity user = userDao.getUserByUsername(username);
        if (null == user) {
            status.setError(commonUtil.getText("404.username.no.record.found", username));
            saveErrorMessage(status, HttpStatus.NOT_FOUND.value());
            return;
        }
        String password = SpacePlannerUtil.getTempPassword();
        user.setPassword(passwordEncoder.encodePassword(password, null));
        user.setUpdatedAt(new Date());
        user.setPasswordType(PasswordType.TEMPORARY);
        userDao.save(user);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name", user.getName());
        map.put("password", password);
        new NotifyUserTask(spacePlannerBeans).notifyUserPassword(map, user.getUsername());
        saveSuccessMessage(status, commonUtil.getText("success.send.new.password", status.getLocale()));
    }

    public void changePassword(String oldPassword, String password, String confirmPassword, Long userId, SpacePlannerResponseStatus status) {
        validatorUtil.validatePassword(password, confirmPassword, status);
        if (status.hasError()) {
            saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
            return;
        }
        UserEntity userEntity = userDao.getUser(userId);
        if (userEntity.getPasswordType().equals(PasswordType.PERMANENT)) {
            if (ValidatorUtil.isNullOrEmpty(oldPassword)) {
                status.addError(commonUtil.getText("error.old.password.required", status.getLocale()));
                saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
                return;
            }
            if (!passwordEncoder.isPasswordValid(userEntity.getPassword(), oldPassword, null))
                throw new SpacePlannerException(HttpStatus.PRECONDITION_FAILED.value(), commonUtil.getText("error.existing.password.wrong"));
        }
        userEntity.setPassword(passwordEncoder.encodePassword(password, null));
        userEntity.setUpdatedAt(new Date());
        userEntity.setPasswordType(PasswordType.PERMANENT);
        userDao.save(userEntity);
        saveSuccessMessage(status, commonUtil.getText("success.update", status.getLocale()));
    }


    /*public void identify(String email, SpacePlannerResponseStatus status) {
        if (ValidatorUtil.isNullOrEmpty(email))
            status.addError(commonUtil.getText("error.email.required"));
        else if (!ValidatorUtil.isValidEmail(email))
            status.addError(commonUtil.getText("error.email.invalid"));
        if (status.hasError()) {
            saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
            return;
        }
        UserEntity userEntity = loadUserByUsername(email);
        String tempPassword = SpacePlannerUtil.getTempPassword();
        userEntity.setPassword(passwordEncoder.encodePassword(tempPassword, null));
        userEntity.setUpdatedAt(new Date());
        userEntity.setPasswordType(PasswordType.TEMPORARY);
        userDao.save(userEntity);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEntity.getEmail());
        mailMessage.setSubject(commonUtil.getText("reset.password.mail.subject", status.getLocale()));
        Map<String, String> model = new HashMap<String, String>();
        model.put("name", userEntity.getName());
        model.put("password", tempPassword);
        try {
            MailSenderUtil.send(mailMessage, velocityEngine, FORGOT_PASSWORD_TEMPLATE, model);
        } catch (MessagingException e) {
            logger.error("Unable to send message:" + e.getMessage());
        }
        saveSuccessMessage(status, commonUtil.getText("password.reset.successfully.check.amil"));
    }*/

    private String generateToken(String deviceId) {
        String raw = deviceId + new Date().toString();
        return passwordEncoder.encodePassword(raw, null);
    }
}
