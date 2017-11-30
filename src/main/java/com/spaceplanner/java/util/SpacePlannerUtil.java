package com.spaceplanner.java.util;

import com.spaceplanner.java.model.UserEntity;
import com.spaceplanner.java.model.master.Role;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ashif
 * Date: 10/10/14
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpacePlannerUtil {

    private static final String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public static String getAlphaNumeric(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int ndx = (int) (Math.random() * ALPHA_NUM.length());
            sb.append(ALPHA_NUM.charAt(ndx));
        }
        return sb.toString();
    }

    public static String getTempPassword() {
        return getAlphaNumeric(8);
    }

    public static Long getRoleId(String roleName) {
        Map<String, Long> roleMap = new HashMap<String, Long>();
        roleMap.put(Role.ROLE_ADMIN, 1l);
        roleMap.put(Role.ROLE_SPACE_PLANNER, 2l);
        roleMap.put(Role.ROLE_DESIGNER, 3l);
        roleMap.put(Role.ROLE_COMMERCIAL, 4l);
        return roleMap.get(roleName);
    }

    public static void processRole(UserEntity userEntity) {
        if (null != userEntity.getRoleName() && userEntity.getRoleName().size() > 0) {
            for (String name : userEntity.getRoleName()) {
                Role role = new Role();
                role.setName(name);
                role.setId(getRoleId(name));
                userEntity.addRole(role);
            }
        }
    }

    public static String getFilePath(String baseLocation, Long storeId, String fileName) {
        return baseLocation + storeId + "/" + fileName;
    }

    public static boolean isFloorDesignAreaTolerable(Double floorArea, Double designArea) {
        int tolerantPercentage = Integer.parseInt(CommonUtil.getProperty("floor.area.tolerance.in.percent"));
        return floorArea - designArea > 0 && floorArea - designArea <= floorArea * tolerantPercentage / 100;
    }

}
