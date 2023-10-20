package com.corpseed.repository.orderrepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.orderentity.OrderNoGenerator;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderNoGeneratorRepo extends JpaRepository<OrderNoGenerator, Long> {

	OrderNoGenerator findTop1ByRegDateOrderByIdDesc(String today);

}
