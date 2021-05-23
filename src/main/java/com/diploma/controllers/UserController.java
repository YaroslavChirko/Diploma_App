package com.diploma.controllers;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.diploma.dao.GroupRepository;
import com.diploma.dao.UserRepository;
import com.diploma.model.GroupModel;
import com.diploma.model.LanguageEnum;
import com.diploma.model.PasswordResetTokenModel;
import com.diploma.model.UserModel;
import com.diploma.service.MailService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
	@Autowired
	GroupRepository groupRep;

	@Autowired
	UserRepository userRep;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	JavaMailSenderImpl sender;
	
	
	
	@PostMapping("/register")
	private ModelAndView registerUser(@Valid @ModelAttribute("userModel")UserModel user,
			BindingResult result,
			HttpServletRequest request) {
			if(result.hasErrors()) {
				System.out.println(result.getModel());
				return new ModelAndView("/register",result.getModel());
			}
		
			if(userRep.existsUserModelByEmail(user.getEmail())) {
				return new ModelAndView("redirect:/register").addObject("error","e-mail taken");
			}
			String originalPass = user.getPass();
			user.setPass(new BCryptPasswordEncoder().encode(user.getPass()));
			
			userRep.save(user);
			session.setAttribute("email", user.getEmail());
			session.setAttribute("languages", user.getLanguages()); // don't forget to remove during logout
			try {
				request.login(user.getEmail(), originalPass);
			} catch (ServletException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/user/fetch-all-compatible-users").addObject("language",user.getLanguages());
	}
	
	@PostMapping("/sign-in")
	private ModelAndView signIn(
			@ModelAttribute(value ="email")String email,
			@ModelAttribute(value ="password")String pass) {
			UserModel user = userRep.getUserByEmail(email);
			if( new BCryptPasswordEncoder().matches(pass, user.getPass())) {
				session.setAttribute("email", email);
				session.setAttribute("languages", user.getLanguages());
				ModelAndView mav = new ModelAndView("redirect:/user/fetch-all-compatible-users");
				mav.addObject("language",user.getLanguages());
				return mav;			
			}
			return new ModelAndView("redirect:/sign-in").addObject("error","No such user found. Please register");
	}
	
	@GetMapping("/fetch-all-compatible-users")
	private ModelAndView fetchAllCompUsers(@RequestParam("language")Set<LanguageEnum> languages,
			GroupModel groupModel) {
		Set<Integer> userIdSet = new HashSet<>();

		for(LanguageEnum language: languages) {
			userIdSet.addAll(userRep.getUserIdByLanguage(language.name()));			
		}
		
		
		
		Set<UserModel> userSet = new HashSet<>();
		userSet.addAll((Collection<? extends UserModel>) userRep.findAllById(userIdSet));
		
		List<GroupModel> userGroups = userRep.getUserGroupsByEmail((String)session.getAttribute("email"));
		
		Set<GroupModel> otherGroups = new HashSet<>();
		for(LanguageEnum language: languages) {
			otherGroups.addAll(groupRep.getGroupsByGroupLanguage(language));
		}
		
		otherGroups.removeAll(userGroups);
		
		ModelAndView mav = new ModelAndView("allLang");
		mav.addObject("users", userSet);
		mav.addObject("languages",languages);
		mav.addObject("user_groups",userGroups);
		mav.addObject("other_groups",otherGroups);
		return mav;
	}
	
	@GetMapping("/{userEmail}/details")
	public ModelAndView showUserDetails(@PathVariable("userEmail")String email,
			@RequestParam(name="error_name",required=false)String nameError,
			@RequestParam(name="error_bio",required=false)String bioError) {
		UserModel user = userRep.getUserByEmail(email);
		ModelAndView mav = new ModelAndView("user-details");
		mav.addObject("user", user);
		mav.addObject("error_name",nameError);
		mav.addObject("error_bio",bioError);
		return mav;
	}
	
	@PostMapping("/{userEmail}/details/edit")
	public ModelAndView editUserDetails(@PathVariable("userEmail")String email,
			@ModelAttribute("name")String name,
			@ModelAttribute("info")String bio) {
		
		UserModel user = userRep.getUserByEmail(email);
		ModelAndView mav = new ModelAndView("redirect:/user/"+email+"/details");
		if(name!=null&&name.trim().length()>1) {
			user.setName(name);			
		}else {
			mav.addObject("error_name","name is too short or consists of white spaces");
		}
		if(bio.length()<1000) {
			user.setBio(bio);			
		}else {
			mav.addObject("error_bio", "bio is too long, use less than 1000 symbols");
		}
		userRep.save(user);
		
		
		return mav;
	}
	
	@PostMapping("/forgot-password")
	public boolean resetUserPassword(@RequestParam("email")String email,
			HttpServletRequest request) {
		//check if user is registered
		if(!userRep.existsUserModelByEmail(email)) {
			//if not return false to render exception but leave form
			return false;
		}
		
		//if is then create password reset code
		String resetCode = UUID.randomUUID().toString();
		
		//save it to user
		UserModel user = userRep.getUserByEmail(email);
		PasswordResetTokenModel token = new PasswordResetTokenModel(resetCode,user);
		System.out.println(token);
		user.setPasswordResetToken(token);
		userRep.save(user);
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		//create message with link and send it to email
		sender.send(MailService.composeMessage(email, resetCode, baseUrl));
		//return true which tells to render sucess message
		return true;
	}
	
	@GetMapping("/{resetCode}/{userEmail}")
	public ModelAndView showPasswordChangeForm(@PathVariable("resetCode")String resetCode,
			@PathVariable("userEmail") String email) {
		if(userRep.existsByEmailAndPasswordResetTokenValue(email, resetCode)) {
			Date now = new Date();
			Date expiry = userRep.getPasswordResetTokenExpiryDateByEmail(email);
			if(now.before(expiry)) {
				return new ModelAndView("password-change").addObject("userEmail",email);				
			}else {
				UserModel user = userRep.getUserByEmail(email);
				user.setPasswordResetToken(null);
				userRep.save(user);
				userRep.deleteTokenByUserId(user.getId());
			}
		}
		return new ModelAndView("redirect:/");
		
	}
	
	@PostMapping("/{email}/change-password")
	public ModelAndView changeUserPassword(@PathVariable("email") String email,
			@ModelAttribute("password")String password,
			HttpServletRequest request) {
		if(password.length()>5) {
			UserModel user = userRep.getUserByEmail(email);
			user.setPass(new BCryptPasswordEncoder().encode(password));
			user.setPasswordResetToken(null);
			userRep.save(user);
			userRep.deleteTokenByUserId(user.getId());
			try {
				request.login(email, password);
			} catch (ServletException e) {
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/user/fetch-all-compatible-users").addObject("language",user.getLanguages());
		}
		return new ModelAndView("/");//change to something else saying password is too short
		
	}
	
	
}
