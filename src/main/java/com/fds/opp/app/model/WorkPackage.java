package com.fds.opp.app.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WorkPackage")
public class WorkPackage {
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

    public Integer getIdWorkPackage() {
        return idWorkPackage;
    }

    public void setIdWorkPackage(Integer idWorkPackage) {
        this.idWorkPackage = idWorkPackage;
    }

    public String getNameWorkPackage() {
        return nameWorkPackage;
    }

    public void setNameWorkPackage(String nameWorkPackage) {
        this.nameWorkPackage = nameWorkPackage;
    }

    public String getDescriptionWorkPackage() {
        return descriptionWorkPackage;
    }

    public void setDescriptionWorkPackage(String descriptionWorkPackage) {
        this.descriptionWorkPackage = descriptionWorkPackage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getPriorityWorkPackage() {
        return priorityWorkPackage;
    }

    public void setPriorityWorkPackage(String priorityWorkPackage) {
        this.priorityWorkPackage = priorityWorkPackage;
    }

    public String getStatusWorkPackage() {
        return statusWorkPackage;
    }

    public void setStatusWorkPackage(String statusWorkPackage) {
        this.statusWorkPackage = statusWorkPackage;
    }

    public String getTypeWorkPackage() {
        return typeWorkPackage;
    }

    public void setTypeWorkPackage(String typeWorkPackage) {
        this.typeWorkPackage = typeWorkPackage;
    }

    public String getNameProject() {
        return nameProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
