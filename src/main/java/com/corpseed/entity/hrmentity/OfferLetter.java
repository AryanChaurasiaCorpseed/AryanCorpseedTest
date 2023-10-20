package com.corpseed.entity.hrmentity;

import com.corpseed.entity.hrmentity.JobReport;
import com.corpseed.entity.hrmentity.TrackApplication;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class OfferLetter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@Column(length = 100)
	@NotBlank(message = "Please enter candidate name !!")
	private String name;
		
	@Column(length = 50)
	@NotBlank(message = "Please enter job position !!")
	private String jobPosition;
	
	@Column(length = 50)
	@NotBlank(message = "Please enter monthly salary !!")
	private String salary;
	
	@Column(length = 20)
	@NotBlank(message = "Please choose joining date !!")
	private String joiningDate;
	
	@Column(length = 100)
	@NotBlank(message = "Please enter reporting name !!")
	private String hOReporting;
	
	private String offerLetterAttachment;
	
	@Column(length = 2)
	private String status="1";

	@OneToOne
	@JoinColumn(name = "trackAppId")
	private TrackApplication trackApplication;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@OneToOne(mappedBy = "offerLetter",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private JobReport jobReport;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 completed, 2 not completed'")
	private int roundStatus=2;
	
	public OfferLetter() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public OfferLetter(long id, String uuid, @NotBlank(message = "Please enter candidate name !!") String name,
			@NotBlank(message = "Please enter job position !!") String jobPosition,
			@NotBlank(message = "Please enter monthly salary !!") String salary,
			@NotBlank(message = "Please choose joining date !!") String joiningDate,
			@NotBlank(message = "Please enter reporting name !!") String hOReporting, String offerLetterAttachment,
			String status, TrackApplication trackApplication, JobReport jobReport) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.jobPosition = jobPosition;
		this.salary = salary;
		this.joiningDate = joiningDate;
		this.hOReporting = hOReporting;
		this.offerLetterAttachment = offerLetterAttachment;
		this.status = status;
		this.trackApplication = trackApplication;
		this.jobReport = jobReport;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getOfferLetterAttachment() {
		return offerLetterAttachment;
	}

	public void setOfferLetterAttachment(String offerLetterAttachment) {
		this.offerLetterAttachment = offerLetterAttachment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TrackApplication getTrackApplication() {
		return trackApplication;
	}

	public void setTrackApplication(TrackApplication trackApplication) {
		this.trackApplication = trackApplication;
	}

	public String gethOReporting() {
		return hOReporting;
	}

	public void sethOReporting(String hOReporting) {
		this.hOReporting = hOReporting;
	}

	public JobReport getJobReport() {
		return jobReport;
	}

	public void setJobReport(JobReport jobReport) {
		this.jobReport = jobReport;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
}
