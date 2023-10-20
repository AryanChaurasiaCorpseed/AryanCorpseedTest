package com.corpseed.entity;

import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.serviceentity.Services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Category { 

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid;
	
	@NotBlank(message = "Please select category name !!")
	@Column(length = 100)
	private String categoryName;
	
	@NotBlank(message = "Please enter sub-category name !!")
	@Column(length = 200,unique = true)
	private String subCategoryName;
	
	@NotBlank(message = "Please enter title !!")
	@Column(length = 100)
	private String title;
	
	@NotBlank(message = "Please enter slug !!")
	@Column(length = 100)
	private String slug;
	
	@NotBlank(message = "Please enter meta title !!")
	@Column(length = 200)
	private String metaTitle;
	
	@NotBlank(message = "Please enter meta keyword !!")
	@Column(columnDefinition = "TEXT")
	private String metaKeyword;
	
	@NotBlank(message = "Please enter meta description !!")
	@Column(columnDefinition = "TEXT")
	private String metaDescription;
	
	@Column(length = 50)
	private String ratingUser="0";
	
	@Column(length = 5)
	private String ratingValue="0";
	
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
	
	@Column(length = 100)
	private String icon;
	
	@OneToMany(mappedBy = "category",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<Blogs> blogs=new ArrayList<>();
	
	@OneToMany(mappedBy = "category",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.EAGER)
	private List<Services> services=new ArrayList<>();

	@OneToMany(mappedBy = "category",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<LawUpdate> lawUpdate=new ArrayList<>();
	
	public Category() {
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
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

	public String getRatingUser() {
		return ratingUser;
	}

	public void setRatingUser(String ratingUser) {
		this.ratingUser = ratingUser;
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

	public List<Blogs> getBlogs() {
		return blogs;
	}

	public void setBlogs(List<Blogs> blogs) {
		this.blogs = blogs;
	}

	public List<Services> getServices() {
		return services;
	}

	public void setServices(List<Services> services) {
		this.services = services;
	}

	public List<LawUpdate> getLawUpdate() {
		return lawUpdate;
	}

	public void setLawUpdate(List<LawUpdate> lawUpdate) {
		this.lawUpdate = lawUpdate;
	}
	public int getDeleteStatus() {
		return deleteStatus;
	}
	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

}
