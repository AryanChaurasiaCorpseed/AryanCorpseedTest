package com.corpseed.entity.hrmentity;

import com.corpseed.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class TechnicalRound {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@Column(length = 2,columnDefinition = "integer COMMENT '1 selected, 2 rejected, 3 on-hold'")
	@NotBlank(message = "Please select status !!")
	private String status;
	
	@Column(columnDefinition = "TEXT")
	@NotBlank(message = "Please describe selction/rejection reason...")
	private String comment;
	
	@OneToOne
	@JoinColumn(name = "trackAppId")
	private TrackApplication trackApplication;
	
	@Column(length = 50)
	private String dateTime;
		
	@Column(length = 10)
	private String interviewMode;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 completed, 2 not completed'")
	private int roundStatus=2;
	
	@ManyToOne
	@JoinColumn(name = "interviewerId")
	private User user;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 email sended, 2 email not sended'")
	private int hrEmailStatus=2;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 100)
	private String addedByUuid;

	public TechnicalRound() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public int getRoundStatus() {
		return roundStatus;
	}


	public void setRoundStatus(int roundStatus) {
		this.roundStatus = roundStatus;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public TrackApplication getTrackApplication() {
		return trackApplication;
	}

	public void setTrackApplication(TrackApplication trackApplication) {
		this.trackApplication = trackApplication;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getInterviewMode() {
		return interviewMode;
	}

	public void setInterviewMode(String interviewMode) {
		this.interviewMode = interviewMode;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getHrEmailStatus() {
		return hrEmailStatus;
	}

	public void setHrEmailStatus(int hrEmailStatus) {
		this.hrEmailStatus = hrEmailStatus;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getAddedByUuid() {
		return addedByUuid;
	}

	public void setAddedByUuid(String addedByUuid) {
		this.addedByUuid = addedByUuid;
	}	
	
}
