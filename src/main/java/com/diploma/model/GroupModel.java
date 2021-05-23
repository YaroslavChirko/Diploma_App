package com.diploma.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "group_table")
public class GroupModel {
	
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	private int groupId;
	
	@NotBlank(message="group name cannot be null")
	@Size(min=2,message="group name is too short, minimal length 2 symbols")
	private String name;
	
	@Size(max=1000, message="group description is too long, maximal length 1000 symbols")
	@Column(length = 1000)
	private String description;
	
	@OneToMany(mappedBy = "group",cascade=CascadeType.ALL)
	private Set<GroupUser> users = new HashSet<>();
	
	@NotNull(message="group language should be set")
	private LanguageEnum groupLanguage;
	
	@Column(unique = true)
	private String groupSerial;

	@OneToMany(mappedBy = "group",cascade=CascadeType.ALL)
	private List<MessageModel> messages= new ArrayList<>();
	
	@OneToMany(mappedBy="group", cascade=CascadeType.ALL)
	private List<TaskModel> tasks;
	
	public GroupModel() {}
	
	public GroupModel(String name, LanguageEnum groupLanguage, List<MessageModel> messages) {
		this.name = name;
		this.groupLanguage = groupLanguage;
		this.messages = messages;
		
	}
	
	public GroupModel(String name, Set<GroupUser> users, LanguageEnum groupLanguage, List<MessageModel> messages) {
		this.name = name;
		this.users = users;
		this.groupLanguage = groupLanguage;
		this.messages = messages;
	}


	public synchronized String getName() {
		return name;
	}


	public synchronized void setName(String name) {
		this.name = name;
	}


	public synchronized Set<GroupUser> getUsers() {
		return users;
	}


	public synchronized void setUsers(Set<GroupUser> users) {
		this.users = users;
	}


	public synchronized LanguageEnum getGroupLanguage() {
		return groupLanguage;
	}


	public synchronized void setGroupLanguage(LanguageEnum groupLanguage) {
		this.groupLanguage = groupLanguage;
	}


	public synchronized List<MessageModel> getMessages() {
		return messages;
	}


	public synchronized void setMessages(List<MessageModel> messages) {
		this.messages = messages;
	}

	public synchronized int getGroupId() {
		return groupId;
	}

	public synchronized void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public synchronized String getGroupSerial() {
		return groupSerial;
	}

	public synchronized void setGroupSerial(String groupSerial) {
		this.groupSerial = groupSerial;
	}
	
	public synchronized String getDescription() {
		return description;
	}

	public synchronized void setDescription(String description) {
		this.description = description;
	}

	public synchronized void addUser(UserModel user,String role,
			boolean requestRights,boolean taskRights,boolean groupRights, boolean isCreator) {
		GroupUser gu = new GroupUser(user,this,role,
				requestRights,taskRights,groupRights,isCreator);
		users.add(gu);
	}
	
	
	public synchronized void addMessage(MessageModel message) {
		message.setGroup(this);
		this.messages.add(message);
	}
	
	public synchronized void addTask(TaskModel task) {
		task.setGroup(this);
		this.tasks.add(task);
	}
	
	

	
}
