package com.spaceplanner.java.bl;

import com.spaceplanner.java.exception.SpacePlannerResponseStatus;
import com.spaceplanner.java.model.UserEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 20/8/14
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserBL {

    UserEntity loadUserByUsername(String username) throws UsernameNotFoundException;

    List<UserEntity> getUser();

    UserEntity getUser(Long id);

    UserEntity save(UserEntity userEntity, SpacePlannerResponseStatus status);
    
    void delete(Long id);

    void updateProfile(String name, String value, Long id, SpacePlannerResponseStatus status);

    void forgotPassword(String username, SpacePlannerResponseStatus status);

    void changePassword(String oldPassword, String password, String confirmPassword,Long id, SpacePlannerResponseStatus status);

    /*void identify(String email, SpacePlannerResponseStatus status);*/

}
