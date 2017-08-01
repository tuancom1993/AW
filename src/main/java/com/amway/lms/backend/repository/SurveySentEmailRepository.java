package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.SurveySentEmail;

public interface SurveySentEmailRepository {
    public void addSurveySentEmail(SurveySentEmail surveySentEmail);
    public void deleteSurveySentEmail(SurveySentEmail surveySentEmail);
    public List<SurveySentEmail> getSurveySentEmailListBySurveySentId(int surveySentId);
}
