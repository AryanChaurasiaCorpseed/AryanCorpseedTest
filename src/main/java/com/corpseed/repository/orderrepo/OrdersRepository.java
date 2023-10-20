package com.corpseed.repository.orderrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.orderentity.Orders;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

	Orders findByUuidAndDeleteStatus(String orderuuid,int dStatus);

	List<Orders> findByDeleteStatusOrderByIdDesc(int dStatus);

	Orders findByOrderNoAndDeleteStatus(String oRDERID,int dStatus);

	List<Orders> findByOrderPushStatusAndOrderStatusAndDeleteStatus(int staus, String staus2
			,int dStatus);

}
