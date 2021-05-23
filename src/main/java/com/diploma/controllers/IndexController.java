package com.diploma.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.diploma.model.UserModel;

@Controller
public class IndexController {
	@GetMapping("/")
	public String getIndex() {
		return "index";
	}

	
	@GetMapping("/register")
	public String getRegisterForm(UserModel userModel) {
		return "register";
	}
	
	@GetMapping("/sign-in")
	public ModelAndView getSignInForm(@RequestParam(name="error",required=false)String error) {
		return new ModelAndView("signIn").addObject("error", error);
	}
	
	@GetMapping("/forgot-password")
	public String getForgotPasswordForm() {
		return "forgot-password";
	}

}
