package com.corpseed.entity.schedulerentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmailScheduler {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String sendTo;
	
	private String emailCC;
	
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String message;
	
	@Column(length = 2,columnDefinition = "integer default 2 COMMENT '1 success, 2 pending'")
	private int status;

	public EmailScheduler() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public EmailScheduler(long id, String sendTo, String emailCC, String subject, String message, int status) {
		super();
		this.id = id;
		this.sendTo = sendTo;
		this.emailCC = emailCC;
		this.subject = subject;
		this.message = message;
		this.status = status;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public String getEmailCC() {
		return emailCC;
	}


	public void setEmailCC(String emailCC) {
		this.emailCC = emailCC;
	}
	
	
}
