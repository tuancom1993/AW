package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.TestQuestion;

public interface TestQuestionRepository {
    public TestQuestion getTestQuestionById(int id);
    public void addTestQuestion(TestQuestion testQuestion);
    public void deleteTestQuestion(TestQuestion testQuestion);
    public void updateTestQuestion(TestQuestion testQuestion);
    public List<TestQuestion> getTestQuestionListByTestPartId(int testPartId);
    public List<TestQuestion> getTestQuestionListByTestId(int testId);
    public TestQuestion getTestQuestionByTestIdAndQuestionId(int testId, int questionId);
}
