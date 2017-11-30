package com.spaceplanner.java.controller;

import com.spaceplanner.java.exception.SpacePlannerResponseStatus;
import com.spaceplanner.java.model.UserEntity;
import com.spaceplanner.java.util.Constants;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 25/8/14
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseController {

    public UserEntity getCurrentUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) authentication.getPrincipal();
    }

    public String sendRedirect(String url) {
        return "redirect:" + url;
    }

    public String redirectToHome() {
        return sendRedirect("/comm/home.html");
    }

    public void saveStatus(HttpServletRequest request, SpacePlannerResponseStatus spacePlannerResponseStatus) {
        request.getSession().setAttribute(Constants.RESPONSE_STATUS, spacePlannerResponseStatus);
    }

    protected void responseAsJSON(HttpServletResponse response, JSONObject object) throws Exception {
        response.setContentType("application/x-json");
        response.getWriter().print(object);
    }
}
