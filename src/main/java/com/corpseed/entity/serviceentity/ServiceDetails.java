package com.corpseed.entity.serviceentity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class ServiceDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid;
	
	@NotBlank(message = "Please enter tab name !!")
	@Column(length = 100)
	private String tabName;
	
	@NotBlank(message = "Please enter title !!")
	@Column(length = 255)
	private String title;
	
	@NotBlank(message = "Please write description !!")
	@Column(columnDefinition = "LONGTEXT")	
	private String description;
	
	@NotBlank(message = "Please enter display order !!")
	@Column(length = 2)
	private String displayOrder;
	
	@NotBlank(message = "Please choose display status !!")
	@Column(length = 2)
	private String displayStatus;
	
	@Column(length = 2)
	private String formShowStatus;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 100)
	private String addedByUUID;
	
	@Column(length = 5)
	private String cardPosition;
	
	@ManyToOne
	@JoinColumn(name="serviceId")
	private Services services;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@OneToMany(mappedBy = "serviceDetails",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.EAGER)
	private List<ServiceCardList> serviceCardLists=new ArrayList<>();

	public ServiceDetails() {
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

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
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

	public String getAddedByUUID() {
		return addedByUUID;
	}

	public void setAddedByUUID(String addedByUUID) {
		this.addedByUUID = addedByUUID;
	}

	public Services getServices() {
		return services;
	}

	public void setServices(Services services) {
		this.services = services;
	}


	public String getFormShowStatus() {
		return formShowStatus;
	}


	public void setFormShowStatus(String formShowStatus) {
		this.formShowStatus = formShowStatus;
	}

	public List<ServiceCardList> getServiceCardLists() {
		return serviceCardLists;
	}

	public void setServiceCardLists(List<ServiceCardList> serviceCardLists) {
		this.serviceCardLists = serviceCardLists;
	}

	public String getCardPosition() {
		return cardPosition;
	}

	public void setCardPosition(String cardPosition) {
		this.cardPosition = cardPosition;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
	
}
