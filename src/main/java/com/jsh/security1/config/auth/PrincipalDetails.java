package com.jsh.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jsh.security1.model.User;

//로긴진행이되면 시큐리티 세션만들어준다 (Security ContextHolder)
// 오브젝트타입은  Authentication 타입객체
// Authentication 안에 user정보가 있어야한다
// User 오브젝트객체 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails

public class PrincipalDetails implements UserDetails{

	private User user; //콤포지션
	
	public PrincipalDetails(User user) {
		this.user = user;
	}

	//해당유저의 권한을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		//사이트에서 1년동안 로긴안하면 휴먼계정
		return true;
	}

}
