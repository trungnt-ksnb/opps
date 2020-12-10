package com.fds.opp.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fds.opp.app.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
