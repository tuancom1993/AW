/**
 * 
 */
package com.amway.lms.backend.model;

import java.util.List;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public class SurveyModel {
    private String title;
    private String name;
    private String questionTitleTemplate;
    private String requiredText;
    private List<PageModel> pages;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestionTitleTemplate() {
        return questionTitleTemplate;
    }

    public void setQuestionTitleTemplate(String questionTitleTemplate) {
        this.questionTitleTemplate = questionTitleTemplate;
    }

    public String getRequiredText() {
        return requiredText;
    }

    public void setRequiredText(String requiredText) {
        this.requiredText = requiredText;
    }

    public List<PageModel> getPages() {
        return pages;
    }

    public void setPages(List<PageModel> pages) {
        this.pages = pages;
    }

}
