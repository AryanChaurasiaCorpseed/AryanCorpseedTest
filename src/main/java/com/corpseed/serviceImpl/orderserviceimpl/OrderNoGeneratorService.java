package com.corpseed.serviceImpl.orderserviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.orderentity.OrderNoGenerator;
import com.corpseed.repository.orderrepo.OrderNoGeneratorRepo;

@Service
public class OrderNoGeneratorService {

	@Autowired
	private OrderNoGeneratorRepo orderNoGeneratorRepo;

	public OrderNoGenerator getLastOrderNo(String today) {
		return this.orderNoGeneratorRepo.findTop1ByRegDateOrderByIdDesc(today);
	}

	public void addOrderNo(OrderNoGenerator orderNoGenerator) {
		this.orderNoGeneratorRepo.save(orderNoGenerator);		
	}
}
