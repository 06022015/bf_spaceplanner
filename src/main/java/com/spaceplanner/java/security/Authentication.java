package com.spaceplanner.java.security;

import com.spaceplanner.java.exception.SpacePlannerResponseStatus;
import com.spaceplanner.java.model.UserEntity;
import com.spaceplanner.java.util.CommonUtil;
import com.spaceplanner.java.util.Constants;
import com.spaceplanner.java.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 20/8/14
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class Authentication extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationFailureHandler {

    @Autowired
    private CommonUtil commonUtil;



    public void setLogoutUrl(String logoutUrl) {
        //To change body of created methods use File | Settings | File Templates.
    }


    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
        System.out.print("Loggedin");
        UserEntity usersEntity = (UserEntity) authentication.getPrincipal();
        HttpSession session = request.getSession();
        session.setAttribute(Constants.LOGGED_IN_USER_ID, usersEntity.getId());
        session.setAttribute(Constants.LOGGED_IN_USERNAME, usersEntity.getEmail());
        session.setAttribute(Constants.LOGGED_IN_FULL_NAME, usersEntity.getName());
        session.setAttribute(Constants.PATTERN_DOUBLE, ValidatorUtil.PATTERN_DOUBLE);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
        System.out.print("Logg-in failed"+ e.getMessage());
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        status.setCode(HttpStatus.FORBIDDEN.value());
        status.setMessage(commonUtil.getText("error.credential.invalid", request.getLocale()));
        request.getSession().setAttribute(Constants.RESPONSE_STATUS, status);
        response.sendRedirect(request.getContextPath() + "/login.html");
    }
}
