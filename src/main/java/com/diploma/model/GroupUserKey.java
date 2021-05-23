package com.diploma.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GroupUserKey implements Serializable {
	private static final long serialVersionUID = 7179848084218135761L;
	@Column(name = "user_id")
	private int userid;
	@Column(name = "group_id")
	private int groupid;
	
	public GroupUserKey() {}
	
	public GroupUserKey(int userid, int groupid) {
		this.userid = userid;
		this.groupid = groupid;
	}
	public int getuserid() {
		return userid;
	}
	public void setuserid(int userid) {
		this.userid = userid;
	}
	public int getgroupid() {
		return groupid;
	}
	public void setgroupid(int groupid) {
		this.groupid = groupid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + groupid;
		result = prime * result + userid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupUserKey other = (GroupUserKey) obj;
		if (groupid != other.groupid)
			return false;
		if (userid != other.userid)
			return false;
		return true;
	}
	
	
	
}
