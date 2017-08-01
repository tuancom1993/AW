package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.TestParticipant;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.TestParticipantRepository;

@Repository
public class TestParticipantRepositoryImpl extends AbstractRepository<Integer, TestParticipant>
        implements TestParticipantRepository {

    @Override
    public TestParticipant getTestParticipantById(int id) {
        // TODO Auto-generated method stub
        return getByKey(id);
    }

    @Override
    public void addTestParticipant(TestParticipant testParticipant) {
        persist(testParticipant);

    }

    @Override
    public void editTestParticipant(TestParticipant testParticipant) {
        update(testParticipant);

    }

    @Override
    public void deleteTestParticipant(TestParticipant testParticipant) {
        delete(testParticipant);

    }

    @Override
    public List<TestParticipant> getTestParticipantListByTestId(int testId) {
        Query query = createNamedQuery("getTestParticipantListByTestId", -1, -1, testId);
        return query.list();
    }

    @Override
    public TestParticipant getTestParticipantByTestIdAndUserId(int testId, int userId) {
        Query query = createNamedQuery("getTestParticipantListByTestIdAndUserId", 0, 1, testId, userId);
        return (TestParticipant) query.uniqueResult();
    }

    @Override
    public List<TestParticipant> searchTestParticipantListByTestIdAndTraineeName(
            int testId, String trainee) {
        trainee = "%" + trainee + "%";
        Query query = createNamedQuery("searchTestParticipantListByTestIdAndTraineeName", -1, -1, testId, trainee, trainee);
        return query.list();
    }

    @Override
    public List<TestParticipant> getTestParticipantByUserId(int userId) {
        Query query = createNamedQuery("getTestParticipantByUserId", -1, -1, userId);
        return query.list();
    }

}
