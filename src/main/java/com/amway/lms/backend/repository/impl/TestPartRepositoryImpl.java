package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.TestPart;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.TestPartRepository;

@Repository
public class TestPartRepositoryImpl extends AbstractRepository<Integer, TestPart> implements TestPartRepository {

    @Override
    public TestPart getTestPartById(int id) {
        return getByKey(id);
    }

    @Override
    public void addTestPart(TestPart part) {
        System.err.println("ID TEST PART: "+part.getId());
        persist(part);
    }

    @Override
    public void updateTestPart(TestPart testPart) {
        update(testPart);
    }

    @Override
    public void deleteTestPart(TestPart testPart) {
        delete(testPart);

    }

    @Override
    public List<TestPart> getTestPartListByTestId(int testId) {
        Query query = createNamedQuery("getTestPartListByTestId", -1, -1, testId);
        return query.list();
    }

}
