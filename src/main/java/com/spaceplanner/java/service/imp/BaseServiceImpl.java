package com.spaceplanner.java.service.imp;

import com.spaceplanner.java.bl.UserBL;
import com.spaceplanner.java.model.UserEntity;
import com.spaceplanner.java.util.ApplicationContextUtil;


/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 25/8/14
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseServiceImpl {

    private UserEntity user;

    /*public BaseServiceImpl(@Context SecurityContext security) {
        this.user = (UserEntity)security.getUserPrincipal();
        //this.user=new UserEntity();
        //getUser().setId(1l);
    }*/

    protected UserEntity getUser() {
        return user;
    }

    protected boolean isUserInRole(String roleName){
        return getUser().isUserInRole(roleName);
    }



    protected UserBL getUserBL() {
        return (UserBL)ApplicationContextUtil.getApplicationContext().getBean("userBL");
    }

}
