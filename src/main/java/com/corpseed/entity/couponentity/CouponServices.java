package com.corpseed.entity.couponentity;

import com.corpseed.entity.serviceentity.Services;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CouponServices {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
	
	@ManyToOne
	@JoinColumn(name = "serviceId")
	private Services services;
	
	@ManyToOne
	@JoinColumn(name="couponId")
	private Coupon coupon;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public CouponServices() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CouponServices(long id, String uuid, Services services, Coupon coupon) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.services = services;
		this.coupon = coupon;
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

	public Services getServices() {
		return services;
	}

	public void setServices(Services services) {
		this.services = services;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
	
}
