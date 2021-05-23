package com.diploma.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.diploma.dao.GroupRepository;

@Component
public class PathAccessGuard {
	
	@Autowired
	GroupRepository groupRep;
	
	public boolean checkIsInGroup(Authentication authentication, String serial) {
		String email = authentication.getName();
		return groupRep.isUserInGroup(serial, email)==1;
	}
	
	public boolean checkCanAcceptRequests(Authentication authentication, String serial) {
		String email = authentication.getName();
		return groupRep.canUserAcceptRequests(serial, email);
	}
	
	public boolean checkCanModifyTasks(Authentication authentication, String serial) {
		String email = authentication.getName();
		return groupRep.canUserModifyTasks(serial, email);
	}
	
	public boolean checkCanAccessUserTasks(Authentication authentication, String serial, String pathEmail) {
		String email = authentication.getName();
		if(groupRep.isUserInGroup(serial, email)==1) {
		return email.contentEquals(pathEmail);
		}else {
			return false;
		}
	}
	
	public boolean checkCanModifyGroup(Authentication authentication, String serial) {
		String email = authentication.getName();
		return groupRep.canUserModifyGroup(serial, email);
	}
	
	public boolean checkCanModifyUserData(Authentication authentication, String pathEmail) {
		String email = authentication.getName();
		return pathEmail.equals(email);
	}
	
}
