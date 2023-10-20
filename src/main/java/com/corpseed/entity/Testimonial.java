package com.corpseed.entity;

import com.corpseed.entity.serviceentity.Services;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "testmonial")
public class Testimonial {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id; 
	
	@NotNull(message = "Null Value not allowd..!!")
	@NotBlank(message = "Blank value not allowed..!!")
	@NotEmpty(message = "Value should not be empty..!!")
	private String name;
	
	@NotNull(message = "Null Value not allowd..!!")
	@NotBlank(message = "Blank value not allowed..!!")
	@NotEmpty(message = "Value should not be empty..!!")
	private String company;
	
	@NotNull(message = "Null Value not allowd..!!")
	@NotBlank(message = "Blank value not allowed..!!")
	@NotEmpty(message = "Value should not be empty..!!")
	private String position;
	
	@Column(name = "profile_picture")
	private String profilePicture;
	
	@NotNull(message = "Null Value not allowd..!!")
	@NotBlank(message = "Blank value not allowed..!!")
	@NotEmpty(message = "Value should not be empty..!!")
	@Column(name = "post_date")
	private String postDate;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@ManyToOne(targetEntity = Services.class)
	@JoinColumn(name = "serviceId",nullable = false)
	private Services services;

	public Testimonial() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Services getServices() {
		return services;
	}

	public void setServices(Services services) {
		this.services = services;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
	
}
