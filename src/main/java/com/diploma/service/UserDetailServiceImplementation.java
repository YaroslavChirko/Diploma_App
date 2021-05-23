package com.diploma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.diploma.dao.UserRepository;
import com.diploma.model.UserModel;

@Service
public class UserDetailServiceImplementation implements UserDetailsService {

	@Autowired
	UserRepository userRep;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserModel user = userRep.getUserByEmail(email);
		if(user==null) {
			throw new UsernameNotFoundException("no such user registered. /n Use Register page");
		}
		return new UserDetailsWrapper(user);
	}

}
