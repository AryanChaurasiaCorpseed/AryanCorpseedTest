package com.corpseed.entity.contactentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class ContactAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100,unique = true)
	private String uuid;
	
	@NotBlank(message = "Please enter title !!")
	@Column(length = 255)
	private String title;
	
	@NotBlank(message = "Please enter email-id !!")
	@Email(message = "Email should be in proper formate !!")
	@Column(length = 100)
	private String email;
	
	@NotBlank(message = "Please enter mobile number !!")
	@Size(min = 10,max = 10,message = "Please enter 10 digit mobile number !!")
	@Column(length = 15)
	private String mobile;
	
	@NotBlank(message = "Please enter contact address !!")
	@Column(length = 255)
	private String address;
	
	@NotBlank(message = "Please choose display status !!")
	@Column(length = 2)
	private String displayStatus;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 100)
	private String addedByUUID;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public ContactAddress() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ContactAddress(long id, String uuid, @NotBlank(message = "Please enter title !!") String title,
			@NotBlank(message = "Please enter email-id !!") @Email(message = "Email should be in proper formate !!") String email,
			@NotBlank(message = "Please enter mobile number !!") @Size(min = 10, max = 10, message = "Please enter 10 digit mobile number !!") String mobile,
			@NotBlank(message = "Please enter contact address !!") String address,
			@NotBlank(message = "Please choose display status !!") String displayStatus, String postDate,
			String modifyDate, String addedByUUID) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.title = title;
		this.email = email;
		this.mobile = mobile;
		this.address = address;
		this.displayStatus = displayStatus;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
		this.addedByUUID = addedByUUID;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getAddedByUUID() {
		return addedByUUID;
	}

	public void setAddedByUUID(String addedByUUID) {
		this.addedByUUID = addedByUUID;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
	
}
