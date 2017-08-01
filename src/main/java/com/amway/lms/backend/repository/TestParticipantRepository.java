package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.TestParticipant;

public interface TestParticipantRepository {
    public TestParticipant getTestParticipantById(int id);
    public List<TestParticipant> getTestParticipantByUserId(int userId);
    public void addTestParticipant(TestParticipant testParticipant);
    public void editTestParticipant(TestParticipant testParticipant);
    public void deleteTestParticipant(TestParticipant testParticipant);
    public List<TestParticipant> getTestParticipantListByTestId(int testId);
    public List<TestParticipant> searchTestParticipantListByTestIdAndTraineeName(int testId, String trainee);
    public TestParticipant getTestParticipantByTestIdAndUserId(int testId, int userId);
}
