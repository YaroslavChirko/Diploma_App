package com.diploma.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PrePersist;
import javax.persistence.Table;


@Entity
@Table(name="group_user")
public class GroupUser {

	@EmbeddedId
	private GroupUserKey id = new GroupUserKey();
	
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private UserModel user;
	
	@ManyToOne
	@MapsId("groupId")
	@JoinColumn(name = "group_id")
	private GroupModel group;
	
	private String role;
	
	private Boolean hasAcceptRequestRights;
	
	private Boolean hasTaskModifyRights;
	
	private Boolean hasGroupModifyRights;
	
	private Boolean isGroupCreator;
	
	public GroupUser() {
		this.isGroupCreator = null;}

	public GroupUser( UserModel user, GroupModel group, String role, 
			Boolean hasAcceptRequestRights, Boolean hasTaskModifyRights,
			Boolean hasGroupModifyRights,Boolean isGroupCreator) {
		this.user = user;
		this.group = group;
		this.role = role;
		this.hasAcceptRequestRights=hasAcceptRequestRights;
		this.hasTaskModifyRights=hasTaskModifyRights;
		this.hasGroupModifyRights=hasGroupModifyRights;
		this.isGroupCreator = isGroupCreator;
	}

	public GroupUserKey getId() {
		return id;
	}

	public void setId(GroupUserKey id) {
		this.id = id;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public GroupModel getGroup() {
		return group;
	}

	public void setGroup(GroupModel group) {
		this.group = group;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public synchronized Boolean isHasAcceptRequestRights() {
		return hasAcceptRequestRights;
	}

	public synchronized void setHasAcceptRequestRights(Boolean hasAcceptRequestRights) {
		this.hasAcceptRequestRights = hasAcceptRequestRights;
	}

	public synchronized Boolean isHasTaskModifyRights() {
		return hasTaskModifyRights;
	}

	public synchronized void setHasTaskModifyRights(Boolean hasTaskModifyRights) {
		this.hasTaskModifyRights = hasTaskModifyRights;
	}

	
	public synchronized Boolean getHasGroupModifyRights() {
		return hasGroupModifyRights;
	}

	public synchronized void setHasGroupModifyRights(Boolean hasGroupModifyRights) {
		this.hasGroupModifyRights = hasGroupModifyRights;
	}
	
	

	public synchronized Boolean getIsGroupCreator() {
		return isGroupCreator;
	}

	public synchronized void setIsGroupCreator(Boolean isGroupCreator) {
		this.isGroupCreator = isGroupCreator;
	}

	@Override
	public String toString() {
		return "GroupUser [id=" + id + ", user=" + user + ", group=" + group + ", role=" + role + "]";
	}

	@PrePersist
	public void prePersist() {
		this.id.setgroupid(group.getGroupId());
		this.id.setuserid(user.getId());
	}
	
}
