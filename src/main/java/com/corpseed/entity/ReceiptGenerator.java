package com.corpseed.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ReceiptGenerator {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String receiptNo;
	
	@Column(length = 20)
	private String regDate;

	public ReceiptGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ReceiptGenerator(long id, String receiptNo, String regDate) {
		super();
		this.id = id;
		this.receiptNo = receiptNo;
		this.regDate = regDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
}
