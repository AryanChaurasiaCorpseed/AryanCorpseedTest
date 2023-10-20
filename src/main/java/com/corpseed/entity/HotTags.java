package com.corpseed.entity;

import com.corpseed.entity.serviceentity.Services;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class HotTags {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@Column(length = 100)
	@NotBlank(message = "Please enter tag name")
	private String name;
	
	@OneToOne(targetEntity = Services.class,fetch = FetchType.LAZY)
	@JoinColumn(name = "serviceId")
	private Services services;
	
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
	
	public HotTags() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HotTags(long id, String uuid, @NotBlank(message = "Please enter tag name") String name, Services services,
			String displayStatus, String postDate, String modifyDate, String addedByUUID) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.services = services;
		this.displayStatus = displayStatus;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
		this.addedByUUID = addedByUUID;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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

	public Services getServices() {
		return services;
	}

	public void setServices(Services services) {
		this.services = services;
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
