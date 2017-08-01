package com.amway.lms.backend.service;

public interface CronJobService {
    public void executeCronJobSyncLDAPUser();
    public void executeCronJobRemindEmailSurvey();
    public void executeCronJobAcceptForTrainee();
    public void executeConJobSendEmailToTrainerForPostSurvey();
    public void executeCronJobBackupDB();
}
