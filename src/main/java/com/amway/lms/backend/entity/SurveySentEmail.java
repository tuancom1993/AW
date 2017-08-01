package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the survey_sent_emails database table.
 * 
 */
@Entity
@Table(name = "survey_sent_emails")
@NamedQueries({
    @NamedQuery(name = "getSurveySentEmailList", query = "SELECT s FROM SurveySentEmail s"),
    @NamedQuery(name = "getSurveySentEmailListBySurveySentId", query = "SELECT s FROM SurveySentEmail s WHERE s.surveySentId = ?")
})

public class SurveySentEmail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String email;

    @Column(name = "survey_sent_id")
    private int surveySentId;

    public SurveySentEmail() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSurveySentId() {
        return this.surveySentId;
    }

    public void setSurveySentId(int surveySentId) {
        this.surveySentId = surveySentId;
    }

}