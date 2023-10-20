package com.corpseed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TrandingSearch {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;	
	@Column(length = 100)
	private String uuid;
	private String searchName;
	private long searched;
	
	public TrandingSearch() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TrandingSearch(long id, String uuid, String searchName, long searched) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.searchName = searchName;
		this.searched = searched;
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
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	public long getSearched() {
		return searched;
	}
	public void setSearched(long searched) {
		this.searched = searched;
	}
	
}
