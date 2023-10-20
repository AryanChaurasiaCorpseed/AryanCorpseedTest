package com.corpseed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String type;
	
	private String ratingValue;
	
	@Column(columnDefinition = "TEXT")
	private String comment;
	
	private String postDate;
	
	private String ipAddress;
	
	private String feedbackUrl;
	
	private String browser;
	
	private String session;
	
	private String operatingSystem;

	public Feedback() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Feedback(long id, String type, String ratingValue, String comment, String postDate, String ipAddress,
			String feedbackUrl, String browser, String session, String operatingSystem) {
		super();
		this.id = id;
		this.type = type;
		this.ratingValue = ratingValue;
		this.comment = comment;
		this.postDate = postDate;
		this.ipAddress = ipAddress;
		this.feedbackUrl = feedbackUrl;
		this.browser = browser;
		this.session = session;
		this.operatingSystem = operatingSystem;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRatingValue() {
		return ratingValue;
	}

	public void setRatingValue(String ratingValue) {
		this.ratingValue = ratingValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getFeedbackUrl() {
		return feedbackUrl;
	}

	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	
	
	
}
