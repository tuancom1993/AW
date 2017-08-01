package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.SurveySent;

public interface SurveySentRepository {
    public void addSurveySent(SurveySent surveySent);
    public void deleteSureySent(SurveySent surveySent);
    public List<SurveySent> getSurveySentList();
    public SurveySent getSurveySentById(int surveySentId);
    public List<SurveySent> getSurveySentBySurveyId(int surveyId);
    public SurveySent getLastSurveySentBySurveyId(int surveyId);
    public SurveySent getFirstSurveySentBySurveyId(int surveyId);
    public void updateSurveySent(SurveySent surveySent);
}
