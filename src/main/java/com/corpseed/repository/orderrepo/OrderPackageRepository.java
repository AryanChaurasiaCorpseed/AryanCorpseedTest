package com.corpseed.repository.orderrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.orderentity.OrderPackage;
import com.corpseed.entity.orderentity.Orders;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPackageRepository extends JpaRepository<OrderPackage, Long> {

	List<OrderPackage> findByOrders(Orders order);

}
