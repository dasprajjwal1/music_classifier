package com.music.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.music.dao.UserRepositry;
import com.music.entities.User;


public class customUserDetailsServicesImpl implements UserDetailsService {
	@Autowired
	private UserRepositry userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userByUsername = this.userRepo.getUserByUsername(username);
		if (userByUsername == null) {
			throw new UsernameNotFoundException("could not found the user with username " + username);
		}
		customUserDetails customuserdetails = new customUserDetails(userByUsername);
		return customuserdetails;
	}

}
