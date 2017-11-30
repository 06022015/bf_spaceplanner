package com.spaceplanner.java.dao.impl;

import com.spaceplanner.java.dao.CommonDao;
import com.spaceplanner.java.model.FloorDesignDetailsEntity;
import com.spaceplanner.java.model.FloorEntity;
import com.spaceplanner.java.model.StoreEntity;
import com.spaceplanner.java.model.master.BrandEntity;
import com.spaceplanner.java.model.master.CategoryDivision;
import com.spaceplanner.java.model.type.DesignStatus;
import com.spaceplanner.java.model.type.Status;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 13/07/15
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("commonDao")
public class CommonDaoImpl extends BaseDaoImpl implements CommonDao {

    public List<StoreEntity> getStores(Status status) {
        Criteria criteria = getCurrentSession().createCriteria(StoreEntity.class)
                .add(Restrictions.eq("status", status));
        return criteria.list();
    }


    public List<FloorEntity> getFloors(Long storeId, Status status) {
        Criteria criteria = getCurrentSession().createCriteria(FloorEntity.class)
                .createAlias("store","store")
                .add(Restrictions.eq("store.id", storeId))
                .add(Restrictions.eq("store.status", Status.ACTIVE))
                .add(Restrictions.eq("status", status));
        return criteria.list();
    }
    

    public List<FloorEntity> getArchiveFloors(Long storeId) {
        String hql ="from FloorEntity f  where f.store.id=:storeId and f.store.status= :storeStatus and f.status=:status group by floorNumber";
        Query query = getCurrentSession().createQuery(hql)
                .setLong("storeId",storeId)
                .setParameter("storeStatus", Status.ACTIVE)
                .setParameter("status", Status.ARCHIVED);
        return query.list();
    }


    public List<FloorEntity> getRecentFloors(int length, Status status) {
        Criteria criteria = getCurrentSession().createCriteria(FloorEntity.class)
                .add(Restrictions.eq("status", status))
                .addOrder(Order.desc("updatedAt"))
                .setMaxResults(length);
        return criteria.list();
    }


    public FloorEntity getFloorByFloorNumber(Long storeId, String floorNumber) {
        Criteria criteria = getCurrentSession().createCriteria(FloorEntity.class)
                .createAlias("store", "store")
                .add(Restrictions.eq("store.id", storeId))
                .add(Restrictions.eq("status", Status.ACTIVE))
                .add(Restrictions.eq("floorNumber",floorNumber));
        return (FloorEntity)criteria.uniqueResult();
    }



    public Integer getArchiveFloorMaxVersion(Long storeId, String floorNumber) {
        Criteria criteria = getCurrentSession().createCriteria(FloorEntity.class)
                .createAlias("store", "store")
                .add(Restrictions.eq("store.id", storeId))
                .add(Restrictions.eq("floorNumber",floorNumber))
                .add(Restrictions.eq("status", Status.ARCHIVED))
                .addOrder(Order.desc("version"))
                .setProjection(Projections.property("version"))
                .setMaxResults(1);
        return (Integer)criteria.uniqueResult();
    }


    public FloorEntity getFloorByNameAndVersion(Long storeId, String floorNumber, Integer version) {
        Criteria criteria = getCurrentSession().createCriteria(FloorEntity.class)
                .createAlias("store", "store")
                .add(Restrictions.eq("store.id", storeId))
                .add(Restrictions.eq("store.status", Status.ACTIVE))
                .add(Restrictions.eq("floorNumber",floorNumber))
                .add(Restrictions.eq("version",version));
        List floors = criteria.list();
        return null!= floors && floors.size()>0? (FloorEntity) floors.get(0) :null;
    }


    public List<FloorDesignDetailsEntity> getFloorDesignDetails(Long floorId) {
        Criteria criteria  = getCurrentSession().createCriteria(FloorDesignDetailsEntity.class)
                .createAlias("floor","floor")
                .add(Restrictions.eq("floor.id", floorId));
        return criteria.list();
    }


    public void deleteFloorDetails(Long floorId, int version) {
        String hql = "delete from FloorDesignDetailsEntity fdd where fdd.floor.id in (from FloorEntity f where f.id=:floorId and f.designStatus=:designStatus)";
        Query query = getCurrentSession().createQuery(hql);
        query.setLong("floorId", floorId);
        query.setParameter("designStatus", DesignStatus.Space_Design_Uploaded);
        query.executeUpdate();
    }


    public StoreEntity getStoreByName(String storeName) {
        Criteria criteria = getCurrentSession().createCriteria(StoreEntity.class)
                .add(Restrictions.eq("name", storeName))
                .add(Restrictions.eq("status", Status.ACTIVE));
        return (StoreEntity)criteria.uniqueResult();
    }


    public BrandEntity getBrandByNameAndCode(String code, String name) {
        Criteria criteria = getCurrentSession().createCriteria(BrandEntity.class)
                .add(Restrictions.eq("code", code.trim()).ignoreCase())
                .add(Restrictions.eq("name", name.trim()).ignoreCase());
        return (BrandEntity)criteria.uniqueResult();
    }


    public List<BrandEntity> getBrands() {
        Criteria criteria = getCurrentSession().createCriteria(BrandEntity.class);
        return criteria.list();
    }
    
    
    public boolean isValidBrandDesign(Long floorId){
        String hql = "from FloorDesignDetailsEntity fdd where fdd.floor.id =:floorId and  fdd.brand != null and fdd.designBrandName != fdd.brand.name";
        Query query = getCurrentSession().createQuery(hql);
        query.setLong("floorId", floorId);
        List<FloorDesignDetailsEntity> floorDesignDetailsList = query.list();
        return !(null!=floorDesignDetailsList && floorDesignDetailsList.size()>0);
    }


    public CategoryDivision getCategoryDivisionByCategory(String category) {
        Criteria criteria = getCurrentSession().createCriteria(CategoryDivision.class)
                .add(Restrictions.eq("category",category).ignoreCase());
        return (CategoryDivision)criteria.uniqueResult();
    }


    public Map<String, CategoryDivision> getCategoryDivision() {
        Criteria criteria = getCurrentSession().createCriteria(CategoryDivision.class);
        List<CategoryDivision> categoryDivisionList = criteria.list();
        Map<String, CategoryDivision> categoryDivisionMap = new HashMap<String, CategoryDivision>();
        for(CategoryDivision categoryDivision : categoryDivisionList){
            String key = categoryDivision.getCategory().replaceAll("\\s","");
          categoryDivisionMap.put(key.toLowerCase(),categoryDivision);
        }
        return categoryDivisionMap;
    }

    public Double getSumOfArea(String columnName, String floorId){
        Criteria criteria = getCurrentSession().createCriteria(FloorDesignDetailsEntity.class)
                .createAlias("floor", "floor")
                .add(Restrictions.eq("floor.id", floorId))
                .setProjection(Projections.sum("area"));
        return (Double)criteria.uniqueResult();
    }



}
