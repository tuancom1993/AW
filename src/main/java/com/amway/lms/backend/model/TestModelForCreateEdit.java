package com.amway.lms.backend.model;

import java.util.List;

import com.amway.lms.backend.entity.TestPart;

public class TestModelForCreateEdit {
    private TestInformation testInfo;
    private List<TestPart> parts;
    
    public TestInformation getTestInfo() {
        return testInfo;
    }
    public void setTestInfo(TestInformation testInfo) {
        this.testInfo = testInfo;
    }
    public List<TestPart> getParts() {
        return parts;
    }
    public void setParts(List<TestPart> parts) {
        this.parts = parts;
    }

}
