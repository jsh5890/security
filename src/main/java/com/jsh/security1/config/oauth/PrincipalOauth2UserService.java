package com.jsh.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jsh.security1.config.auth.PrincipalDetails;
import com.jsh.security1.model.User;
import com.jsh.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("oauth 계정정보  : " + super.loadUser(userRequest).getAttributes());
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		System.out.println("getat : " + oAuth2User.getAttributes());
		
		String provider = userRequest.getClientRegistration().getClientId();
		System.out.println("provider : " + provider);
		String providerId = oAuth2User.getAttribute("sub");
		System.out.println("providerId : " + providerId);
		String username = provider + "_" + providerId;
		System.out.println("username : " + username);
		String password = bCryptPasswordEncoder.encode("겟인코더");
		System.out.println("password : " + password);
		String email = oAuth2User.getAttribute("email");
		System.out.println("email : " + email);
		String role = "ROLE_USER";
		System.out.println("role : " + role);
		
 		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username).password(password).email(email).role(role).provider(providerId).providerId(providerId).build();
			userRepository.save(userEntity);
		} 
		
		return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
	}
}
