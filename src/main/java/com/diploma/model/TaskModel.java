package com.diploma.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="task_table")
public class TaskModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long taskId;
	
	@NotBlank(message="task name cannot be empty")
	@Size(min=2,message="task name is too short, minimal length 2 symbols")
	private String name;
	
	@NotBlank(message="task description cannot be empty")
	@Size(min=5,message="task description is too short, minimal length 2 symbols")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="group_id")
	private GroupModel group;
	
	@ManyToOne
	@JoinColumn(name="executor_id")
	private UserModel executor;
	
	@NotBlank(message="task state cannot be blank")
	@Pattern(regexp="PENDING|DONE|STUCK")
	private String state;
	
	//add previous tasks
	
	private Date deadline;
	private String resultURL;

	public TaskModel() {}
	
	
	
	public TaskModel(String name, String description, GroupModel group, 
			UserModel executor, String state,
			Date deadline) {
		super();
		this.name = name;
		this.description = description;
		this.group = group;
		this.executor = executor;
		this.state = state;
		this.deadline = deadline;
	}

	

	public synchronized long getTaskId() {
		return taskId;
	}



	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public synchronized String getDescription() {
		return description;
	}

	public synchronized void setDescription(String description) {
		this.description = description;
	}

	public synchronized GroupModel getGroup() {
		return group;
	}

	public synchronized void setGroup(GroupModel group) {
		this.group = group;
	}

	public synchronized UserModel getExecutor() {
		return executor;
	}

	public synchronized void setExecutor(UserModel executor) {
		this.executor = executor;
	}

	public synchronized Date getDeadline() {
		return deadline;
	}

	public synchronized void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public synchronized String getState() {
		return state;
	}

	public synchronized void setState(String state) {
		this.state = state;
	}

	public synchronized String getResultURL() {
		return resultURL;
	}
	
	public synchronized void setResultURL(String resultURL) {
		this.resultURL = resultURL;
	}



	@Override
	public String toString() {
		return "TaskModel [taskId=" + taskId + ", name=" + name + ", description=" + description + ", group=" + group
				+ ", executor=" + executor + ", state=" + state + ", deadline=" + deadline + ", resultURL=" + resultURL
				+ "]";
	}	
	
}
