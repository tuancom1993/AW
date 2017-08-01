package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.Test;

public interface TestRepository {
    public Test getTestById(int id);
    public List<Test> getTestList();
    public void addTest(Test test);
    public void updateTest(Test test);
    public void deleteTestById(int id);
    public void deleteTest(Test test);
}
