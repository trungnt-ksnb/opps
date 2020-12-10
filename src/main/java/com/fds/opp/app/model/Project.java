package com.fds.opp.app.model;

import javax.persistence.*;

@Entity
@Table(name = "Project")
public class Project {
    @Id
    @Basic(optional = false)
    @Column(name = "idProject")
    private Integer idProject;
    @Basic(optional = false)
    @Column(name = "nameProject")
    private String nameProject;
    @Column(name = "descriptionProject")
    private String descriptionProject;

    public Integer getIdProject() {
        return idProject;
    }

    public void setIdProject(Integer idProject) {
        this.idProject = idProject;
    }

    public String getNameProject() {
        return nameProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public String getDescriptionProject() {
        return descriptionProject;
    }

    public void setDescriptionProject(String descriptionProject) {
        this.descriptionProject = descriptionProject;
    }
}
