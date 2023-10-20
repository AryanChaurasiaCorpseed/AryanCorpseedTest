package com.corpseed.entity.hrmentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class JobReport {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@Column(length = 100)
	private String hrName;
	
	@Column(length = 100)
	private String reportingAuthority;
	
	@Column(length = 20)
	private String postDate;
		
	@OneToOne
	@JoinColumn(name = "offerLetterId")
	private OfferLetter offerLetter;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public JobReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JobReport(long id, String uuid, String hrName, String reportingAuthority, String postDate,
			OfferLetter offerLetter) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.hrName = hrName;
		this.reportingAuthority = reportingAuthority;
		this.postDate = postDate;
		this.offerLetter = offerLetter;
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

	public String getHrName() {
		return hrName;
	}

	public void setHrName(String hrName) {
		this.hrName = hrName;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public OfferLetter getOfferLetter() {
		return offerLetter;
	}

	public void setOfferLetter(OfferLetter offerLetter) {
		this.offerLetter = offerLetter;
	}
	public String getReportingAuthority() {
		return reportingAuthority;
	}
	public void setReportingAuthority(String reportingAuthority) {
		this.reportingAuthority = reportingAuthority;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

}
