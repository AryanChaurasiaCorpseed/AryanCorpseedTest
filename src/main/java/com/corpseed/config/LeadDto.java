package com.corpseed.config;

import java.util.Date;

public class LeadDto {
	String uuid;

	String name;

	String leadName;

	String email;

	String leadDescription;

	String mobileNo;

	String urls;

	Date createDate ;

	Date lastUpdated;

	Date latestStatusChangeDate;

	String source ;

	String PrimaryAddress ;

	boolean isDeleted;

	String city;
	
	Long productId;

	String categoryId;

	String serviceId;

	String industryId;

	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	String ipAddress;

	String displayStatus;
	
	Long assigneeId;


	int whatsAppStatus;



	public String getName() {
		return name;
	}

	public String getLeadName() {
		return leadName;
	}

	public String getLeadDescription() {
		return leadDescription;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public Date getLatestStatusChangeDate() {
		return latestStatusChangeDate;
	}

	public String getSource() {
		return source;
	}

	public String getPrimaryAddress() {
		return PrimaryAddress;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public String getCity() {
		return city;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getIndustryId() {
		return industryId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getDisplayStatus() {
		return displayStatus;
	}

	public int getWhatsAppStatus() {
		return whatsAppStatus;
	}

	public String getEmail() {
		return email;
	}

	public String getUrls() {
		return urls;
	}

	public String getUuid() {
		return uuid;
	}

	//Setter

	public void setName(String name) {
		this.name = name;
	}

	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}

	public void setLeadDescription(String leadDescription) {
		this.leadDescription = leadDescription;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void setLatestStatusChangeDate(Date latestStatusChangeDate) {
		this.latestStatusChangeDate = latestStatusChangeDate;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setPrimaryAddress(String primaryAddress) {
		PrimaryAddress = primaryAddress;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}

	public void setWhatsAppStatus(int whatsAppStatus) {
		this.whatsAppStatus = whatsAppStatus;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
