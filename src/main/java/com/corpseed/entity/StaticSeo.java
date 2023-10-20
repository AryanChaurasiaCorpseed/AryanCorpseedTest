package com.corpseed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class StaticSeo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@Column(length = 100)
	@NotBlank(message = "Select Page !!")
	private String page;
	
	@Column(length = 200)
	@NotBlank(message = "Enter mete title !!")
	private String metaTitle;
	
	@Column(columnDefinition = "TEXT")
	@NotBlank(message = "Enter mete keyword !!")
	private String metaKeyword;
	
	@Column(columnDefinition = "TEXT")
	@NotBlank(message = "Enter meta description !!")
	private String metaDescription;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public StaticSeo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StaticSeo(long id, String uuid, @NotBlank(message = "Select Page !!") String page,
			@NotBlank(message = "Enter mete title !!") String metaTitle,
			@NotBlank(message = "Enter mete keyword !!") String metaKeyword,
			@NotBlank(message = "Enter meta description !!") String metaDescription, String postDate,
			String modifyDate) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.page = page;
		this.metaTitle = metaTitle;
		this.metaKeyword = metaKeyword;
		this.metaDescription = metaDescription;
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

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
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
