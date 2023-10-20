package com.corpseed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class LawUpdate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100,unique = true)
	private String uuid;
	
	@NotBlank(message = "Please enter title !!")
	@Column(length = 255,unique = true)
	private String title;
	
	@NotBlank(message = "Please enter slug !!")
	@Column(length = 255)
	private String slug;
	
	@NotBlank(message = "Please enter refrence slug !!")
	@Column(length = 255)
	private String refrenceSlug;
	
	@NotBlank(message = "Please enter summary !!")
	@Column(columnDefinition = "TEXT")
	private String summary;
	
	@NotBlank(message = "Please enter department !!")
	@Column(length = 50)
	private String department;
	
	@NotBlank(message = "Please enter authority !!")
	@Column(length = 50)
	private String authority;
	
	@NotBlank(message = "Please enter publish date !!")
	@Column(length = 20)
	private String publishDate;
	
	@NotBlank(message = "Please enter applicable date !!")
	@Column(length = 20)
	private String applicableDate;
	
	@Column(length = 20)
	private String displayStatus;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 100)
	private String addedByUUID;
	
	@Column(length = 200)
	@NotBlank(message ="Enter meta title !!")
	private String metaTitle;
	
	@NotBlank(message = "Enter meta keyword !!")
	@Column(columnDefinition = "TEXT")
	private String metaKeyword;
	
	@NotBlank(message = "Enter meta description !!")
	@Column(columnDefinition = "TEXT")
	private String metaDescription;
	
	@ManyToOne
	@JoinColumn(name = "categoryId")
	private Category category;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public LawUpdate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LawUpdate(long id, String uuid, @NotBlank(message = "Please enter title !!") String title,
			@NotBlank(message = "Please enter slug !!") String slug,
			@NotBlank(message = "Please enter refrence slug !!") String refrenceSlug,
			@NotBlank(message = "Please enter summary !!") String summary,
			@NotBlank(message = "Please enter department !!") String department,
			@NotBlank(message = "Please enter authority !!") String authority,
			@NotBlank(message = "Please enter publish date !!") String publishDate,
			@NotBlank(message = "Please enter applicable date !!") String applicableDate, String displayStatus,
			String postDate, String modifyDate, String addedByUUID,
			@NotBlank(message = "Enter meta title !!") String metaTitle,
			@NotBlank(message = "Enter meta keyword !!") String metaKeyword,
			@NotBlank(message = "Enter meta description !!") String metaDescription, Category category) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.title = title;
		this.slug = slug;
		this.refrenceSlug = refrenceSlug;
		this.summary = summary;
		this.department = department;
		this.authority = authority;
		this.publishDate = publishDate;
		this.applicableDate = applicableDate;
		this.displayStatus = displayStatus;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
		this.addedByUUID = addedByUUID;
		this.metaTitle = metaTitle;
		this.metaKeyword = metaKeyword;
		this.metaDescription = metaDescription;
		this.category = category;
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

	public String getRefrenceSlug() {
		return refrenceSlug;
	}

	public void setRefrenceSlug(String refrenceSlug) {
		this.refrenceSlug = refrenceSlug;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getApplicableDate() {
		return applicableDate;
	}

	public void setApplicableDate(String applicableDate) {
		this.applicableDate = applicableDate;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	
}
