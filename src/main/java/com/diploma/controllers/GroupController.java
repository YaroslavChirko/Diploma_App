package com.diploma.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.diploma.dao.GroupRepository;
import com.diploma.dao.MessageRepository;
import com.diploma.dao.UserRepository;
import com.diploma.model.GroupModel;
import com.diploma.model.GroupUser;
import com.diploma.model.MessageModel;
import com.diploma.model.MessageModel.MessageType;
import com.diploma.model.UserModel;

@RestController
@RequestMapping("/group")
public class GroupController {
	@Autowired
	GroupRepository groupRep;
	@Autowired
	HttpSession session;
	@Autowired
	UserRepository userRep;
	@Autowired
	MessageRepository messageRep;
	
	@PostMapping("/register")
	private ModelAndView registerGroup(@Valid @ModelAttribute("groupModel") GroupModel group,
			BindingResult result,
			@SessionAttribute("email") String email,
			@SessionAttribute("languages")List<String> languages) {
		if(result.hasErrors()) {
			ModelAndView mav = new ModelAndView("redirect:/user/fetch-all-compatible-users"
					,result.getModel());
			mav.addObject("language",languages);
			return mav;
		}
		UserModel user = userRep.getUserByEmail(email);
		group.setGroupSerial(group.getName()+(new Random()).nextLong());
		group.setMessages(new ArrayList<MessageModel>());
		group.addUser(user, "CREATOR",true,true,true,true);
		groupRep.save(group);
		return new ModelAndView("redirect:/group/"+group.getGroupSerial()+"/main");
	}
	
	@GetMapping("/{groupSerial}/main")
	private ModelAndView getGroupMainView(@PathVariable("groupSerial") String serial,
			@SessionAttribute("email")String email) {
		List<GroupUser> users = groupRep.getGroupUsers(serial);
		List<MessageModel> messages = messageRep.getMessagesByGroupGroupSerialAndTypeOrderBySentAtAsc(serial, MessageType.MESSAGE);
		GroupUser user = groupRep.getUserFromGroup(serial, email);
		ModelAndView mav = new ModelAndView("group");
		mav.addObject("serial", serial);
		mav.addObject("messages",messages);
		mav.addObject("requestRigths",user.isHasAcceptRequestRights());
		mav.addObject("groupRights", user.getHasGroupModifyRights());
		mav.addObject("taskRights", user.isHasTaskModifyRights());
		return mav.addObject("users",users);
		
	}
	
	@PostMapping("/{groupSerial}/addUser")
	private void addUserToGroup(@ModelAttribute("email")String email,
			@ModelAttribute("role")String role,
			@RequestParam(name="request-rights",defaultValue ="false")Boolean requestAcceptRights,
			@RequestParam(name="task-rights",defaultValue ="false")Boolean taskModifyRights,
			@RequestParam(name="group-rights",defaultValue ="false")Boolean groupModifyRights,
			@PathVariable("groupSerial")String serial) {
		if(groupRep.isUserInGroup(serial, email)==0) {
			GroupModel group = groupRep.getGroupByGroupSerial(serial);
			group.addUser(userRep.getUserByEmail(email), role.toUpperCase(),
				requestAcceptRights,taskModifyRights,groupModifyRights,false);
			groupRep.save(group);
		}
		
	}
	
	@GetMapping("/{groupSerial}/add-user-form")
	private ModelAndView showAddUserForm(@PathVariable("groupSerial") String serial) {
		List<Integer> userIds =userRep.getUserIdByLanguage(groupRep.getGroupByGroupSerial(serial).getGroupLanguage().name()); 
		List<UserModel> users = new ArrayList<>();
			users = (List<UserModel>) userRep.findAllById(userIds);
		List<Integer> groupUserIds = groupRep.getGroupUserIds(serial);
		users.removeAll((List<UserModel>) userRep.findAllById(groupUserIds));
		ModelAndView mav = new ModelAndView("show-available-users");
		mav.addObject("users", users);
		mav.addObject("serial", serial);
		return mav;
	}
	
	@GetMapping("/{groupSerial}/requests")
	private ModelAndView getGroupRequestsView(@PathVariable("groupSerial") String serial) {
		List<MessageModel> requests = messageRep.getMessagesByGroupGroupSerialAndTypeOrderBySentAtAsc(serial, MessageType.REQUEST);
		ModelAndView mav = new ModelAndView("requests");
		mav.addObject("serial", serial);
		mav.addObject("requests",requests);
		return mav;
		
	}
	
	@GetMapping(value = "/{groupSerial}/requests/process", params="accept")
	private ModelAndView acceptGroupRequestsView(@PathVariable("groupSerial") String serial,
			@ModelAttribute("user_login")String userLogin,
			@ModelAttribute("role") String role,
			@RequestParam(name="request-rights",defaultValue ="false")Boolean requestAcceptRights,
			@RequestParam(name="task-rights",defaultValue ="false")Boolean taskModifyRights,
			@RequestParam(name="group-rights",defaultValue ="false")Boolean groupModifyRights) {
		if(groupRep.isUserInGroup(serial, userLogin)==0) {			
			GroupModel group = groupRep.getGroupByGroupSerial(serial);
			
			group.addUser(userRep.getUserByEmail(userLogin), role,
					requestAcceptRights,taskModifyRights,groupModifyRights,false);
			messageRep.deleteMessagesByGroupGroupSerialAndTypeAndFromLogin(serial, MessageType.REQUEST, userLogin);
			groupRep.save(group);
			return new ModelAndView("redirect:");
		}else {
			ModelAndView mav = new ModelAndView("redirect:process").addObject("user_login",userLogin);
			mav.addObject("deny","deny");
			return mav;
		}
		
	}
	
	@GetMapping(value = "/{groupSerial}/requests/process", params="deny")
	private ModelAndView denyGroupRequestsView(@PathVariable("groupSerial") String serial,@ModelAttribute("user_login")String userLogin) {
		GroupModel group = groupRep.getGroupByGroupSerial(serial);

		messageRep.deleteMessagesByGroupGroupSerialAndTypeAndFromLogin(serial, MessageType.REQUEST, userLogin);
		
		groupRep.save(group);
		return new ModelAndView("redirect:/requests");
	}

	@GetMapping("/{groupSerial}/details")
	private ModelAndView getGroupDetailsForm(@PathVariable("groupSerial") String serial,
			@RequestParam(name="error_name",required = false)String nameError,
			@RequestParam(name="error_description", required=false)String descriptionError) {
		GroupModel group = groupRep.getGroupByGroupSerial(serial);
		ModelAndView mav = new ModelAndView("group_details");
		mav.addObject("group", group);
		mav.addObject("error_name" ,nameError);
		mav.addObject("error_description",descriptionError);
		return mav;
		
	}
	
	@PostMapping("/{groupSerial}/details/edit")
	private ModelAndView editGroupDetails(@PathVariable("groupSerial") String serial,
			@ModelAttribute("name") String name,
			@ModelAttribute("info") String description) {
		ModelAndView mav = new ModelAndView("redirect:/group/"+serial+"/details");
		GroupModel group = groupRep.getGroupByGroupSerial(serial);
		if(name!=null&&name.trim().length()>1) {
			group.setName(name);
		}else {
			mav.addObject("error_name","name is too short or consists of white spaces");
		}
		
		if(description.length()<1000) {
			group.setDescription(description);			
		}else {
			mav.addObject("error_description","name is too short or consists of white space");
		}
		
		groupRep.save(group);
		
		return mav;
		
	}
	
	@PostMapping("/{groupSerial}/manage-rights/{user_id}")
	private boolean manageUserRights(@PathVariable("groupSerial") String serial,
			@PathVariable("user_id") int userId,
			@ModelAttribute("requestRights") boolean requestRights,
			@ModelAttribute("groupRights") boolean groupRights,
			@ModelAttribute("taskRights") boolean taskRights) {
		GroupUser user = groupRep.getUserFromGroup(serial, userId);
		if(user==null||user.getIsGroupCreator()) {
			return false;
		}
		GroupModel group = groupRep.getGroupByGroupSerial(serial);
		Set<GroupUser> users = group.getUsers();
		users.remove(user);
		user.setHasAcceptRequestRights(requestRights);
		user.setHasGroupModifyRights(groupRights);
		user.setHasTaskModifyRights(taskRights);
		users.add(user);
		groupRep.save(group);
		return true;
		
	}
	
}
