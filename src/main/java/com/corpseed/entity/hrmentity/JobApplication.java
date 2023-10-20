package com.corpseed.entity.hrmentity;

import com.corpseed.entity.User;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
public class JobApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100,unique = true)
	private String uuid;
	
	@NotBlank(message = "Please enter name !!")
	@Column(length = 100)
	private String name;
	
	@Email(message = "Email should be in proper formate !!")
	@NotBlank(message = "Please enter email-id !!")
	private String email;
	
	@NotBlank(message = "Please enter mobile number")
	@Column(length = 15)
	private String mobile;
	
	@NotBlank(message = "Please enter your current address !!")
	@Column(length = 255)
	private String address;
	
	@NotBlank(message = "Please enter your current ctc !!")
	@Column(length = 50)
	private String currentCTC;
	
	@NotBlank(message = "Please enter your experience !!")
	@Column(length = 50)
	private String experience;
	
	@NotBlank(message = "Please enter qualification !!")
	@Column(length = 100)
	private String qualification;
	
	@NotBlank(message = "Please enter your location !!")
	@Column(length = 255)
	private String location;
	
	@NotBlank(message = "Please enter best goal of yours !!")
	@Column(columnDefinition = "TINYTEXT")
	private String bestGoal;
	
	@Column(length = 255)
	private String attachedFile;
	
	@NotBlank(message = "Please choose display status !!")
	@Column(length = 2)
	private String displayStatus;
	
	@Column(length = 2)
	private String applicationStatus="3";
	
	@Column(length = 2)
	private String uploadDocStatus="2";
	
	@Column(length = 1)
	private String uploadDocEmail="2";
	
	@Column(length = 100)
	private String addedByUUID;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@ManyToOne
	@JoinColumn(name="jobId")
	private Jobs jobs;

	@ManyToOne
	@JoinColumn(name="interviewerId")
	private User user;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@Column(length = 50)
	private String holdRound;
	
	@OneToOne
	private User holdBy;
	
	@OneToOne
	private User actionBy;
	
	@OneToMany(mappedBy = "jobApplication",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<InterviewSchedule> interviewSchedule=new ArrayList<>();
	
	@OneToOne(mappedBy = "jobApplication",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private TrackApplication trackApplication;
	
	@OneToMany(mappedBy = "jobApplication",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<CandidateDocuments> candidateDocument=new ArrayList<>();
	
	@Column(columnDefinition = "TEXT")
	private String motivation;
	
	public JobApplication() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCurrentCTC() {
		return currentCTC;
	}

	public void setCurrentCTC(String currentCTC) {
		this.currentCTC = currentCTC;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBestGoal() {
		return bestGoal;
	}

	public void setBestGoal(String bestGoal) {
		this.bestGoal = bestGoal;
	}

	public String getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(String attachedFile) {
		this.attachedFile = attachedFile;
	}

	public String getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}

	public String getAddedByUUID() {
		return addedByUUID;
	}

	public void setAddedByUUID(String addedByUUID) {
		this.addedByUUID = addedByUUID;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}
	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	public Jobs getJobs() {
		return jobs;
	}

	public void setJobs(Jobs jobs) {
		this.jobs = jobs;
	}

	public List<InterviewSchedule> getInterviewSchedule() {
		return interviewSchedule;
	}


	public void setInterviewSchedule(List<InterviewSchedule> interviewSchedule) {
		this.interviewSchedule = interviewSchedule;
	}


	public TrackApplication getTrackApplication() {
		return trackApplication;
	}


	public void setTrackApplication(TrackApplication trackApplication) {
		this.trackApplication = trackApplication;
	}

	public String getUploadDocStatus() {
		return uploadDocStatus;
	}

	public void setUploadDocStatus(String uploadDocStatus) {
		this.uploadDocStatus = uploadDocStatus;
	}

	public List<CandidateDocuments> getCandidateDocument() {
		return candidateDocument;
	}

	public void setCandidateDocument(List<CandidateDocuments> candidateDocument) {
		this.candidateDocument = candidateDocument;
	}

	public String getUploadDocEmail() {
		return uploadDocEmail;
	}

	public void setUploadDocEmail(String uploadDocEmail) {
		this.uploadDocEmail = uploadDocEmail;
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

	public String getHoldRound() {
		return holdRound;
	}

	public void setHoldRound(String holdRound) {
		this.holdRound = holdRound;
	}

	public User getHoldBy() {
		return holdBy;
	}

	public void setHoldBy(User holdBy) {
		this.holdBy = holdBy;
	}

	public User getActionBy() {
		return actionBy;
	}

	public void setActionBy(User actionBy) {
		this.actionBy = actionBy;
	}

	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}
	
}
