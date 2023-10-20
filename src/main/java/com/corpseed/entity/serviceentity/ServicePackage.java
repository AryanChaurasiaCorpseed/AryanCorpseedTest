package com.corpseed.entity.serviceentity;

import com.corpseed.entity.serviceentity.Services;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class ServicePackage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid;
	
	@NotBlank(message = "Enter package name !!")
	@Column(length = 255)
	private String packageName;
	
	@NotBlank(message = "Please enter show price !!")
	@Column(length = 11)
	private String showPrice;
	
	@NotBlank(message="Please enter professional fee !!")
	@Column(length = 11)
	private String professionalFees;
	
	@NotBlank(message = "Please enter government fee !!")
	@Column(length = 11)
	private String governmentFees;
	
	@NotBlank(message = "Please enter miscellaneous fees !!")
	@Column(length = 11)
	private String miscellaneousFees;
	
	@NotBlank(message = "Please enter package price !!")
	@Column(length = 11)
	private String packagePrice;
	
	@NotBlank(message = "Please enter summary !!")
	@Column(columnDefinition = "TINYTEXT")
	private String summary;
	
	@Column(length = 2)
	private String defaultPackageStatus;
	
	@Column(length = 2)
	private String displayStatus;
	
	@Column(length = 100)
	private String addedByUUID;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@ManyToOne
	@JoinColumn(name = "serviceId")
	private Services services;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public ServicePackage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServicePackage(long id, String uuid, @NotBlank(message = "Enter package name !!") String packageName,
			@NotBlank(message = "Please enter show price !!") String showPrice,
			@NotBlank(message = "Please enter professional fee !!") String professionalFees,
			@NotBlank(message = "Please enter government fee !!") String governmentFees,
			@NotBlank(message = "Please enter miscellaneous fees !!") String miscellaneousFees,
			@NotBlank(message = "Please enter package price !!") String packagePrice,
			@NotBlank(message = "Please enter summary !!") String summary, String defaultPackageStatus,
			String displayStatus, String addedByUUID, String postDate, String modifyDate, Services services) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.packageName = packageName;
		this.showPrice = showPrice;
		this.professionalFees = professionalFees;
		this.governmentFees = governmentFees;
		this.miscellaneousFees = miscellaneousFees;
		this.packagePrice = packagePrice;
		this.summary = summary;
		this.defaultPackageStatus = defaultPackageStatus;
		this.displayStatus = displayStatus;
		this.addedByUUID = addedByUUID;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
		this.services = services;
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

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getShowPrice() {
		return showPrice;
	}

	public void setShowPrice(String showPrice) {
		this.showPrice = showPrice;
	}

	public String getProfessionalFees() {
		return professionalFees;
	}

	public void setProfessionalFees(String professionalFees) {
		this.professionalFees = professionalFees;
	}

	public String getGovernmentFees() {
		return governmentFees;
	}

	public void setGovernmentFees(String governmentFees) {
		this.governmentFees = governmentFees;
	}

	public String getMiscellaneousFees() {
		return miscellaneousFees;
	}

	public void setMiscellaneousFees(String miscellaneousFees) {
		this.miscellaneousFees = miscellaneousFees;
	}

	public String getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(String packagePrice) {
		this.packagePrice = packagePrice;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDefaultPackageStatus() {
		return defaultPackageStatus;
	}

	public void setDefaultPackageStatus(String defaultPackageStatus) {
		this.defaultPackageStatus = defaultPackageStatus;
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

	public Services getServices() {
		return services;
	}

	public void setServices(Services services) {
		this.services = services;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	
	
}
