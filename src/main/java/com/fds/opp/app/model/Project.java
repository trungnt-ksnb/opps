package com.fds.opp.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Project extends AccountAudit{
    @Id
    @Basic(optional = false)
    @Column(name = "idProject")
    private Integer idProject;
    @Basic(optional = false)
    @Column(name = "nameProject")
    private String nameProject;
    @Column(name = "descriptionProject")
    private String descriptionProject;

    public void setIdProject(Integer idProject) {
        this.idProject = idProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public void setDescriptionProject(String descriptionProject) {
        this.descriptionProject = descriptionProject;
    }
}
