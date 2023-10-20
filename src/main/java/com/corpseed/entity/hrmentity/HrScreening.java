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
public class HrScreening {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
		
	@Column(length = 2,columnDefinition = "integer COMMENT '1 selected, 2 rejected, 3 on-hold'")
	@NotBlank(message = "Please select status !!")
	private String status;
	
	@Column(length = 50)
	private String interviewMode;
	
	@Column(columnDefinition = "TEXT")
	@NotBlank(message = "Please describe selction/rejection reason...")
	private String comment;
	
	@Column(length = 100)
	private String addedByUuid;
	
	@ManyToOne
	@JoinColumn(name = "interviewerId")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "trackAppId")
	private TrackApplication trackApplication;
	
	@Column(length = 50)
	private String dateTime;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 completed, 2 not completed'")
	private int roundStatus=2;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 email sended, 2 email not sended'")
	private int hrEmailStatus=2;
	
	@Column(length = 20)
	private String postDate;

	public HrScreening() {
		super();
		// TODO Auto-generated constructor stub
	}	

	public HrScreening(long id, String uuid,
			@NotBlank(message = "Please select status !!") String status, String interviewMode,
			@NotBlank(message = "Please describe selction/rejection reason...") String comment, String addedByUuid,
			User user, TrackApplication trackApplication, String dateTime) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.status = status;
		this.interviewMode = interviewMode;
		this.comment = comment;
		this.addedByUuid = addedByUuid;
		this.user = user;
		this.trackApplication = trackApplication;
		this.dateTime = dateTime;
	}

	public int getRoundStatus() {
		return roundStatus;
	}

	public void setRoundStatus(int roundStatus) {
		this.roundStatus = roundStatus;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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

	public String getInterviewMode() {
		return interviewMode;
	}

	public void setInterviewMode(String interviewMode) {
		this.interviewMode = interviewMode;
	}

	public String getAddedByUuid() {
		return addedByUuid;
	}

	public void setAddedByUuid(String addedByUuid) {
		this.addedByUuid = addedByUuid;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
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

}
