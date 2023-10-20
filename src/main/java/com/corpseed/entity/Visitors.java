package com.corpseed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Visitors {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	@Column(length = 20)
	private String type;
	@Column(length = 100)
	private String ipAddress;
	@Column(length = 20)
	private String visitedDate;
	private String browser;
	private String url;
	private int visited;
	private String operatingSystem;
	@Column(length = 100)
	private String blogServiceUuid;
	private String title;
	@Column(length = 10)
	private String day;
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
		
	public Visitors() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Visitors(long id, String uuid, String type, String ipAddress, String visitedDate, String browser, String url,
			int visited, String operatingSystem, String blogServiceUuid, String title, String day) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.type = type;
		this.ipAddress = ipAddress;
		this.visitedDate = visitedDate;
		this.browser = browser;
		this.url = url;
		this.visited = visited;
		this.operatingSystem = operatingSystem;
		this.blogServiceUuid = blogServiceUuid;
		this.title = title;
		this.day = day;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getVisitedDate() {
		return visitedDate;
	}

	public void setVisitedDate(String visitedDate) {
		this.visitedDate = visitedDate;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getVisited() {
		return visited;
	}

	public void setVisited(int visited) {
		this.visited = visited;
	}

	public String getBlogServiceUuid() {
		return blogServiceUuid;
	}

	public void setBlogServiceUuid(String blogServiceUuid) {
		this.blogServiceUuid = blogServiceUuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

}
