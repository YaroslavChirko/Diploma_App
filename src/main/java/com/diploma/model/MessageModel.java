package com.diploma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name="message_table")
public class MessageModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(length = 1000)
	private String message;
	private String fromLogin;
	
	@ManyToOne
	@JoinColumn(name="group_id")
	private GroupModel group;
	
	private Date sentAt;
	
	private MessageType type;
	
	public enum MessageType{
		MESSAGE,
		REQUEST
	}
	
	
	public MessageModel() {}
	
	public MessageModel(String message, String fromLogin, Date sentAt) {
		this.fromLogin = fromLogin;
		this.message = message;
		this.sentAt = sentAt;
	}
	
	
	public synchronized String getMessage() {
		return message;
	}
	public synchronized void setMessage(String message) {
		this.message = message;
	}
	public synchronized String getFromLogin() {
		return fromLogin;
	}
	public synchronized void setFromLogin(String fromLogin) {
		this.fromLogin = fromLogin;
	}
	
	public synchronized GroupModel getGroup() {
		return group;
	}
	public synchronized void setGroup(GroupModel group) {
		this.group = group;
	}
	@Override
	public String toString() {
		return "MessageModel [message=" + message + ", fromLogin=" + fromLogin + "]";
	}
	
	@PrePersist
	private void setSentAt() {
		this.sentAt = new Date();
	}
	
	public Date getSentAt() {
		return this.sentAt;
	}

	public synchronized MessageType getType() {
		return type;
	}

	//as of now setting this field should be done this way
	public synchronized void setType(MessageType type) {
		this.type = type;
	}
	
	
	
}
