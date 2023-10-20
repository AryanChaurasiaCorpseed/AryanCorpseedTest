package com.corpseed.entity.orderentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderNoGenerator {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String orderNo;
	
	@Column(length = 20)
	private String regDate;

	public OrderNoGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}


	public OrderNoGenerator(long id, String orderNo, String regDate) {
		super();
		this.id = id;
		this.orderNo = orderNo;
		this.regDate = regDate;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getRegDate() {
		return regDate;
	}


	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
}
