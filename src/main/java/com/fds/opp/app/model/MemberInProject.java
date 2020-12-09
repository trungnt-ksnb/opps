package com.fds.opp.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MemberInProject")
public class MemberInProject {
    @Id
    @Basic(optional = false)
    @Column(name = "idMemberShip")
    private Integer idMemberShip;
    @Column(name = "roles")
    private String roles;
    @Column(name = "idProject")
    private String nameProject;
    @Column(name = "idUser")
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
