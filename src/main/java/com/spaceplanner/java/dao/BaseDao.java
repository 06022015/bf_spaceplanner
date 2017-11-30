package com.spaceplanner.java.dao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 20/8/14
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BaseDao {
    
    void executeSQLQueryUpdate(String sqlQuery);

    void save(Object object);
    
    void saveAll(List objectList);

    void update(Object object);
    
    Object get(Class clazz, Long id);


}
