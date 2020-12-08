package com.fds.opp.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String startDate;
    @Column(name = "dueDate")
    private String dueDate;
    @Column(name = "deadlineDate")
    private String deadlineDate;
    @Column(name = "priorityWorkPackage")
    private String priorityWorkPackage;
    @Column(name = "statusWorkPackage")
    private String statusWorkPackage;
    @Column(name = "typeWorkPackage")
    private String typeWorkPackage;
    @Column(name = "idProject")
    private Integer idProject;
    @Column(name = "idUser")
    private Integer idUser;
    @Column (name="author")
    private String author;


}
