package com.music.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import com.music.entities.User;

public interface UserRepositry extends JpaRepository<User, Integer> {
	@Query("select user from User user where user.email = :email")
	public User getUserByUsername(@RequestParam("email") String email);
	
}
