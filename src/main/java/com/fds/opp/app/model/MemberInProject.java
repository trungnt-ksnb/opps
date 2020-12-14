package com.fds.opp.app.model;

import javax.persistence.*;

@Entity
@Table(name = "MemberInProject")
public class MemberInProject {
    @Id
    @Basic(optional = false)
    @Column(name = "idMemberShip")
    private Integer idMemberShip;
    @Column(name = "roles")
    private String roles;
    @Column(name = "nameProject")
    private String nameProject;
    @Column(name = "nameUser")
    private String nameUser;


    public Integer getIdMemberShip() {
        return idMemberShip;
    }

    public void setIdMemberShip(Integer idMemberShip) {
        this.idMemberShip = idMemberShip;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
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
}
