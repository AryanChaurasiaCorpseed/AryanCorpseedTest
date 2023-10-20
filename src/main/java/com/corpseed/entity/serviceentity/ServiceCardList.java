package com.corpseed.entity.serviceentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ServiceCardList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@ManyToOne
	@JoinColumn(name = "serviceId")
	private Services service;
	
	@ManyToOne
	@JoinColumn(name = "serviceDetailsId")
	private ServiceDetails serviceDetails;
	
	@Column(length = 20)
	private String postDate;

	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	public ServiceCardList() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ServiceCardList(long id, String uuid, Services service, ServiceDetails serviceDetails, String postDate) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.service = service;
		this.serviceDetails = serviceDetails;
		this.postDate = postDate;
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

	public Services getService() {
		return service;
	}

	public void setService(Services service) {
		this.service = service;
	}

	public ServiceDetails getServiceDetails() {
		return serviceDetails;
	}

	public void setServiceDetails(ServiceDetails serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
	
}
