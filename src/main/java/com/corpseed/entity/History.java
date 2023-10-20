package com.corpseed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class History {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;	
	private String type;	
	private long typeId;	
	private String browser;
	private String ip;
	private long userId;
	private String userName;
	private String date;
	private String time;
	private String itemName;
	public History() {
		super();
		// TODO Auto-generated constructor stub
	}	
	public History(long id, String uuid, String type, long typeId, String browser, String ip, long userId,
			String userName, String date, String time, String itemName) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.type = type;
		this.typeId = typeId;
		this.browser = browser;
		this.ip = ip;
		this.userId = userId;
		this.userName = userName;
		this.date = date;
		this.time = time;
		this.itemName = itemName;
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
	public long getTypeId() {
		return typeId;
	}
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}	
	
}
