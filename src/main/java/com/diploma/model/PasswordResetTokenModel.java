package com.diploma.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="password_reset_tokens")
public class PasswordResetTokenModel {
	@Transient
	private static final long EXPIRY = 2*60;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String value;
	
	@OneToOne(targetEntity=UserModel.class,fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
	private UserModel user;
	
	private Date expiryDate;
	
	public PasswordResetTokenModel() {}
	
	public PasswordResetTokenModel(String value, UserModel user) {
		this.value = value;
		this.user = user;
	}

	

	public synchronized int getId() {
		return id;
	}

	public synchronized void setId(int id) {
		this.id = id;
	}

	public synchronized String getValue() {
		return value;
	}

	public synchronized void setValue(String value) {
		this.value = value;
	}

	public synchronized UserModel getUser() {
		return user;
	}

	public synchronized void setUser(UserModel user) {
		this.user = user;
	}

	public synchronized Date getExpiryDate() {
		return expiryDate;
	}

	@PrePersist
	private synchronized void setExpiryDate() {
		this.expiryDate = Date.from(LocalDateTime.now().plusMinutes(PasswordResetTokenModel.getEXPIRY()).atZone(ZoneId.systemDefault()).toInstant());
	}

	public static synchronized long getEXPIRY() {
		return EXPIRY;
	}

	@Override
	public String toString() {
		return "PasswordResetTokenModel [id=" + id + ", value=" + value + ", user=" + user + ", expiryDate="
				+ expiryDate + "]";
	}
	
	
	
	
}
