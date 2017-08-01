package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.TestPart;

public interface TestPartRepository {
    public TestPart getTestPartById(int id);
    public void addTestPart(TestPart part);
    public void updateTestPart(TestPart testPart);
    public void deleteTestPart(TestPart testPart);
    public List<TestPart> getTestPartListByTestId(int testId);
}
