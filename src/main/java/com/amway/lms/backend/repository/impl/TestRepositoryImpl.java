package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Test;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.TestRepository;

@Repository
public class TestRepositoryImpl extends AbstractRepository<Integer, Test> implements TestRepository{

    @Override
    public Test getTestById(int id) {
        return getByKey(id);
    }

    @Override
    public List<Test> getTestList() {
        Query query = createNamedQuery("getTestList", -1, -1);
        return query.list();
    }

    @Override
    public void addTest(Test test) {
        persist(test);
    }

    @Override
    public void updateTest(Test test) {
        update(test);
    }

    @Override
    public void deleteTestById(int id) {
        Test entity = new Test();
        entity.setId(id);
        delete(entity);
    }

    @Override
    public void deleteTest(Test test) {
        delete(test);
    }

}
