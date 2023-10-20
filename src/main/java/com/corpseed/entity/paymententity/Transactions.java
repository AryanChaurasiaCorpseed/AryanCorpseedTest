package com.corpseed.entity.paymententity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Transactions {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid;
	
	@Column(length = 20)
	private String type;
	
	@Column(length = 100)
	private String receiptNo;
	
	@Column(length = 50)
	private String orderNo;
	
	@Column(length = 100)
	@NotBlank(message = "Please enter name !!")
	private String name;
	
	@Column(length = 50)
	@NotBlank(message = "Please enter email !!")	
	private String email;
	
	@Column(length = 50)
	private String mobile;	
	
	private String bankTaxnId;
	
	@Column(length = 20)
	private String txnAmount;
	
	private String txnId;
	@Column(length = 20)
	private String txDate;
	@Column(length = 2)
	private String txStatus;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public Transactions() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Transactions(long id, String uuid, String type, String receiptNo, String orderNo,
			@NotBlank(message = "Please enter name !!") String name,
			@NotBlank(message = "Please enter email !!") String email, String mobile, String bankTaxnId,
			String txnAmount, String txnId, String txDate, String txStatus) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.type = type;
		this.receiptNo = receiptNo;
		this.orderNo = orderNo;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.bankTaxnId = bankTaxnId;
		this.txnAmount = txnAmount;
		this.txnId = txnId;
		this.txDate = txDate;
		this.txStatus = txStatus;
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

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBankTaxnId() {
		return bankTaxnId;
	}

	public void setBankTaxnId(String bankTaxnId) {
		this.bankTaxnId = bankTaxnId;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getTxDate() {
		return txDate;
	}

	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}

	public String getTxStatus() {
		return txStatus;
	}

	public void setTxStatus(String txStatus) {
		this.txStatus = txStatus;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	
}
