/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.SurveyType;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.SurveyTypeRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class SurveyTypeRepositoryImpl extends AbstractRepository<Integer, SurveyType> implements SurveyTypeRepository{
    @Override
    public SurveyType getSurveyTypeById(int id) {
        return getByKey(id);
    }

}
