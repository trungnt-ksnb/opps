package com.fds.opp.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkPackage extends AccountAudit{
    @Id
    @Basic(optional = false)
    @Column(name = "idWorkPackage")
    private Integer idWorkPackage;
    @Basic(optional = false)
    @Column(name = "nameWorkPackage")
    private String nameWorkPackage;
    @Column(name = "descriptionWorkPackage")
    private String descriptionWorkPackage;
    @Column(name = "startDate")
    private Date startDate;
    @Column(name = "dueDate")
    private Date dueDate;
    @Column(name = "deadlineDate")
    private Date deadlineDate;
    @Column(name = "priorityWorkPackage")
    private String priorityWorkPackage;
    @Column(name = "statusWorkPackage")
    private String statusWorkPackage;
    @Column(name = "typeWorkPackage")
    private String typeWorkPackage;
    @Column(name = "nameProject")
    private String nameProject;
    @Column(name = "nameUser")
    private String nameUser;
    @Column (name="author")
    private String author;

    public void setIdWorkPackage(Integer idWorkPackage) {
        this.idWorkPackage = idWorkPackage;
    }

    public void setNameWorkPackage(String nameWorkPackage) {
        this.nameWorkPackage = nameWorkPackage;
    }

    public void setDescriptionWorkPackage(String descriptionWorkPackage) {
        this.descriptionWorkPackage = descriptionWorkPackage;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public void setPriorityWorkPackage(String priorityWorkPackage) {
        this.priorityWorkPackage = priorityWorkPackage;
    }

    public void setStatusWorkPackage(String statusWorkPackage) {
        this.statusWorkPackage = statusWorkPackage;
    }

    public void setTypeWorkPackage(String typeWorkPackage) {
        this.typeWorkPackage = typeWorkPackage;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
