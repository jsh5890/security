package com.jsh.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	//로컬 이동
	@GetMapping({"","/"})
	public String index() {
		//머스테치 사용(jsp말고)
		return "index";
	}
}
