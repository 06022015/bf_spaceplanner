package com.spaceplanner.java.task;

import com.spaceplanner.java.dao.UserDao;
import com.spaceplanner.java.model.UserEntity;
import com.spaceplanner.java.model.master.Role;
import com.spaceplanner.java.model.type.DesignStatus;
import com.spaceplanner.java.util.CommonUtil;
import com.spaceplanner.java.util.Constants;
import com.spaceplanner.java.util.MailSenderUtil;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.MessagingException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 31/10/15
 * Time: 1:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class NotifyUserTask {

    private Logger logger = LoggerFactory.getLogger(NotifyUserTask.class);

    private UserDao userDao;
    private VelocityEngine velocityEngine;
    private CommonUtil commonUtil;

    public NotifyUserTask(SpacePlannerBeans spacePlannerBeans) {
        this.velocityEngine = spacePlannerBeans.getVelocityEngine();
        this.userDao = spacePlannerBeans.getUserDao();
        this.commonUtil = spacePlannerBeans.getCommonUtil();
    }


    public void execute(Map<String, Object> map) {
        logger.info("Notifying user.....");
        String templateName = Constants.NOTIFICATION_MAIL_TEMPLATE;
        DesignStatus designStatus = (DesignStatus) map.get("designStatus");
        List<UserEntity> userList = getUsersForNotification(designStatus);
        if (userList.size() <= 0) {
            return;
        }
        map.put("content", getContent(designStatus, new Object[]{map.get("floorNumber"), map.get("storeName")}));
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(getSubject(designStatus, new Object[]{map.get("storeName")}));
        boolean isNotificationSend = true;
        for (UserEntity user : userList) {
            mailMessage.setTo(user.getEmail());
            map.put("name", user.getName());
            try {
                MailSenderUtil mailSenderUtil = new MailSenderUtil(getProperties());
                mailSenderUtil.send(mailMessage, velocityEngine, templateName, map);
            } catch (MessagingException e) {
                logger.error("Unable to send mail to user " + e);
                isNotificationSend = false;
            }
        }
        if (designStatus.equals(DesignStatus.Brand_Design_published))
            notifyAdmin(map);
        if (isNotificationSend)
            logger.info("Sent notification mail to the respective user!");
    }

    public void notifyAdmin(Map<String, Object> map) {
        String templateName = Constants.NOTIFICATION_MAIL_TEMPLATE;
        List<UserEntity> userList = userDao.getUserByRole(Role.ROLE_ADMIN);
        map.put("content", commonUtil.getText("email.notification.content.floor.enrich", new Object[]{map.get("storeName")}));
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(commonUtil.getText("email.notification.subject.floor.enrich", new Object[]{map.get("storeName")}));
        for (UserEntity user : userList) {
            mailMessage.setTo(user.getEmail());
            map.put("name", user.getName());
            try {
                MailSenderUtil mailSenderUtil = new MailSenderUtil(getProperties());
                mailSenderUtil.send(mailMessage, velocityEngine, templateName, map);
            } catch (MessagingException e) {
                logger.error("Unable to send mail to user " + e);
            }
        }
    }

    private List<UserEntity> getUsersForNotification(DesignStatus designStatus) {
        List<UserEntity> userList = new ArrayList<UserEntity>();
        if (designStatus.equals(DesignStatus.Master_Published)
                || designStatus.equals(DesignStatus.Space_Design_Published)
                || designStatus.equals(DesignStatus.Brand_Master_Published)
                || designStatus.equals(DesignStatus.Brand_Design_published)) {
            userList.addAll(userDao.getUserByRole(Role.ROLE_DESIGNER));
        } else if (designStatus.equals(DesignStatus.Space_Design_Uploaded)
                || designStatus.equals(DesignStatus.Brand_Design_Uploaded)) {
            userList.addAll(userDao.getUserByRole(Role.ROLE_SPACE_PLANNER));
        } /*else if (designStatus.equals(DesignStatus.Enrichment_Uploaded)) {
            userList.addAll(userDao.getUserByRole(Role.ROLE_ADMIN));
        }*/
        return userList;
    }


    private String getSubject(DesignStatus designStatus, Object objects[]) {

        Map<DesignStatus, String> subjectMap = new HashMap<DesignStatus, String>();
        subjectMap.put(DesignStatus.Master_Published, commonUtil.getText("email.notification.subject.floor.master", objects));
        subjectMap.put(DesignStatus.Space_Design_Published, commonUtil.getText("email.notification.subject.floor.space.design", objects));
        subjectMap.put(DesignStatus.Brand_Master_Published, commonUtil.getText("email.notification.subject.floor.brand.master", objects));
        subjectMap.put(DesignStatus.Brand_Design_published, commonUtil.getText("email.notification.subject.floor.brand.design", objects));
        //subjectMap.put(DesignStatus.Enrichment_Uploaded, commonUtil.getText("email.notification.subject.floor.enrich", objects));
        return subjectMap.get(designStatus);
    }

    private String getContent(DesignStatus designStatus, Object objects[]) {
        Map<DesignStatus, String> subjectMap = new HashMap<DesignStatus, String>();
        subjectMap.put(DesignStatus.Master_Published, commonUtil.getText("email.notification.content.floor.master", objects));
        subjectMap.put(DesignStatus.Space_Design_Published, commonUtil.getText("email.notification.content.floor.space.design", objects));
        subjectMap.put(DesignStatus.Brand_Master_Published, commonUtil.getText("email.notification.content.floor.brand.master", objects));
        subjectMap.put(DesignStatus.Brand_Design_published, commonUtil.getText("email.notification.content.floor.brand.design", objects));
        //subjectMap.put(DesignStatus.Enrichment_Uploaded, commonUtil.getText("email.notification.content.floor.enrich", objects));
        return subjectMap.get(designStatus);
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", CommonUtil.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.host", CommonUtil.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", CommonUtil.getProperty("mail.smtp.port"));
        props.put("mail.smtp.auth", CommonUtil.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.from.email", CommonUtil.getProperty("mail.smtp.from.email"));
        props.put("mail.smtp.user", CommonUtil.getProperty("mail.smtp.user"));
        props.put("mail.smtp.password", CommonUtil.getProperty("mail.smtp.password"));
        return props;
    }


    public void notifyUserPassword(Map<String, Object> map, String email) {
        String templateName = Constants.FORGOT_PASSWORD_TEMPLATE;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(commonUtil.getText("email.notification.subject.forgot.password"));
        mailMessage.setTo(email);
        try {
            MailSenderUtil mailSenderUtil = new MailSenderUtil(getProperties());
            mailSenderUtil.send(mailMessage, velocityEngine, templateName, map);
        } catch (MessagingException e) {
            logger.error("Unable to send mail to user " + e);
            //e.printStackTrace();
        }
    }

}
