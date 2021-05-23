package com.diploma.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.diploma.dao.GroupRepository;
import com.diploma.dao.TaskRepository;
import com.diploma.dao.UserRepository;
import com.diploma.model.GroupModel;
import com.diploma.model.GroupUser;
import com.diploma.model.TaskModel;
import com.diploma.model.UserModel;

@RestController
@RequestMapping("group/{groupSerial}/tasks")
public class TaskController {
	@Autowired
	private TaskRepository taskRep;
	
	@Autowired
	private GroupRepository groupRep;
	@Autowired
	private UserRepository userRep;
	
	@GetMapping("/{userEmail}")
	public ModelAndView showUserTasks(@PathVariable("userEmail") String email, 
			@PathVariable("groupSerial") String groupSerial) {
		
		ModelAndView mav = new ModelAndView("task");
		GroupUser user =  groupRep.getUserFromGroup(groupSerial,email);

		List<TaskModel> userTasksP = taskRep.getTasksByExecutorEmailAndGroupGroupSerialAndStateOrderByDeadlineAsc(email, groupSerial, "PENDING");
		mav.addObject("pending_tasks", userTasksP);
		List<TaskModel> userTasksD = taskRep.getTasksByExecutorEmailAndGroupGroupSerialAndStateOrderByDeadlineAsc(email, groupSerial, "DONE");
		mav.addObject("done_tasks", userTasksD);
		if(user.isHasTaskModifyRights()) {
			mav.addObject("users",user);
			mav.addObject("taskRights", groupRep.canUserAcceptRequests(groupSerial, email));
		}
		
		return mav;
	}
	
	@GetMapping("/all-tasks")
	public ModelAndView showAllTasks(@PathVariable("groupSerial") String groupSerial,
			@SessionAttribute("email")String email) {
		List<GroupUser> users = groupRep.getGroupUsers(groupSerial);
		ModelAndView mav = new ModelAndView("task");
		
		List<TaskModel> userTasksP = taskRep.getTasksByGroupGroupSerialAndStateOrderByDeadlineAsc( groupSerial, "PENDING");
		mav.addObject("pending_tasks", userTasksP);
		List<TaskModel> userTasksD = taskRep.getTasksByGroupGroupSerialAndStateOrderByDeadlineAsc( groupSerial, "DONE");
		mav.addObject("done_tasks", userTasksD);
		mav.addObject("taskRights", groupRep.canUserAcceptRequests(groupSerial, email));

		mav.addObject("users", users);
		mav.addObject("serial", groupSerial);
		return mav;
	}
	
	@PostMapping("/add-task")
	public ModelAndView addTask(@ModelAttribute("task-name") String name,
			@ModelAttribute("task-description") String description,
			@ModelAttribute("executor-email") String email,
			@ModelAttribute("deadline_date") String deadlineDateString,
			@ModelAttribute("deadline_time") String deadlineTimeString,
			@PathVariable("groupSerial") String groupSerial) {
		GroupModel group = groupRep.getGroupByGroupSerial(groupSerial);
		UserModel user = userRep.getUserByEmail(email);
		Date deadline = new Date();
		
		try {
			deadline = new SimpleDateFormat("yyyy-MM-dd' 'hh:mm").parse(deadlineDateString+" "+deadlineTimeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		TaskModel task = new TaskModel(name,description,group,user,
				"PENDING", deadline);
		group.addTask(task);
		groupRep.save(group);
		ModelAndView mav = new ModelAndView("redirect:all-tasks");
		return mav;
	}
	
	
	@PostMapping("/complete-task")
	public ModelAndView completeUserTask(@PathVariable("groupSerial") String groupSerial,
			@ModelAttribute("taskID")Long taskId,
			@ModelAttribute("resultURL")String resultURL,
			@ModelAttribute("executorEmail")String executorEmail,
			@SessionAttribute("email")String email) {
		if(!email.equals(executorEmail)) {
			System.out.println("wrong user");
			return new ModelAndView("redirect:"+email);
		}
		TaskModel task = taskRep.findById(taskId).get();
		task.setResultURL(resultURL);
		task.setState("DONE");
		taskRep.save(task);
		ModelAndView mav = new ModelAndView("redirect:"+email);
		return mav;
	}

}
