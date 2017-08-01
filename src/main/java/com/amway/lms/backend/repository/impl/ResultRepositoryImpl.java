/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Result;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.ResultRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class ResultRepositoryImpl extends AbstractRepository<Integer, Result> implements ResultRepository{

    @Override
    public void addResult(Result result) {
        persist(result);
    }

    /* (non-Javadoc)
     * @see com.amway.lms.backend.repository.ResultRepository#deleteResultBySurveyIdAndQuestionId(int, int)
     */
    @Override
    public void deleteResultBySurveyIdAndQuestionId(int surveyId, int questionId) {
        Query query = createNamedQuery("deleteResultBySurveyIdAndQuestionId", -1, -1, surveyId, questionId);
        query.executeUpdate();
    }

	@Override
	public List<Result> getResultsBySurveyIdAndQuestionId(int surveyId, int questionId) {
        Query query = createNamedQuery("getResultsBySurveyIdAndQuestionId", -1, -1, surveyId, questionId);
        return query.list();
	}

    @Override
    public long countResultBySurveyIdQuestionIdMatrixRowIdMatrixColumnId(int surveyId, int questionId, int matrixRowId,
            int matrixColumnId) {
        Query query = createNamedQuery("countResultBySurveyIdQuestionIdMatrixRowIdMatrixColumnId", -1, -1, surveyId, questionId, matrixRowId, matrixColumnId);
        return (long) query.uniqueResult();
    }

    @Override
    public long countResultBySurveyIdQuestionIdMatrixRowId(int surveyId, int questionId, int matrixRowId) {
        Query query = createNamedQuery("countResultBySurveyIdQuestionIdMatrixRowId", -1, -1, surveyId, questionId, matrixRowId);
        return (long) query.uniqueResult();
    }

    @Override
    public void deleteResultBySurveyId(int surveyId) {
        Query query = createNamedQuery("deleteResultBySurveyId", -1, -1, surveyId);
        query.executeUpdate();
    }

    @Override
    public List<Result> getResultsBySurveyResultIdSurveyIdAndQuestionId(int surveyResultId, int surveyId,
            int questionId) {
        Query query = createNamedQuery("getResultsBySurveyResultIdSurveyIdAndQuestionId", -1, -1,surveyResultId ,surveyId, questionId);
        return query.list();
    }

    @Override
    public List<Result> getResultsBySurveyResultIdSurveyIdAndQuestionIdMatrixRowIdMatrixColumnId(int surveyResultId, 
            int surveyId, int questionId, int matrixRowId, int matrixColumnId) {
        Query query = createNamedQuery("getResultsBySurveyResultIdSurveyIdAndQuestionIdMatrixRowIdMatrixColumnId", -1, -1,surveyResultId ,surveyId, questionId, matrixRowId, matrixColumnId);
        return query.list();
    }

    @Override
    public Result getResultBySurveyResultIdSurveyIdQuestionId(int surveyResultId, int surveyId, int questionId) {
        List<Result> results = getResultsBySurveyResultIdSurveyIdAndQuestionId(surveyResultId, surveyId, questionId);
        if(results == null || results.isEmpty()) return null;
        else return results.get(0);
    }
    

}
