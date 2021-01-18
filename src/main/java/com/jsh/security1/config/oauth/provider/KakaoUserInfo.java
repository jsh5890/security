package com.jsh.security1.config.oauth.provider;

import java.util.HashMap;
import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes;
	
	@SuppressWarnings("unchecked")
	public KakaoUserInfo(Map<String, Object> attributes) {
		Map<String, Object> kakao_account  =(Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> properties  =(Map<String, Object>) attributes.get("properties");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", attributes.get("id"));
		params.put("nickname", properties.get("nickname"));
		params.put("email", kakao_account.get("email"));
		
		this.attributes = params;
	}
	
	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("id")); 
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		
		return String.valueOf(attributes.get("email"));
	}

	@Override
	public String getName() {
		return String.valueOf(attributes.get("nickname"));
	}

}
