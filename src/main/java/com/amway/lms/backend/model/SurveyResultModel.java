package com.amway.lms.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amway.lms.backend.entity.Question;

public class SurveyResultModel {
    private List<Question> questions;
    private Map<Integer, ArrayList<String>> mapResults;
    
    public List<Question> getQuestions() {
        return questions;
    }
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    public Map<Integer, ArrayList<String>> getMapResults() {
        return mapResults;
    }
    public void setMapResults(Map<Integer, ArrayList<String>> mapResults) {
        this.mapResults = mapResults;
    }
}
