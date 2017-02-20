package net.eulerframework.web.core.base.dao.impl.hibernate5;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import net.eulerframework.web.core.base.dao.IBaseLogicDelDao;
import net.eulerframework.web.core.base.entity.BaseLogicDelEntity;
import net.eulerframework.web.module.authentication.util.UserContext;

public abstract class BaseLogicDelDao<T extends BaseLogicDelEntity<?>> extends BaseModifyInfoDao<T> implements IBaseLogicDelDao<T> {

    @Override
    public T load(Serializable id) {
        T entity = super.load(id);
        if(entity == null)
            return null;
        if(entity.getIfDel())
            return null;
        return entity;
    }
    
    @Override
    public List<T> load(Collection<Serializable> ids) {
        Serializable[] idArray = ids.toArray(new Serializable[0]);
        return this.load(idArray);
    }
    
    @Override
    public List<T> load(Serializable[] idArray) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select en from ");
        hqlBuffer.append(this.entityClass.getSimpleName());
        hqlBuffer.append(" en where en.ifDel = false and (");
        for(int i=0;i<idArray.length;i++) {
            if(i==0) {
                hqlBuffer.append("en.id= ?");
            } else {
                hqlBuffer.append(" or en.id= ?");
            }
        }
        final String hql = hqlBuffer.toString();
        return super.query(hql, (Object[])idArray);        
    }

    @Override
    public Serializable save(T entity) {
        this.setLogicDelInfo(entity);
        return super.save(entity);
    }

    @Override
    public void update(T entity) {
        this.setLogicDelInfo(entity);
        super.update(entity);

    }

    @Override
    public void saveOrUpdate(T entity) {
        this.setLogicDelInfo(entity);
        super.saveOrUpdate(entity);
    }
    
    @Override
    public void saveOrUpdate(Collection<T> entities){
        for(T entity : entities){
            this.setLogicDelInfo(entity);
        }
        super.saveOrUpdate(entities);
    }

    @Override
    public void delete(T entity) {
        entity.setIfDel(true);
        this.update(entity);
    }

    @Override
    public void deleteById(Serializable id) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("update ");
        hqlBuffer.append(this.entityClass.getSimpleName());
        hqlBuffer.append(" en set en.modifyBy = ?, en.modifyDate = ?, en.ifDel = true where en.id = ?");

        final String hql = hqlBuffer.toString();
        this.update(hql, UserContext.getCurrentUser().getId(), new Date(), id);
    }

    @Override
    public void deleteAll(Collection<T> entities) {
        if(entities == null || entities.isEmpty()) return;
        
        Collection<Serializable> idList = new HashSet<>();
        for(T entity : entities){
            idList.add(entity.getId());
        }
        
        this.deleteByIds(idList);
    }

    @Override
    public void deleteByIds(Collection<Serializable> ids) {
        Serializable[] idArray = ids.toArray(new Serializable[0]);
        this.deleteByIds(idArray);
    }

    @Override
    public void deleteByIds(Serializable[] idArray) {
        
        for(Serializable id : idArray) {
            this.deleteById(id);
        }
        
//        StringBuffer hqlBuffer = new StringBuffer();
//        hqlBuffer.append("update ");
//        hqlBuffer.append(this.entityClass.getSimpleName());
//        hqlBuffer.append(" en set en.modifyBy = ?, en.modifyDate = ?, en.ifDel = true where ");
//        for(int i=0;i<idArray.length;i++) {
//            if(i==0) {
//                hqlBuffer.append("en.id= ?");
//            } else {
//                hqlBuffer.append(" or en.id= ?");
//            }
//        }
//        final String hql = hqlBuffer.toString();
//        this.update(hql, UserContext.getCurrentUser().getId(), new Date());
    }
    
    @Override
    public void deletePhysical(T entity) {
        super.delete(entity);
    }

    @Override
    public void deleteByIdPhysical(Serializable id) {
        super.deleteById(id);
    }

    @Override
    public void deleteAllPhysical(Collection<T> entities){
        super.deleteAll(entities);
    }

    @Override
    public void deleteByIdsPhysical(Collection<Serializable> ids){
        super.deleteByIds(ids);
    }

    @Override
    public void deleteByIdsPhysical(Serializable[] idArray){
        super.deleteByIds(idArray);
    }

    @Override
    public List<T> queryByEntity(T entity) {        
        entity.setIfDel(false);
        return super.queryByEntity(entity);
    }

    @Override
    public List<T> queryAll() {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select en from ");
        hqlBuffer.append(this.entityClass.getSimpleName());
        hqlBuffer.append(" en where en.ifDel = false");
        final String hql = hqlBuffer.toString();
        return super.query(hql);
    }

    @Override
    public long countAll() {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select count(*) from ");
        hqlBuffer.append(this.entityClass.getSimpleName());
        hqlBuffer.append(" en where en.ifDel = false");
        final String hql = hqlBuffer.toString();
        List<?> l = super.query(hql);

        if(l!=null && l.size() == 1)
            return (Long)l.get(0);
        return 0;
    }

    private void setLogicDelInfo(T entity){        
        if(entity.getIfDel() == null)
            entity.setIfDel(false);
    }
}
