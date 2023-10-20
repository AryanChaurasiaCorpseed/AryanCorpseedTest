package com.corpseed.entity.footerentity;

import com.corpseed.entity.serviceentity.Services;

import javax.persistence.*;

@Entity
public class FooterService {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@OneToOne(targetEntity = Services.class,fetch = FetchType.LAZY)
	@JoinColumn(name = "serviceId")
	private Services services;
		 
	@ManyToOne(targetEntity = FooterCategory.class,fetch = FetchType.LAZY)
	@JoinColumn(name = "footerId")
	private FooterCategory footerCategory;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public FooterService() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	public FooterService(long id, String uuid, Services services, FooterCategory footerCategory, String postDate) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.services = services;
		this.footerCategory = footerCategory;
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
		return services;
	}

	public void setService(Services services) {
		this.services = services;
	}	

	public FooterCategory getFooterCategory() {
		return footerCategory;
	}

	public void setFooterCategory(FooterCategory footerCategory) {
		this.footerCategory = footerCategory;
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
