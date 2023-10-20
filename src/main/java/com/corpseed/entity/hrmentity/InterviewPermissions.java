package com.corpseed.entity.hrmentity;

import com.corpseed.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class InterviewPermissions {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(length = 50)
	private String department;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	public InterviewPermissions() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InterviewPermissions(int id, String department, User user) {
		super();
		this.id = id;
		this.department = department;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
