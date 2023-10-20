package com.corpseed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100,unique = true)
	private String uuid;
	
	@Column(length = 100)
	@NotBlank(message = "Please enter your name !!")
	private String name;
	
	@Column(length = 100)
	@NotBlank(message = "Please enter your position !!")
	private String position;
	
	@Column(length = 255)
	private String image;
	
	@Column(columnDefinition = "TEXT")
	@NotBlank(message = "Please write sometext about company !!")
	private String summary;
	
	@Column(length = 3)
	@NotBlank(message = "Please rate use out of 5 !!")
	private String ratingValue="1";
	
	@Column(length = 2)
	private String showHomeStatus;
	
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
		
	public Review() {
		super();
		// TODO Auto-generated constructor stub
	}	

	public Review(long id, String uuid, @NotBlank(message = "Please enter your name !!") String name,
			@NotBlank(message = "Please enter your position !!") String position, String image,
			@NotBlank(message = "Please write sometext about company !!") String summary,
			@NotBlank(message = "Please rate use out of 5 !!") String ratingValue, String showHomeStatus,
			String displayStatus, String postDate, String modifyDate, String addedByUUID, int deleteStatus) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.position = position;
		this.image = image;
		this.summary = summary;
		this.ratingValue = ratingValue;
		this.showHomeStatus = showHomeStatus;
		this.displayStatus = displayStatus;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
		this.addedByUUID = addedByUUID;
		this.deleteStatus = deleteStatus;
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getRatingValue() {
		return ratingValue;
	}

	public void setRatingValue(String ratingValue) {
		this.ratingValue = ratingValue;
	}

	public String getShowHomeStatus() {
		return showHomeStatus;
	}

	public void setShowHomeStatus(String showHomeStatus) {
		this.showHomeStatus = showHomeStatus;
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
