/**
 * 
 */
package com.amway.lms.backend.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="survey_types")
public class SurveyType implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Column(name = "type_desc")
    private String typeDesc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
