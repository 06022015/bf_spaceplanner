package com.spaceplanner.java.dto;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 11/09/15
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChangePasswordDTO {
    
    private Long userId;
    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
