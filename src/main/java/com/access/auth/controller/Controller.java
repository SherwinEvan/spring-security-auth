package com.access.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.access.auth.entities.User;
import com.access.auth.event.RegistrationCompleteEvent;
import com.access.auth.models.UserModel;
import com.access.auth.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class Controller {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping("/test")
	public String test() {
		return "Hello! This is a test.";
	}
	
	@PostMapping("/signup")
	public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request  ) {
		User user = userService.registerUser(userModel);
		
		publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
		
		return "Success";
	}
	
	@GetMapping("/verifyRegistration")
	public String verifyRegistration(@RequestParam("token") String token) {
		boolean validity = userService.validateVerificationToken(token);
		
		if(validity) {
			return "User verified successfully!";
		}
		
		return "Bad token request";
	}

	private String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" 
					+ request.getServerPort() + request.getContextPath();
	}
}
