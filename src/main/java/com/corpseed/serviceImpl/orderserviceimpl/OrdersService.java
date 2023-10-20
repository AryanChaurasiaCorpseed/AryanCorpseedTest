package com.corpseed.serviceImpl.orderserviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.orderentity.Orders;
import com.corpseed.repository.orderrepo.OrdersRepository;

@Service
public class OrdersService {

	@Autowired
	private OrdersRepository ordersRepository;
	
	public List<Orders> getAllOrders(){
		return this.ordersRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	@CacheEvict(value = "orderByUuid",allEntries = true)
	public Orders saveOrder(Orders order) {
		return this.ordersRepository.save(order);
	}

	@Cacheable("orderByUuid")
	public Orders getOrderByUuid(String orderuuid) {
		return this.ordersRepository.findByUuidAndDeleteStatus(orderuuid,2);
	}

	public Orders getOrderByOrderNo(String ORDERID) {
		return this.ordersRepository.findByOrderNoAndDeleteStatus(ORDERID,2);
	}

	public List<Orders> findByOrderPushStatusAndOrderStatus(int staus, String staus2) {
		return this.ordersRepository.findByOrderPushStatusAndOrderStatusAndDeleteStatus(staus, staus2,2);
	}

	@CacheEvict(value = "orderByUuid",allEntries = true)
	public void updateAllOrders(List<Orders> failedList) {
		this.ordersRepository.saveAll(failedList);
	}

	@CacheEvict(value = "orderByUuid",allEntries = true)
	public void saveOrders(List<Orders> orders) {
		this.ordersRepository.saveAll(orders);
	}
}
