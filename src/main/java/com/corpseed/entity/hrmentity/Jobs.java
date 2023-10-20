package com.corpseed.entity.hrmentity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
public class Jobs {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid;
	
	@NotBlank(message = "Please enter job title !!")
	@Column(length = 255)
	private String title;
	
	@NotBlank(message = "Please enter slug !!")
	@Column(length = 255)
	private String slug;
	
	@NotBlank(message = "Please enter job position !!")
	@Column(length = 255)
	private String position;
	
	@NotBlank(message = "Please describe about job !!")
	@Column(columnDefinition = "LONGTEXT")
	private String description;
	
	@NotBlank(message = "Please select job title !!")
	@Column(length = 255)
	private String jobTitle;
	
	@Email(message = "Email should be valid")
	@NotBlank(message = "Please enter email !!")
	@Column(length = 255)
	private String email;
	
	@NotBlank(message = "Please enter job qualification !!")
	@Column(length = 255)
	private String qualification;
	
	@NotBlank(message = "Please enter job experience !!")
	@Column(length = 255)
	private String experience;
	
	@NotBlank(message = "Please enter monthly salary !!")
	@Column(length = 20)
	private String salary;
	
	@NotBlank(message = "Please enter total position open !!")
	@Column(length = 20)
	private String noOfPosition;
	
	@Column(length = 20)
	private String positionFilled="0";
	
	@NotBlank(message = "Please enter job locations !!")
	private String jobLocation;
	
	@NotBlank(message = "Please choose display status !!")
	@Column(length = 2)
	private String displayStatus;
	
	@Column(length = 100)
	private String addedByUUID;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 200)
	@NotBlank(message ="Enter meta title !!")
	private String metaTitle;
	
	@NotBlank(message = "Enter meta keyword !!")
	@Column(columnDefinition = "TEXT")
	private String metaKeyword;
	
	@NotBlank(message = "Enter meta description !!")
	@Column(columnDefinition = "TEXT")
	private String metaDescription;
	
	@Column(length = 20)
	@NotBlank(message = "Enter job expire date !!")
	private String expireDate;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@OneToMany(mappedBy = "jobs",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<JobApplication> jobApplication=new ArrayList<>();

	public Jobs() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Jobs(long id, String uuid, @NotBlank(message = "Please enter job title !!") String title,
			@NotBlank(message = "Please enter slug !!") String slug,
			@NotBlank(message = "Please enter job position !!") String position,
			@NotBlank(message = "Please describe about job !!") String description,
			@NotBlank(message = "Please select job title !!") String jobTitle,
			@Email(message = "Email should be valid") @NotBlank(message = "Please enter email !!") String email,
			@NotBlank(message = "Please enter job qualification !!") String qualification,
			@NotBlank(message = "Please enter job experience !!") String experience,
			@NotBlank(message = "Please enter monthly salary !!") String salary,
			@NotBlank(message = "Please enter total position open !!") String noOfPosition, String positionFilled,
			@NotBlank(message = "Please enter job locations !!") String jobLocation,
			@NotBlank(message = "Please choose display status !!") String displayStatus, String addedByUUID,
			String postDate, String modifyDate, @NotBlank(message = "Enter meta title !!") String metaTitle,
			@NotBlank(message = "Enter meta keyword !!") String metaKeyword,
			@NotBlank(message = "Enter meta description !!") String metaDescription,
			@NotBlank(message = "Enter job expire date !!") String expireDate, List<JobApplication> jobApplication) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.title = title;
		this.slug = slug;
		this.position = position;
		this.description = description;
		this.jobTitle = jobTitle;
		this.email = email;
		this.qualification = qualification;
		this.experience = experience;
		this.salary = salary;
		this.noOfPosition = noOfPosition;
		this.positionFilled = positionFilled;
		this.jobLocation = jobLocation;
		this.displayStatus = displayStatus;
		this.addedByUUID = addedByUUID;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
		this.metaTitle = metaTitle;
		this.metaKeyword = metaKeyword;
		this.metaDescription = metaDescription;
		this.expireDate = expireDate;
		this.jobApplication = jobApplication;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getJobLocation() {
		return jobLocation;
	}

	public void setJobLocation(String jobLocation) {
		this.jobLocation = jobLocation;
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

	public List<JobApplication> getJobApplication() {
		return jobApplication;
	}

	public void setJobApplication(List<JobApplication> jobApplication) {
		this.jobApplication = jobApplication;
	}

	public String getNoOfPosition() {
		return noOfPosition;
	}

	public void setNoOfPosition(String noOfPosition) {
		this.noOfPosition = noOfPosition;
	}

	public String getPositionFilled() {
		return positionFilled;
	}

	public void setPositionFilled(String positionFilled) {
		this.positionFilled = positionFilled;
	}

	public String getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}

	public String getMetaKeyword() {
		return metaKeyword;
	}

	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	
}
