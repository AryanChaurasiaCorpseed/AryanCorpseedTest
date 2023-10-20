package com.corpseed.entity.documententity;

import com.corpseed.entity.hrmentity.TrackApplication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class DocumentVerification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@Column(length = 2)
	@NotBlank(message = "Please select status !!")
	private String status;
	
	@Column(columnDefinition = "TEXT")
	@NotBlank(message = "Please describe selction/rejection reason...")
	private String comment;

	@OneToOne
	@JoinColumn(name = "trackAppId")
	private TrackApplication trackApplication;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 completed, 2 not completed'")
	private int roundStatus=2;
	
	public DocumentVerification() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DocumentVerification(long id, String uuid, @NotBlank(message = "Please select status !!") String status,
			@NotBlank(message = "Please describe selction/rejection reason...") String comment,
			TrackApplication trackApplication) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.status = status;
		this.comment = comment;
		this.trackApplication = trackApplication;
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

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	
}
