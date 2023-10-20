package com.corpseed.entity.hrmentity;

import com.corpseed.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class HrmBlog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@Column(length = 255)
	@NotBlank(message = "Please enter blog title !!")
	private String title;
	
	@NotBlank(message = "Please enter slug !!")
	@Column(length = 255)
	private String slug;
	
	private String image;
	
	@NotBlank(message = "Please enter blog summary !!")
	@Column(columnDefinition = "TEXT")
	private String summary;
	
	@NotBlank(message = "Please provide blog description !!")
	@Column(columnDefinition = "LONGTEXT")
	private String description;
		
	@Column(length = 2,columnDefinition = "integer default 1 COMMENT '1 enable, 2 disable'")
	private int displayStatus=1;
		
	@ManyToOne
	@JoinColumn(name = "postedByUserId")
	private User user;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate; 
			
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	@NotBlank(message = "Please enter meta title !!")
	@Column(length = 255)
	private String metaTitle;

	@NotBlank(message = "Please enter meta keyword !!")
	@Column(columnDefinition = "TEXT")
	private String metaKeyword;

	@NotBlank(message = "Please provide meta description !!")
	@Column(columnDefinition = "TEXT")
	private String metaDescription;

	public HrmBlog() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDisplayStatus() {
		return displayStatus;
	}
	public void setDisplayStatus(int displayStatus) {
		this.displayStatus = displayStatus;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
}
