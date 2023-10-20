package com.corpseed.entity.lifeentity;

import com.corpseed.entity.lifeentity.CultureGallery;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class OurCulture {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String uuid;
	
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Column(length = 1,columnDefinition = "integer default 1 COMMENT '1 enable, 2 disable'")
	private int displayStatus;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@OneToMany(mappedBy = "ourCulture",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<CultureGallery> cultureGallery=new ArrayList<>();

	public OurCulture() {
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

	public int getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(int displayStatus) {
		this.displayStatus = displayStatus;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
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

	public List<CultureGallery> getCultureGallery() {
		return cultureGallery;
	}

	public void setCultureGallery(List<CultureGallery> cultureGallery) {
		this.cultureGallery = cultureGallery;
	}
	
}
