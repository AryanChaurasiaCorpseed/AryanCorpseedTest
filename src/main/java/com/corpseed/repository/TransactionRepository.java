package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.paymententity.Transactions;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long>{

	List<Transactions> findByOrderNoAndDeleteStatus(String orderNo,int dStatus);

	List<Transactions> findByDeleteStatusOrderByIdDesc(int dStatus);

	Transactions findByUuidAndDeleteStatus(String uuid,int dStatus);

}
