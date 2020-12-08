package com.fds.opp.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MemberInProject extends AccountAudit {
    @Id
    @Basic(optional = false)
    @Column(name = "idMemberShip")
    private Integer idMemberShip;
    @Column(name = "roles")
    private String roles;
    @Column(name = "idProject")
    private Integer idProject;
    @Column(name = "idUser")
    private Integer idUser;


}
