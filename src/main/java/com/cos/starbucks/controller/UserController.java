package com.cos.starbucks.controller;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.cos.starbucks.model.User;
import com.cos.starbucks.repository.UserRepository;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository URepo;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	
	@GetMapping("/join1")
	public String join1() {
		return "auth/join1";
	}
	
	@GetMapping("/join2")
	public String join2() {
		return "auth/join2";
	}
	
	@PostMapping("/joinProc")
	public String create(User user) {
		String rawPassword = user.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		URepo.join(user);
		return "redirect:/user/login";
	}
	
	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}
	
	
}
