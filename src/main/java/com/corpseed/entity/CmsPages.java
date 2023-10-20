package com.corpseed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class CmsPages {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100,unique = true)
	private String uuid;
	
	@NotBlank(message = "Please enter title !!")
	@Column(length = 255)
	private String title;
	
	@NotBlank(message = "Please enter slug !!")
	@Column(length = 255)
	private String slug;
	
	@NotBlank(message = "Please enter heading !!")
	@Column(length = 255)
	private String heading;
	
	@NotBlank(message = "Please write about cms pages !!")
	@Column(columnDefinition = "LONGTEXT")
	private String description;
	
	@NotBlank(message = "Please enter meta title !!")
	@Column(length = 255)
	private String metaTitle;
	
	@NotBlank(message = "Please enter meta keyword !!")
	@Column(length = 255)
	private String metaKeyword;
	
	@NotBlank(message = "Please enter meta description  !!")
	@Column(columnDefinition = "TINYTEXT")
	private String metaDescription;	
		
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

	public CmsPages() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CmsPages(long id, String uuid, @NotBlank(message = "Please enter title !!") String title,
			@NotBlank(message = "Please enter slug !!") String slug,
			@NotBlank(message = "Please enter heading !!") String heading,
			@NotBlank(message = "Please write about cms pages !!") String description,
			@NotBlank(message = "Please enter meta title !!") String metaTitle,
			@NotBlank(message = "Please enter meta keyword !!") String metaKeyword,
			@NotBlank(message = "Please enter meta description  !!") String metaDescription,			
			@NotBlank(message = "Please choose display status !!") String displayStatus, String postDate,
			String modifyDate, String addedByUUID) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.title = title;
		this.slug = slug;
		this.heading = heading;
		this.description = description;
		this.metaTitle = metaTitle;
		this.metaKeyword = metaKeyword;
		this.metaDescription = metaDescription;		
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}

	public String getMetaKeyword() {
		return metaKeyword;
	}

	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
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
