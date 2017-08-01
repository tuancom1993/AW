package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.SurveySentEmail;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.SurveySentEmailRepository;

@Repository
public class SurveySentEmailRepositoryRepository extends AbstractRepository<Integer, SurveySentEmail> implements SurveySentEmailRepository{

    @Override
    public void addSurveySentEmail(SurveySentEmail surveySentEmail) {
        persist(surveySentEmail);
        
    }

    @Override
    public void deleteSurveySentEmail(SurveySentEmail surveySentEmail) {
        delete(surveySentEmail);
        
    }

    @Override
    public List<SurveySentEmail> getSurveySentEmailListBySurveySentId(int surveySentId) {
        Query query = createNamedQuery("getSurveySentEmailListBySurveySentId", -1, -1, surveySentId);
        return query.list();
    }

}
