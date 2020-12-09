package com.fds.opp.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
