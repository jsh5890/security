package com.jsh.security1.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jsh.security1.config.auth.PrincipalDetails;
import com.jsh.security1.config.oauth.provider.GoogleUserInfo;
import com.jsh.security1.config.oauth.provider.KakaoUserInfo;
import com.jsh.security1.config.oauth.provider.NaverUserInfo;
import com.jsh.security1.config.oauth.provider.OAuth2UserInfo;
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
		System.out.println("oauth id  : " + userRequest.getClientRegistration().getRegistrationId());
		OAuth2UserInfo auth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글요청");
			auth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
			System.out.println("네이버요청");
			auth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
			System.out.println("카카오요청");
			auth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		}
		
		String provider = auth2UserInfo.getProvider();
		String providerId = auth2UserInfo.getProviderId();
		String username = provider + "_" + providerId;
		String password = bCryptPasswordEncoder.encode("겟인코더");
		String email = auth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
 		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username).password(password).email(email).role(role).provider(provider).providerId(providerId).build();
			userRepository.save(userEntity);
		} 
		
		return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
	}
}
