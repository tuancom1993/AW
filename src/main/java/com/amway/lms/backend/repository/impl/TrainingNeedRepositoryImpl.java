/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.TrainingNeed;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.TrainingNeedRepository;

/**
 * @author acton
 *
 */
@Repository
public class TrainingNeedRepositoryImpl extends AbstractRepository<Integer, TrainingNeed> implements TrainingNeedRepository {

    
    /* (non-Javadoc)
     * @see com.amway.lms.backend.dao.EntityDao#addEntity(java.lang.Object)
     */
    @Override
    public void addTrainingNeed(TrainingNeed trainingNeed) {
        persist(trainingNeed);
    }

    

    /* (non-Javadoc)
     * @see com.amway.lms.backend.dao.EntityDao#getEntityById(int)
     */
    @Override
    public TrainingNeed getTrainingNeedById(int id) {
        return getByKey(id);
    }

    /* (non-Javadoc)
     * @see com.amway.lms.backend.dao.TrainingNeedDao#getListTraningNeedByUserId(int)
     */
    @Override
    public List<TrainingNeed> getListTraningNeedByUserId(int userId) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("userId", userId));
        criteria.addOrder(Order.desc("id"));
        return criteria.list();
    }

    @Override
    public boolean exists(int id) {
        try {
//            Criteria criteria = createEntityCriteria();
//            criteria.add(Restrictions.eq("id", id));
//            return criteria.uniqueResult() != null;
            return getByKey(id) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
