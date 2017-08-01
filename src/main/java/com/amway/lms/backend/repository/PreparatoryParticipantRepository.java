package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.PreparatoryParticipant;

public interface PreparatoryParticipantRepository {

    public void addPreparatoryParticipant(
            PreparatoryParticipant preparatoryParticipant);

    public List<PreparatoryParticipant> getPreparatoryParticipant(
            int trainingPlanId, int userId);
    
    public List<PreparatoryParticipant> getPreparatoryParticipantByTrainingPlanId(int trainingPlanId);
    
    public List<PreparatoryParticipant> getPreparatoryParticipantByCourse(int courseId);

    public void delPreparatoryParticipant(
            List<PreparatoryParticipant> preparatoryParticipants);
    
    public PreparatoryParticipant getPreparatoryParticipantByCourseIdAndUserId(int courseId, int userId,int sessionId);

}
