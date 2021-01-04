package com.jsh.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jsh.security1.model.User;
import com.jsh.security1.repository.UserRepository;

//시큐리티 설정에서 login프로세스에서 호출되면 아래 loadUserByUsername username이 호출됨

@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);
		if (user != null) {
			return new PrincipalDetails(user);
		}
		return null;
	}
}
