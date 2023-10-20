package com.corpseed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
public class Meeting {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100,unique = true)
	private String uuid;
	
	@NotBlank(message = "Enter your name !1")
	@Column(length = 100)
	private String name;
	
	@NotBlank(message = "Please enter email !!")
	@Email(message = "Email should be in proper formate !!")
	@Column(length = 100)
	private String email;
	
	@Column(length = 50)
	@NotBlank(message = "Please enter mobile number !!")
	private String mobile;
	
	@NotBlank(message = "Please select date and time !!")
	@Column(length = 50)
	private String dateTime;
	
	@NotBlank(message = "Please enter your city !!")
	@Column(length = 100)
	private String city;
	
	@NotBlank(message = "Please write meeting about !!")
	@Column(columnDefinition = "TEXT")
	private String message;
	
	@Column(length = 2)
	private String displayStatus;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public Meeting() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Meeting(long id, String uuid, @NotBlank(message = "Enter your name !1") String name,
			@NotBlank(message = "Please enter email !!") @Email(message = "Email should be in proper formate !!") String email,
			@NotBlank(message = "Please enter mobile number !!") String mobile,
			@NotBlank(message = "Please select date and time !!") String dateTime,
			@NotBlank(message = "Please enter your city !!") String city,
			@NotBlank(message = "Please write meeting about !!") String message, String displayStatus, String postDate,
			String modifyDate) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.dateTime = dateTime;
		this.city = city;
		this.message = message;
		this.displayStatus = displayStatus;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
}
