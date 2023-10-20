package com.corpseed.entity.orderentity;

import com.corpseed.entity.serviceentity.ServicePackage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderPackage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="packageId")
	private ServicePackage servicePackage;
	
	@ManyToOne
	@JoinColumn(name="orderId")
	private Orders orders;

	public OrderPackage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderPackage(long id, ServicePackage servicePackage, Orders orders) {
		super();
		this.id = id;
		this.servicePackage = servicePackage;
		this.orders = orders;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ServicePackage getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(ServicePackage servicePackage) {
		this.servicePackage = servicePackage;
	}

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}
	
}
