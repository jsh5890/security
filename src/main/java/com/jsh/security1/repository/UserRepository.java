package com.jsh.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsh.security1.model.User;

//crud 함수를 jparepository가  가지고있음
public interface UserRepository extends JpaRepository<User, Integer> {

	// select * from user where username = ?
	public User findByUsername(String username);

}
