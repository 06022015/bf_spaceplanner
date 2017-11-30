package com.spaceplanner.java.controller;

import com.spaceplanner.java.bl.UserBL;
import com.spaceplanner.java.dto.ChangePasswordDTO;
import com.spaceplanner.java.exception.SpacePlannerResponseStatus;
import com.spaceplanner.java.model.UserEntity;
import com.spaceplanner.java.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 20/8/14
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserBL userBL;

    @Autowired
    private CommonUtil commonUtil;

    @RequestMapping(value = "show", method = RequestMethod.GET)
    public String getUser(HttpServletRequest request) {
        List<UserEntity> userEntityList = userBL.getUser();
        request.setAttribute("users", userEntityList);
        return "userList";
    }

    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String userForm(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        UserEntity userEntity = null;
        if (id != null && !"".equals(id.trim())) {
            userEntity = userBL.getUser(Long.parseLong(id));
        }
        if (userEntity == null) {
            userEntity = new UserEntity();
        }
        return form(request, userEntity);
    }

    private String form(HttpServletRequest request, UserEntity userEntity) throws Exception {
        request.setAttribute("user", userEntity);
        return "userForm";
    }
    @RequestMapping(value = "/save" , method = RequestMethod.POST)
    public String saveUser(HttpServletRequest request, @ModelAttribute(value = "user" ) UserEntity user) throws Exception {
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        userBL.save(user, status);
        saveStatus(request, status);
        if (status.getCode() != HttpStatus.OK.value()) {
            return form(request, user);
        }
        return sendRedirect("/user/show.html");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteUser(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        userBL.delete(id);
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus(HttpStatus.OK.value());
        status.setMessage(commonUtil.getText("success.delete"));
        saveStatus(request, status);
        return sendRedirect("/user/show.html");
    }

    @RequestMapping(value = "/password/change/form", method = RequestMethod.GET)
    public String changePasswordForm(HttpServletRequest request) {
        ChangePasswordDTO changePassword = new ChangePasswordDTO();
        return form(request,changePassword);
    }
    
    private String form(HttpServletRequest request, ChangePasswordDTO changePassword){
        request.setAttribute("changePassword", changePassword);
        return "changePasswordForm";
    }

    @RequestMapping(value = "/password/change/save", method = RequestMethod.POST)
    public String changePassword(HttpServletRequest request, ChangePasswordDTO changePassword) {
        changePassword.setUserId(getCurrentUsers().getId());
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        userBL.changePassword(changePassword.getOldPassword(), changePassword.getNewPassword(),
                changePassword.getConfirmPassword(), changePassword.getUserId(), status);
        saveStatus(request,status);
        if (status.getCode() != HttpStatus.OK.value()) {
            return sendRedirect("/user/password/change/save.html");
        }
        return redirectToHome();
    }
}
