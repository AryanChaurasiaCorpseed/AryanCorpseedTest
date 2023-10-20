package com.corpseed.entity.serviceentity;

import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.serviceentity.Services;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ServiceBlogs {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@ManyToOne
	@JoinColumn(name = "blogId")
	private Blogs blogs;
	
	@ManyToOne(targetEntity = Services.class)
	@JoinColumn(name = "serviceId")
	private Services services;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public ServiceBlogs() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServiceBlogs(long id, String uuid, Blogs blogs, Services services) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.blogs = blogs;
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

	public Blogs getBlogs() {
		return blogs;
	}

	public void setBlogs(Blogs blogs) {
		this.blogs = blogs;
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
