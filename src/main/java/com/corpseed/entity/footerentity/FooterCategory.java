package com.corpseed.entity.footerentity;

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
public class FooterCategory {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@Column(length = 100)
	@NotBlank(message = "Please select category !!")
	private String categoryName;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;	
	
	@Column(length = 1)
	private String displayStatus="2";
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@OneToMany(mappedBy = "footerCategory",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.EAGER)
	private List<FooterService> footerService=new ArrayList<>();

	public FooterCategory() { 
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public String getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}

	public List<FooterService> getFooterService() {
		return footerService;
	}

	public void setFooterService(List<FooterService> footerService) {
		this.footerService = footerService;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
}
