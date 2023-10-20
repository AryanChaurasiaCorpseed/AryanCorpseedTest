package com.corpseed.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Country {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid;
	
	@NotBlank(message = "Please enter country name !!")
	@Column(length = 50)
	private String countryName;
	
	@NotBlank(message = "Please enter short name of country !!")
	@Column(length = 10)
	private String shortName;
	
	@NotBlank(message = "Please enter country code !!")
	@Column(length = 10)
	private String countryCode;
	
	@Column(length = 2)
	private String displayStatus;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 100)
	private String addedByUUID;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@OneToMany(mappedBy = "country",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<State> state=new ArrayList<>();

	public Country() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Country(long id, String uuid, @NotBlank(message = "Please enter country name !!") String countryName,
			@NotBlank(message = "Please enter short name of country !!") String shortName,
			@NotBlank(message = "Please enter country code !!") String countryCode, String displayStatus,
			String postDate, String modifyDate, String addedByUUID) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.countryName = countryName;
		this.shortName = shortName;
		this.countryCode = countryCode;
		this.displayStatus = displayStatus;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
		this.addedByUUID = addedByUUID;
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

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public List<State> getState() {
		return state;
	}

	public void setState(List<State> state) {
		this.state = state;
	}


}
