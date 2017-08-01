/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.Result;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface ResultRepository {
    public void addResult(Result result);
    public void deleteResultBySurveyIdAndQuestionId(int surveyId, int questionId);
    public List<Result> getResultsBySurveyIdAndQuestionId(int surveyId, int questionId);
    public long countResultBySurveyIdQuestionIdMatrixRowIdMatrixColumnId(int surveyId, int questionId, int matrixRowId, int matrixColumnId);
    public long countResultBySurveyIdQuestionIdMatrixRowId(int surveyId, int questionId, int matrixRowId);
    public void deleteResultBySurveyId(int surveyId);
    public List<Result> getResultsBySurveyResultIdSurveyIdAndQuestionId(int surveyResultId, int surveyId, int questionId);
    public List<Result> getResultsBySurveyResultIdSurveyIdAndQuestionIdMatrixRowIdMatrixColumnId(int surveyResultId, int surveyId, int questionId, int matrixRowId, int matrixColumnId);
    public Result getResultBySurveyResultIdSurveyIdQuestionId(int surveyResultId, int surveyId, int questionId);
}
