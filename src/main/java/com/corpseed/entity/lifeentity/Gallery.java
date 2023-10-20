package com.corpseed.entity.lifeentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Gallery {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid;
	
	@Column(length = 255)
	private String fileName;

	@Column(length = 20)
	private String uploadDate;
	
	@Column(length = 2)
	private String displayStatus;
	
	@Column(length = 100)
	private String addedByUuid;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	public Gallery() {
		super();
		// TODO Auto-generated constructor stub
	}	

	public Gallery(long id, String uuid, String fileName, String uploadDate, String displayStatus, String addedByUuid) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.fileName = fileName;
		this.uploadDate = uploadDate;
		this.displayStatus = displayStatus;
		this.addedByUuid = addedByUuid;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}

	public String getAddedByUuid() {
		return addedByUuid;
	}

	public void setAddedByUuid(String addedByUuid) {
		this.addedByUuid = addedByUuid;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
}
