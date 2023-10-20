package com.corpseed.entity.couponentity;

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
import javax.validation.constraints.NotBlank;

@Entity
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid;
	
	@NotBlank(message = "Please enter coupon title !!")
	@Column(length = 255)
	private String title;
	
	@NotBlank(message = "Please enter coupon value !!")
	@Column(length = 11)
	private String value;
	
	@Column(length = 50)
	private String type;
	
	@Column(length = 50)
	private String serviceType;
	
	@NotBlank(message = "Please enter coupon start date !!")
	@Column(length = 20)
	private String startDate;
	
	@NotBlank(message = "Please enter coupon end date !!")
	@Column(length = 20)
	private String endDate;
	
	@Column(length = 2)
	private String displayStatus;
	
	@Column(length = 100)
	private String addedByUUID;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 20)
	private String maximumDiscount;
	
	@Column(length = 2)
	private int couponPushStatus;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@OneToMany(mappedBy = "coupon",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.EAGER)
	private List<CouponServices> couponServices=new ArrayList<>();

	public Coupon() {
		super();
		// TODO Auto-generated constructor stub
	}
		
	public Coupon(long id, String uuid, @NotBlank(message = "Please enter coupon title !!") String title,
			@NotBlank(message = "Please enter coupon value !!") String value, String type, String serviceType,
			@NotBlank(message = "Please enter coupon start date !!") String startDate,
			@NotBlank(message = "Please enter coupon end date !!") String endDate, String displayStatus,
			String addedByUUID, String postDate, String modifyDate, String maximumDiscount, int couponPushStatus,
			List<CouponServices> couponServices) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.title = title;
		this.value = value;
		this.type = type;
		this.serviceType = serviceType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.displayStatus = displayStatus;
		this.addedByUUID = addedByUUID;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
		this.maximumDiscount = maximumDiscount;
		this.couponPushStatus = couponPushStatus;
		this.couponServices = couponServices;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public String getMaximumDiscount() {
		return maximumDiscount;
	}

	public void setMaximumDiscount(String maximumDiscount) {
		this.maximumDiscount = maximumDiscount;
	}
 
	public List<CouponServices> getCouponServices() {
		return couponServices;
	}

	public void setCouponServices(List<CouponServices> couponServices) {
		this.couponServices = couponServices;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public int getCouponPushStatus() {
		return couponPushStatus;
	}

	public void setCouponPushStatus(int couponPushStatus) {
		this.couponPushStatus = couponPushStatus;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

}
