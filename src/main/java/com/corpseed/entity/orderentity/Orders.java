package com.corpseed.entity.orderentity;

import com.corpseed.entity.serviceentity.Services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotBlank
	@Column(unique = true,length = 100)
	private String uuid;
	
	@Column(length = 50)
	private String orderNo;
		
	@Column(length = 100)
	private String orderAmount;
	
	@Column(length = 100)
	private String coupon;
	
	@Column(length = 10)
	private String discount;
	
	private String bankTxNo;
	
	private String txId;
	
	@Column(length = 50)
	private String txStatus;
	
	@NotBlank(message = "Please enter name")
	@Column(length = 100)
	private String name;
	
	@NotBlank(message = "Please enter email")
	@Email(message = "Email should be in proper formate")
	@Column(length = 100)
	private String email;
	
	@NotBlank(message = "Please enter mobile")
	@Column(length = 50)
	private String mobile;
	
	@NotBlank(message = "Please enter city")
	@Column(length = 100)
	private String city;
	
	@Column(length = 2)
	private String orderStatus;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 50)
	private String productNo;
	
	@Column(length = 5)
	private String cgstPercent;
	
	@Column(length = 5)
	private String sgstPercent;
	
	@Column(length = 50)
	private String cgstAmount;
	
	@Column(length = 50)
	private String sgstAmount;
	
	@Column(length = 2)
	private int orderPushStatus;
	
	@OneToMany(mappedBy = "orders",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<OrderPackage> orderPackage=new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "serviceId")
	private Services services;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;

	public Orders() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	public Orders(long id, @NotBlank String uuid, String orderNo, String orderAmount, String coupon, String discount,
			String bankTxNo, String txId, String txStatus, @NotBlank(message = "Please enter name") String name,
			@NotBlank(message = "Please enter email") @Email(message = "Email should be in proper formate") String email,
			@NotBlank(message = "Please enter mobile") String mobile,
			@NotBlank(message = "Please enter city") String city, String orderStatus, String postDate,
			String modifyDate, String productNo, String cgstPercent, String sgstPercent, String cgstAmount,
			String sgstAmount, int orderPushStatus, List<OrderPackage> orderPackage, Services services) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.orderNo = orderNo;
		this.orderAmount = orderAmount;
		this.coupon = coupon;
		this.discount = discount;
		this.bankTxNo = bankTxNo;
		this.txId = txId;
		this.txStatus = txStatus;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.city = city;
		this.orderStatus = orderStatus;
		this.postDate = postDate;
		this.modifyDate = modifyDate;
		this.productNo = productNo;
		this.cgstPercent = cgstPercent;
		this.sgstPercent = sgstPercent;
		this.cgstAmount = cgstAmount;
		this.sgstAmount = sgstAmount;
		this.orderPushStatus = orderPushStatus;
		this.orderPackage = orderPackage;
		this.services = services;
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
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getCoupon() {
		return coupon;
	}
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getBankTxNo() {
		return bankTxNo;
	}
	public void setBankTxNo(String bankTxNo) {
		this.bankTxNo = bankTxNo;
	}
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	public String getTxStatus() {
		return txStatus;
	}
	public void setTxStatus(String txStatus) {
		this.txStatus = txStatus;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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
	public List<OrderPackage> getOrderPackage() {
		return orderPackage;
	}
	public void setOrderPackage(List<OrderPackage> orderPackage) {
		this.orderPackage = orderPackage;
	}
	public Services getServices() {
		return services;
	}
	public void setServices(Services services) {
		this.services = services;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getCgstPercent() {
		return cgstPercent;
	}

	public void setCgstPercent(String cgstPercent) {
		this.cgstPercent = cgstPercent;
	}

	public String getSgstPercent() {
		return sgstPercent;
	}

	public void setSgstPercent(String sgstPercent) {
		this.sgstPercent = sgstPercent;
	}

	public String getCgstAmount() {
		return cgstAmount;
	}

	public void setCgstAmount(String cgstAmount) {
		this.cgstAmount = cgstAmount;
	}

	public String getSgstAmount() {
		return sgstAmount;
	}

	public void setSgstAmount(String sgstAmount) {
		this.sgstAmount = sgstAmount;
	}

	public int getOrderPushStatus() {
		return orderPushStatus;
	}

	public void setOrderPushStatus(int orderPushStatus) {
		this.orderPushStatus = orderPushStatus;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

		
}
