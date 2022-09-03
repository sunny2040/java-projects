package com.surge.vms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surge.vms.model.UserDomain;

@Repository
public interface UserRepository extends JpaRepository<UserDomain, Long> {

	UserDomain findByUserId(String userid);

	UserDomain findByUserLn(String userLn);

	UserDomain findByUserFn(String userFN);

	UserDomain findByUserEmail(String userEmail);

	UserDomain findByUserPwd(String userPwd);

}