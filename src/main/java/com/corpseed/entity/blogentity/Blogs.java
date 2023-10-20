package com.corpseed.entity.blogentity;

import com.corpseed.entity.Category;
import com.corpseed.entity.serviceentity.ServiceBlogs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Blogs {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
		
	@NotBlank(message = "Please enter meta title !!")
	@Column(length = 255)
	private String metaTitle;
	
	@Transient
	private String serviceName;
	
	@NotBlank(message = "Please enter meta keyword !!")
	@Column(columnDefinition = "TEXT")
	private String metaKeyword;
		
	@NotBlank(message = "Please provide meta description !!")
	@Column(columnDefinition = "TEXT")
	private String metaDescription;
	
	@Column(length = 50)
	private String ratingUser="0";
	
	@Column(length = 5)
	private String ratingValue="0";
	
	@Column(length = 2)
	private String displayStatus;
	
	@Column(length = 100)
	private String addedByUUID;
	
	@Column(length = 100)
	private String postedByName;
	
	@Column(length = 100)
	@NotBlank(message = "Please select author !!")
	private String postedByUuid;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	private long visited=0;
	
	@Column(length = 5)
	private String cardPosition;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
		
	@ManyToOne(targetEntity = Category.class,fetch = FetchType.EAGER)
	@JoinColumn(name = "categoryId")
	private Category category;
	
	@OneToMany(mappedBy = "blogs",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<ServiceBlogs> serviceBlogs=new ArrayList<>();
	
	@OneToMany(mappedBy = "blogs",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<BlogServiceCardList> blogServiceCardLists=new ArrayList<>();
	
	@OneToMany(mappedBy = "blogs",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<BlogFaq> blogFaq=new ArrayList<>();
	
	public Blogs() {
		super();
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


	public String getMetaTitle() {
		return metaTitle;
	}


	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}


	public String getServiceName() {
		return serviceName;
	}


	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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


	public String getDisplayStatus() {
		return displayStatus;
	}


	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}


	public String getAddedByUUID() {
		return addedByUUID;
	}


	public void setAddedByUUID(String addedByUUID) {
		this.addedByUUID = addedByUUID;
	}


	public String getPostedByName() {
		return postedByName;
	}


	public void setPostedByName(String postedByName) {
		this.postedByName = postedByName;
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


	public long getVisited() {
		return visited;
	}


	public void setVisited(long visited) {
		this.visited = visited;
	}


	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	public List<ServiceBlogs> getServiceBlogs() {
		return serviceBlogs;
	}


	public void setServiceBlogs(List<ServiceBlogs> serviceBlogs) {
		this.serviceBlogs = serviceBlogs;
	}

	public List<BlogServiceCardList> getBlogServiceCardLists() {
		return blogServiceCardLists;
	}

	public void setBlogServiceCardLists(List<BlogServiceCardList> blogServiceCardLists) {
		this.blogServiceCardLists = blogServiceCardLists;
	}

	public String getCardPosition() {
		return cardPosition;
	}

	public void setCardPosition(String cardPosition) {
		this.cardPosition = cardPosition;
	}

	public String getPostedByUuid() {
		return postedByUuid;
	}

	public void setPostedByUuid(String postedByUuid) {
		this.postedByUuid = postedByUuid;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public List<BlogFaq> getBlogFaq() {
		return blogFaq;
	}

	public void setBlogFaq(List<BlogFaq> blogFaq) {
		this.blogFaq = blogFaq;
	}

}
