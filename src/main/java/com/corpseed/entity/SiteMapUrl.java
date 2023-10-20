package com.corpseed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SiteMapUrl {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;

	private long uid;
	
	@Column(length = 50)
	private String type;
	
	private String url;
	
	@Column(length = 2)
	private int status;
	
	@Column(length = 20)
	private String postDate;

	public SiteMapUrl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SiteMapUrl(long id, String uuid, long uid, String type, String url, int status, String postDate) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.uid = uid;
		this.type = type;
		this.url = url;
		this.status = status;
		this.postDate = postDate;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}
	
	
}
