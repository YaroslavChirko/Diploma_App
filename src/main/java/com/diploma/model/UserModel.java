package com.diploma.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Table(name = "user_table")
public class UserModel {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotBlank(message = "username cannot be empty")
	
	@Size(min=2,message="name too short")
	private String name;
	
	@Column(unique = true)
	@NotBlank(message = "email cannot be null")
	private String email;
	
	@NotBlank(message="password cannot be empty")
	@Size(min=6,message="password is too short, minimal size 6 symbols")
	private String pass;
	
	@Size(max=1000,message="bio is too long, maximal size is 1000 symbols")
	@Column(length = 1000)
	private String bio;
	
	@OneToMany(mappedBy="executor", cascade=CascadeType.ALL)
	private List<TaskModel> tasks;
	
	@ElementCollection(targetClass = LanguageEnum.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "user_table_languages",
	            joinColumns =  @JoinColumn(name = "user_id", referencedColumnName = "id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "languages")
	@NotNull(message="at least one language should be selected")
	private Set<LanguageEnum> languages;
	
	@OneToMany(mappedBy = "user",fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	private Set<GroupUser> groups = new HashSet<GroupUser>();
	
	@OneToOne(targetEntity=PasswordResetTokenModel.class,cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name="token_id",referencedColumnName = "id")
	private PasswordResetTokenModel passwordResetToken;
	
	public UserModel(){}

	
	public UserModel(String name, String email, String pass, String bio,
			Set<LanguageEnum> languages) {
		this.name = name;
		this.email = email;
		this.pass = pass;
		this.bio = bio;
		this.languages = languages;
	}

	
	public UserModel(String name, String email, String pass, String bio, Set<LanguageEnum> languages,
			Set<GroupUser> groups) {
		this.name = name;
		this.email = email;
		this.pass = pass;
		this.bio = bio;
		this.languages = languages;
		this.groups = groups;
	}



	public synchronized int getId() {
		return id;
	}

	public synchronized void setId(int id) {
		this.id = id;
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		if(name != null && !name.isEmpty()) {
			this.name = name;			
		}else {
			throw new IllegalArgumentException("empty name");
		}
	}

	public synchronized String getEmail() {
		return email;
	}

	public synchronized void setEmail(String email) {
		if(email != null && !email.isEmpty()) {  //the check for format will be conducted on the client, though should add some checks here too
			//check for proper formatting and query DB for this entry
			this.email = email;			
		}else {
			throw new IllegalArgumentException("empty email");
		}
	}

	public synchronized String getPass() {
		return pass;
	}

	public synchronized void setPass(String pass) {
		if(pass !=null && !pass.isEmpty()) {
			this.pass = pass;			
		}else {
			throw new IllegalArgumentException("empty pass");
		}
	}

	public synchronized String getBio() {
		return bio;
	}

	

	public synchronized void setBio(String bio) {
		this.bio = bio;
	}

	public synchronized Set<LanguageEnum> getLanguages() {
		return languages;
	}

	public synchronized void setLanguages(Set<LanguageEnum> languages) {
		if(languages.size()>0) {
			this.languages = languages;			
		}else {
			throw new IllegalArgumentException("empty list of languages");
		}
	}
	
	

	public synchronized Set<GroupUser> getGroups() {
		return groups;
	}



	public synchronized void setGroups(Set<GroupUser> groups) {
		this.groups = groups;
	}


	public synchronized void addTask(TaskModel task) {
		task.setExecutor(this);
		this.tasks.add(task);
	}

	
	
	public synchronized PasswordResetTokenModel getPasswordResetToken() {
		return passwordResetToken;
	}


	public synchronized void setPasswordResetToken(PasswordResetTokenModel passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}

	
	@Override
	public String toString() {
		return "UserModel [id=" + id + ", name=" + name + ", email=" + email + ", pass=" + pass + ", bio=" + bio
				+ ", languages=" + languages + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
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
		UserModel other = (UserModel) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pass == null) {
			if (other.pass != null)
				return false;
		} else if (!pass.equals(other.pass))
			return false;
		return true;
	}
	
}
