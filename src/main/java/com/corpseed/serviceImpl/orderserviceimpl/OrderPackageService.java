package com.corpseed.serviceImpl.orderserviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.orderentity.OrderPackage;
import com.corpseed.entity.orderentity.Orders;
import com.corpseed.repository.orderrepo.OrderPackageRepository;

@Service
public class OrderPackageService {

	@Autowired
	private OrderPackageRepository orderPackageRepository;

	public void saveOrdersPackage(List<OrderPackage> ordpkglist) {
		this.orderPackageRepository.saveAll(ordpkglist);
	}

	public List<OrderPackage> getOrderPackageByOrder(Orders order) {
		return this.orderPackageRepository.findByOrders(order);
	}
}
