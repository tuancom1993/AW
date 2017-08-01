package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.TestQuestion;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.TestQuestionRepository;

@Repository
public class TestQuestionRepositoryImpl extends AbstractRepository<Integer, TestQuestion>
        implements TestQuestionRepository {

    @Override
    public TestQuestion getTestQuestionById(int id) {
        return getByKey(id);
    }

    @Override
    public void addTestQuestion(TestQuestion testQuestion) {
        persist(testQuestion);
    }

    @Override
    public void deleteTestQuestion(TestQuestion testQuestion) {
        delete(testQuestion);
    }

    @Override
    public List<TestQuestion> getTestQuestionListByTestPartId(int testPartId) {
        Query query = createNamedQuery("getTestQuestionListByTestPartId", -1, -1, testPartId);
        return query.list();
    }

    @Override
    public TestQuestion getTestQuestionByTestIdAndQuestionId(int testId, int questionId) {
        Query query = createNamedQuery("getTestQuestionByTestIdAndQuestionId", -1, -1, testId, questionId);
        List<TestQuestion> list = query.list();
        if (list.isEmpty()) return null;
        else return list.get(0);
    }

    @Override
    public void updateTestQuestion(TestQuestion testQuestion) {
       update(testQuestion);
    }

    @Override
    public List<TestQuestion> getTestQuestionListByTestId(int testId) {
        Query query = createNamedQuery("getTestQuestionListByTestId", -1, -1, testId);
        return query.list();
    }

}
