package com.spaceplanner.java.task;

import com.spaceplanner.java.dao.CommonDao;
import com.spaceplanner.java.dao.UserDao;
import com.spaceplanner.java.util.CommonUtil;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 06/11/15
 * Time: 11:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SpacePlannerBeans {

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CommonDao commonDao ;

    @Autowired
    private UserDao userDao;

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public CommonUtil getCommonUtil() {
        return commonUtil;
    }

    public CommonDao getCommonDao() {
        return commonDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
