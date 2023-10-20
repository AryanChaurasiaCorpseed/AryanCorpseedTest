package com.corpseed.entity.blogentity;

import com.corpseed.entity.serviceentity.Services;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class BlogServiceCardList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@ManyToOne
	@JoinColumn(name = "serviceId")
	private Services service;
	
	@ManyToOne
	@JoinColumn(name = "blogId")
	private Blogs blogs;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public BlogServiceCardList() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public BlogServiceCardList(long id, String uuid, Services service, Blogs blogs, String postDate, int deleteStatus) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.service = service;
		this.blogs = blogs;
		this.postDate = postDate;
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

	public Services getService() {
		return service;
	}

	public void setService(Services service) {
		this.service = service;
	}

	public Blogs getBlogs() {
		return blogs;
	}

	public void setBlogs(Blogs blogs) {
		this.blogs = blogs;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}


	public int getDeleteStatus() {
		return deleteStatus;
	}


	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
	
}
