package com.corpseed.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.corpseed.entity.User;
import com.corpseed.repository.UserRepository;


public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// fetching user from database 

		User user = userRepository.findByEmailAndDeleteStatus(username,2);

		if (user == null) {
			throw new UsernameNotFoundException("Could not found user !!");
		}else if(user.getAccountStatus()==2) {
			throw new UsernameNotFoundException("User inactive !!");
		}

		CustomUserDetails customUserDetails = new CustomUserDetails(user);

		return customUserDetails;
	}

}
