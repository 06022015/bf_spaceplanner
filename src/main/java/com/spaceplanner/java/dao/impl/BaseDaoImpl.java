package com.spaceplanner.java.dao.impl;

import com.spaceplanner.java.dao.BaseDao;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 20/8/14
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */

public class BaseDaoImpl implements BaseDao {

    private Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
  }


    public void executeSQLQueryUpdate(String sqlQuery) {
        SQLQuery query =getCurrentSession().createSQLQuery(sqlQuery);
        query.executeUpdate();
    }

    public void save(Object object) {
        getCurrentSession().saveOrUpdate(object);
    }


    public void saveAll(List objectList) {
        for (Object object : objectList) {
            try{
                getCurrentSession().saveOrUpdate(object);
            }catch (JDBCException jdbce){
                logger.error(jdbce.toString());
               if(jdbce.getErrorCode()==1062 && jdbce.getSQLState().equals("23000")){
                   logger.error("Duplicate Object :- "+ object.toString());
               }else{
                   //throw jdbce;
               }
            }
        }
    }

    public void update(Object object){
        getCurrentSession().update(object);
    }


    public Object get(Class clazz, Long id) {
        Criteria criteria  = getCurrentSession().createCriteria(clazz)
                .add(Restrictions.eq("id", id));
        return criteria.uniqueResult();
    }

    protected Session getCurrentSession() {
        Session session = null;
        try{
            session = sessionFactory.getCurrentSession();
        }catch (HibernateException he){
            session = sessionFactory.openSession();
        }
        if(null == session)
            session = sessionFactory.openSession();
        return session;
    }

}
