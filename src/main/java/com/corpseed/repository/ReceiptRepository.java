package com.corpseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.ReceiptGenerator;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<ReceiptGenerator, Long> {

	ReceiptGenerator findTop1ByOrderByIdDesc();


}
