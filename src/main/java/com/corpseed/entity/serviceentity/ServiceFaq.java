package com.corpseed.entity.serviceentity;

import com.corpseed.entity.serviceentity.Services;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class ServiceFaq {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid;
	
	@Column(length = 255)
	@NotBlank(message = "Please enter title !!")
	private String title;
	
	@NotBlank(message = "Please write Faq's description !!")
	@Column(columnDefinition = "LONGTEXT")
	private String description;
	
	@Column(length = 2)
	@NotBlank(message = "Please choose display status")
	private String displayStatus;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 100)
	private String addedByUUID;
	
	@ManyToOne
	@JoinColumn(name = "serviceId")
	private Services services;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public ServiceFaq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServiceFaq(long id, String uuid, @NotBlank(message = "Please enter title !!") String title,
			@NotBlank(message = "Please write Faq's description !!") String description,
			@NotBlank(message = "Please choose display status") String displayStatus, String postDate,
			String modifyDate, String addedByUUID, Services services) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.title = title;
		this.description = description;
		this.displayStatus = displayStatus;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
		this.addedByUUID = addedByUUID;
		this.services = services;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
