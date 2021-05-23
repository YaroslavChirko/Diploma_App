package com.diploma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.diploma.service.UserDetailServiceImplementation;

@Configuration
@EnableWebSecurity
@ComponentScan("com.diploma.service")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	
	@Autowired
	UserDetailServiceImplementation userDetailsService;


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.formLogin()
		.loginPage("/sign-in")
		.successForwardUrl("/user/sign-in")
		.loginProcessingUrl("/user/sign-in")
		.failureUrl("/sign-in?error=no such user")
		.usernameParameter("email")
		.permitAll()
		.and()
		.logout()
		.invalidateHttpSession(true)
		.permitAll();
		
		http.headers().frameOptions().sameOrigin();
		
		http
		.authorizeRequests()
		.antMatchers("/sign-in","/register","/","/user/sign-in","/user/register",
				"/user/{resetCode}/{userEmail}","/user/{email}/change-password",
				"/user/forgot-password","/forgot-password").permitAll()
		.antMatchers("/group/register","/user/fetch-all-compatible-users","/logout","/group/{groupSerial}/tasks/complete-task").authenticated()
		
		.antMatchers("/user/{userEmail}/details",
				"/user/{userEmail}/details/edit")
		.access("@pathAccessGuard.checkCanModifyUserData(authentication,#userEmail)")

		.antMatchers("/group/{groupSerial}/tasks/add-task",
				"/group/{groupSerial}/tasks/create-task",
				"/group/{groupSerial}/tasks/all-tasks")
		.access("@pathAccessGuard.checkCanModifyTasks(authentication,#groupSerial)")
		
		.antMatchers("/group/{groupSerial}/tasks/{userEmail}")
		.access("@pathAccessGuard.checkCanAccessUserTasks(authentication,#groupSerial,#userEmail)")
		
		.antMatchers("/group/{groupSerial}/addUser",
				"/group/{groupSerial}/add-user-form","/group/{groupSerial}/requests",
				"/group/{groupSerial}/requests/process",
				"/{groupSerial}/manage-rights/{user_id}")
		.access("@pathAccessGuard.checkCanAcceptRequests(authentication,#groupSerial)")
		
		.antMatchers("/group/{groupSerial}/details","/group/{groupSerial}/details/edit")
		.access("@pathAccessGuard.checkCanModifyGroup(authentication,#groupSerial)")
		
		.antMatchers("/group/{groupSerial}/**")
		.access("@pathAccessGuard.checkIsInGroup(authentication,#groupSerial)");
		
	}
	
	
	
}
