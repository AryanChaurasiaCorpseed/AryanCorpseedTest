package com.corpseed.entity.enquiryentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Enquiry {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid;
	
	@Column(length = 250)
	private String type;
	
	@Column(columnDefinition = "TEXT")
	private String message;
	
	@NotBlank(message = "Please enter your name !!")
	@Column(length = 255)
	private String name;
	
	@NotBlank(message = "Please enter email !!")
	@Column(length = 255)
	private String email;
	
	@NotBlank(message = "Please enter mobile number !!")
	@Column(length = 50)
	@Size(min = 10)
	private String mobile;

	@NotBlank(message = "Please enter your city !!")
	@Column(length = 250)
	private String city;
	
	@Column(length = 100)
	private String categoryId;
	
	@Column(length = 100)
	private String serviceId;
	
	@Column(length = 100)
	private String industryId;
		
	@Column(columnDefinition = "TINYTEXT")
	private String url;
	
	@Column(length = 255)
	private String ipAddress;
	
	@Column(length = 2)
	private String displayStatus;
	
	@Column(length = 220)
	private String postDate;
	
	@Column(length = 220)
	private String modifyDate;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 whatsApp number, 2 not whatsApp'")
	private int whatsAppStatus=2;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1.success, 2.pending'")
	private int bitrixStatus=2;

	public Enquiry() {
		super();
		// TODO Auto-generated constructor stub
	}		
	public Enquiry(long id, String uuid, String type, String message,
			@NotBlank(message = "Please enter your name !!") String name,
			@NotBlank(message = "Please enter email !!") String email,
			@NotBlank(message = "Please enter mobile number !!") @Size(min = 10) String mobile,
			@NotBlank(message = "Please enter your city !!") String city, String categoryId, String serviceId,
			String industryId, String url, String ipAddress, String displayStatus, String postDate, String modifyDate,
			int whatsAppStatus, int deleteStatus, int bitrixStatus) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.type = type;
		this.message = message;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.city = city;
		this.categoryId = categoryId;
		this.serviceId = serviceId;
		this.industryId = industryId;
		this.url = url;
		this.ipAddress = ipAddress;
		this.displayStatus = displayStatus;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
		this.whatsAppStatus = whatsAppStatus;
		this.deleteStatus = deleteStatus;
		this.bitrixStatus = bitrixStatus;
	}

	public int getWhatsAppStatus() {
		return whatsAppStatus;
	}
	public void setWhatsAppStatus(int whatsAppStatus) {
		this.whatsAppStatus = whatsAppStatus;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getIndustryId() {
		return industryId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
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

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public int getBitrixStatus() {
		return bitrixStatus;
	}

	public void setBitrixStatus(int bitrixStatus) {
		this.bitrixStatus = bitrixStatus;
	}

}
