package net.eulerframework.web.module.authentication.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import net.eulerframework.common.util.StringUtils;
import net.eulerframework.web.core.base.dao.impl.hibernate5.BaseDao;
import net.eulerframework.web.core.base.request.PageQueryRequest;
import net.eulerframework.web.core.base.response.easyuisupport.EasyUIPageResponse;
import net.eulerframework.web.core.extend.hibernate5.RestrictionsX;
import net.eulerframework.web.module.authentication.entity.Group;

public class GroupDao extends BaseDao<Group> {

    
    public EasyUIPageResponse<Group> findGroupByPage(PageQueryRequest pageQueryRequest) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(this.entityClass)
                .setFetchMode("authorities", FetchMode.SELECT);
        try {
            String queryValue = null;
            queryValue = pageQueryRequest.getQueryValue("name");
            if (!StringUtils.isNull(queryValue)) {
                detachedCriteria.add(RestrictionsX.like("name", queryValue, MatchMode.ANYWHERE).ignoreCase());
            }
            queryValue = pageQueryRequest.getQueryValue("description");
            if (!StringUtils.isNull(queryValue)) {
                detachedCriteria.add(RestrictionsX.like("description", queryValue, MatchMode.ANYWHERE).ignoreCase());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        detachedCriteria.addOrder(Order.asc("name"));        
        
        EasyUIPageResponse<Group> result = this.pageQuery(detachedCriteria, pageQueryRequest.getPageIndex(), pageQueryRequest.getPageSize());
        
        return result;
    }

    
    public List<Group> findAllGroupsInOrder() {
        List<Group> result = this.queryAll();

        Comparator<Group> c = new Comparator<Group>() {  
              
            public int compare(Group o1, Group o2) {  
                return StringUtils.toPinYinString(o1.getName()).compareTo(StringUtils.toPinYinString(o2.getName()));  
            }
        };
        
        Collections.sort(result, c);
        return result;
    }

    
    public Group findSystemUsersGroup() {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(this.entityClass)
                .setFetchMode("authorities", FetchMode.SELECT);
        detachedCriteria.add(Restrictions.eq("name", Group.SYSTEM_USERS_CROUP_NAME));
        List<Group> result = this.query(detachedCriteria);
        if(result == null || result.isEmpty())
            throw new RuntimeException("System Users Group Not Found");
        return result.get(0);
    }

}
