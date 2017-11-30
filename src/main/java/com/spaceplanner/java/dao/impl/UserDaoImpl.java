package com.spaceplanner.java.dao.impl;

import com.spaceplanner.java.dao.UserDao;
import com.spaceplanner.java.exception.DuplicateObjectException;
import com.spaceplanner.java.model.UserEntity;
import com.spaceplanner.java.model.master.Role;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 20/8/14
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

    public Role getRole(String name) {
        Criteria criteria  = getCurrentSession().createCriteria(Role.class)
                .add(Restrictions.eq("name", name));
        return (Role)criteria.uniqueResult();
    }


    public List<UserEntity> getUser() {
        Criteria criteria = getCurrentSession().createCriteria(UserEntity.class)
                .add(Restrictions.eq("enabled", true));
        return criteria.list();
    }

    public UserEntity getUserByUsername(String username){
        Criteria criteria = getCurrentSession().createCriteria(UserEntity.class)
                .add(Restrictions.eq("username", username.trim()));
        return (UserEntity)criteria.uniqueResult();
    }

    public UserEntity getUser(Long id) {
        Criteria criteria = getCurrentSession().createCriteria(UserEntity.class)
                .add(Restrictions.eq("id", id))
                .setFetchMode("roles", FetchMode.SELECT);
        return (UserEntity)criteria.uniqueResult();
    }

    public void saveUser(UserEntity userEntity) throws DuplicateObjectException{
        Criteria criteria = getCurrentSession().createCriteria(UserEntity.class)
                .add(Restrictions.or(Restrictions.eq("username", userEntity.getUsername()),
                        Restrictions.eq("mobile", userEntity.getMobile())));
        if(null!= userEntity.getId()){
            criteria.add(Restrictions.ne("id", userEntity.getId()));
        }
        UserEntity users = (UserEntity)criteria.uniqueResult();
        if(null!=users)
            throw new DuplicateObjectException("User name or mobile already exist");
        save(userEntity);
    }

    public void updateProfile(String name, String value, Long id) throws SQLException{
        String query = "update user set "+ name+" = '" +value+"'where id="+id+"";
        getCurrentSession().createSQLQuery(query).executeUpdate();
    }


    public List<UserEntity> getUserByRole(String userRole) {
        Criteria criteria = getCurrentSession().createCriteria(UserEntity.class)
                .createAlias("roles","role")
                .add(Restrictions.eq("role.name", userRole))
                .add(Restrictions.eq("enabled",true));
        return criteria.list();
    }


}
