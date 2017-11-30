package com.spaceplanner.java.controller;

import com.spaceplanner.java.bl.UserBL;
import com.spaceplanner.java.exception.SpacePlannerResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 25/8/14
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
/*@RequestMapping("/ann")*/
public class AnonymousController extends BaseController{

    @Autowired
    public UserBL userBL;

    @RequestMapping("/login")
    public String login(HttpServletRequest request){
        try {
            if (request.getUserPrincipal() != null) {
                return redirectToHome();
            }
        } catch (Exception e) {
            System.out.print("Error occur:- " + e.getMessage());
        }
        return "login";
    }

    @RequestMapping("/register")
    public String register(HttpServletRequest request){
        return "login";
    }

    @RequestMapping("/forgot/password/form")
    public String forgotPasswordForm(HttpServletRequest request){
        return "forgotPasswordForm";
    }

    @RequestMapping("/forgot/password")
    public String forgotPassword(HttpServletRequest request,
                                 @RequestParam(value = "email", required = true) String email){
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        userBL.forgotPassword(email,status);
        saveStatus(request,status);
        if(status.getCode()!= HttpStatus.OK.value()){
            return sendRedirect("/forgot/password/form.html");
        }
        return sendRedirect("/login.html");
    }

    @RequestMapping("/username/validate")
    public String validateUserName(HttpServletRequest request, /*@QueryParam("username")*/String username){
        return "";
    }

}
