/**
 * 
 */
package com.amway.lms.backend.model;

import java.util.List;

/**
 * @author acton
 *
 */
public class PageModel {
    private String name;
    private String title;
    private List<QuestionModel> questions;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<QuestionModel> getQuestions() {
        return questions;
    }
    public void setQuestions(List<QuestionModel> questions) {
        this.questions = questions;
    }
}
