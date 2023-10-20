package com.corpseed.entity;

import com.corpseed.entity.hrmentity.*;
import com.corpseed.entity.newsentity.News;
import com.corpseed.entity.newsentity.NewsCategory;
import com.corpseed.entity.serviceentity.ServiceContact;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.*;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid; 
	
	@NotBlank(message = "Please enter first name !!")
	@Column(length = 50)
	private String firstName;
	
	@NotBlank(message = "Please enter last name !!")
	@Column(length = 50)
	private String lastName; 
	
	@Email
	@NotBlank(message = "Please enter email id !!")
	@Column(length = 100)
	private String email;
	
	@NotBlank(message = "Please enter a stong password !!")
	@Column(length = 255)
	private String password;
	
	@NotBlank(message = "Please enter job title !!")
	@Column(length = 255)
	private String jobTitle;
	
	@Column(length = 255)
	private String profilePicture;
	
	@NotBlank(message = "Please write about user job title !!")
	@Column(columnDefinition = "TEXT")
	private String aboutMe;
	
	@Column(length = 2)
	private String displayStatus="1";
	
	@Column(length = 100)
	private String addedByUUID;
	
	@Column(length = 255)
	private String ipAddress; 
	
	@Column(length = 20)
	private String regDate;
	
	@Column(length = 50)
	@NotBlank(message = "Please select department !!")
	private String department;
	
	@Transient
	String permissions;
	
	@NotBlank(message = "Please select user role !!")
	@Column(length = 50)
	private String role;

	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@Column(length = 1,columnDefinition = "integer default 1 COMMENT '1 active, 2 inactive'")
	private int accountStatus=1;
	
	@OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private Position position;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<CandidateDocuments> docList=new ArrayList<>();
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.EAGER)
	private List<HrPermissions> hrPermissions=new ArrayList<>();
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<ForgetPassword> forgetPassword=new ArrayList<>();
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<JobApplication> jobApplications=new ArrayList<>();
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<TrackApplication>  trackApplications=new ArrayList<>();

	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<TechnicalRound> technicalRounds=new ArrayList<>();
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<HrScreening> hrScreening=new ArrayList<>();
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<InterviewPermissions> interviewPermissions=new ArrayList<>(); 
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<HrmBlog> hrmBlog=new ArrayList<>();
	
	@OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
	private EmployeeReview employeeReview;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<NewsCategory> newscategory=new ArrayList<>();
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<News> news=new ArrayList<>();

	@OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<ServiceContact> serviceContacts=new ArrayList<>();

	private String facebook;

	private String linkedin;

	private String twitter;

	@Column(columnDefinition = "TINYTEXT")
	private String address;

	@Min(value = 10,message = "Minimmum length should be 10 digits")
	private String mobile;
	private String slug;

	@NotBlank(message = "Please enter meta title !!")
	private String metaTitle;

	@NotBlank(message ="Please enter meta keyword !!")
	@Column(columnDefinition = "TEXT")
	private String metaKeyword;

	@NotBlank(message = "Please enter meta description !!")
	@Column(columnDefinition = "TEXT")
	private String metaDescription;

	public User() {
		super();
	}	

	public User(int id, String uuid, @NotBlank(message = "Please enter first name !!") String firstName,
			@NotBlank(message = "Please enter last name !!") String lastName,
			@Email @NotBlank(message = "Please enter email id !!") String email,
			@NotBlank(message = "Please enter a strong password !!") String password,
			@NotBlank(message = "Please enter job title !!") String jobTitle, String profilePicture,
			@NotBlank(message = "Please write about user job title !!") String aboutMe, String displayStatus,
			String addedByUUID, String ipAddress, String regDate, String department, String permissions,
			@NotBlank(message = "Please select user role !!") String role, int deleteStatus, Position position,
			List<CandidateDocuments> docList, List<HrPermissions> hrPermissions, List<ForgetPassword> forgetPassword,
			int accountStatus,String slug,String metaTitle,String metaKeyword,String metaDescription) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.jobTitle = jobTitle;
		this.profilePicture = profilePicture;
		this.aboutMe = aboutMe;
		this.displayStatus = displayStatus;
		this.addedByUUID = addedByUUID;
		this.ipAddress = ipAddress;
		this.regDate = regDate;
		this.department = department;
		this.permissions = permissions;
		this.role = role;
		this.deleteStatus = deleteStatus;
		this.position = position;
		this.docList = docList;
		this.hrPermissions = hrPermissions;
		this.forgetPassword = forgetPassword;
		this.accountStatus = accountStatus;
		this.slug=slug;
		this.metaTitle=metaTitle;
		this.metaKeyword=metaKeyword;
		this.metaDescription=metaDescription;
	}	

	public String getLinkedin() {
		return this.linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	};

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	public List<CandidateDocuments> getDocList() {
		return docList;
	}
	public void setDocList(List<CandidateDocuments> docList) {
		this.docList = docList;
	}

	public List<HrPermissions> getHrPermissions() {
		return hrPermissions;
	}

	public void setHrPermissions(List<HrPermissions> hrPermissions) {
		this.hrPermissions = hrPermissions;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public List<ForgetPassword> getForgetPassword() {
		return forgetPassword;
	}

	public void setForgetPassword(List<ForgetPassword> forgetPassword) {
		this.forgetPassword = forgetPassword;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public List<JobApplication> getJobApplications() {
		return jobApplications;
	}

	public void setJobApplications(List<JobApplication> jobApplications) {
		this.jobApplications = jobApplications;
	}

	public List<TrackApplication> getTrackApplications() {
		return trackApplications;
	}

	public void setTrackApplications(List<TrackApplication> trackApplications) {
		this.trackApplications = trackApplications;
	}

	public List<TechnicalRound> getTechnicalRounds() {
		return technicalRounds;
	}

	public void setTechnicalRounds(List<TechnicalRound> technicalRounds) {
		this.technicalRounds = technicalRounds;
	}

	public List<HrScreening> getHrScreening() {
		return hrScreening;
	}

	public void setHrScreening(List<HrScreening> hrScreening) {
		this.hrScreening = hrScreening;
	}

	public int getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}

	public List<InterviewPermissions> getInterviewPermissions() {
		return interviewPermissions;
	}

	public void setInterviewPermissions(List<InterviewPermissions> interviewPermissions) {
		this.interviewPermissions = interviewPermissions;
	}

	public List<HrmBlog> getHrmBlog() {
		return hrmBlog;
	}

	public void setHrmBlog(List<HrmBlog> hrmBlog) {
		this.hrmBlog = hrmBlog;
	}

	public EmployeeReview getEmployeeReview() {
		return employeeReview;
	}

	public void setEmployeeReview(EmployeeReview employeeReview) {
		this.employeeReview = employeeReview;
	}	
	
	public List<NewsCategory> getNewscategory() {
		return newscategory;
	}

	public void setNewscategory(List<NewsCategory> newscategory) {
		this.newscategory = newscategory;
	}

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}

	public String getFacebook() {
		return facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public List<ServiceContact> getServiceContacts() {
		return serviceContacts;
	}

	public void setServiceContacts(List<ServiceContact> serviceContacts) {
		this.serviceContacts = serviceContacts;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getMetaTitle() {
		return metaTitle;
	}

	public String getMetaKeyword() {
		return metaKeyword;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}

	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", uuid=" + uuid + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", password=" + password + ", jobTitle=" + jobTitle + ", profilePicture=" + profilePicture
				+ ", aboutMe=" + aboutMe + "]";
	}	
	
}
