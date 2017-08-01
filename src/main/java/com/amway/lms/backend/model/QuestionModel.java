/**
 * 
 */
package com.amway.lms.backend.model;

import java.util.List;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public class QuestionModel {
    private String name;
    private String type;
    private String html;
    private String title;
    private boolean isRequired;
    private Boolean isAllRowRequired;
    private boolean hasOther;
    private String fileLocation;
    private String fileType;
    private List<ChoiceModel> choices;
    private List<ColumnModel> columns;
    private List<RowModel> rows;
    private List<ValidatorModel> validators;
    
    
    public QuestionModel(String name, String type, String html) {
        this.name = name;
        this.type = type;
        this.html = html;
    }
    public QuestionModel() {

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getHtml() {
        return html;
    }
    public void setHtml(String html) {
        this.html = html;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean getIsRequired() {
        return isRequired;
    }
    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }
    public boolean isHasOther() {
        return hasOther;
    }
    public void setHasOther(boolean hasOther) {
        this.hasOther = hasOther;
    }
    public List<ChoiceModel> getChoices() {
        return choices;
    }
    public void setChoices(List<ChoiceModel> choices) {
        this.choices = choices;
    }
    public List<ColumnModel> getColumns() {
        return columns;
    }
    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }
    public List<RowModel> getRows() {
        return rows;
    }
    public void setRows(List<RowModel> rows) {
        this.rows = rows;
    }
    public List<ValidatorModel> getValidators() {
        return validators;
    }
    public void setValidators(List<ValidatorModel> validators) {
        this.validators = validators;
    }
    public String getFileLocation() {
        return fileLocation;
    }
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public Boolean getIsAllRowRequired() {
        return isAllRowRequired;
    }
    public void setIsAllRowRequired(Boolean isAllRowRequired) {
        this.isAllRowRequired = isAllRowRequired;
    }
    
}
