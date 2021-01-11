package com.jsh.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jsh.security1.config.auth.PrincipalDetails;
import com.jsh.security1.model.User;
import com.jsh.security1.repository.UserRepository;

@Controller
public class IndexController {

	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	
	@GetMapping( "/test/login")
	public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
		System.out.println("/test/login");
		PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication :: " + details.getUser());
		System.out.println("userDetails : " + userDetails.getUser());
		return "세션정보 확인하기";
	}
	
	@GetMapping( "/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User auth2User) {
		System.out.println("/test/login");
		OAuth2User details = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication :: " + details.getAttributes());
		System.out.println("auth2User :: " + auth2User.getAttributes());
		return "오어스 세션정보 확인하기";
	}
	
	// 로컬 이동
	@GetMapping({ "", "/" })
	public String index() {
		// 머스테치 사용(jsp말고)
		return "index";
	}

	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails : " + principalDetails);
		return "user";
	}

	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}

	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}

	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}

	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}

	@PostMapping("/join")
	public String join(User user) {
		System.out.println("user :" + user);
		user.setRole("ROLE_USER");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		userRepository.save(user);//회원가입됨 비번1234로 => 시큐리티 로긴안됨 암호화안되서
		return "redirect:/loginForm";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터";
	}
}
