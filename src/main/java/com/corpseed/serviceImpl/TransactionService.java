package com.corpseed.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.paymententity.Transactions;
import com.corpseed.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	public List<Transactions> getAllTransactions() {
		return this.transactionRepository.findByDeleteStatusOrderByIdDesc(2);
	}
	
	@CacheEvict(value = "txnByOrderNo",allEntries = true)
	public void saveTransaction(Transactions txs) {
		this.transactionRepository.save(txs);
		
	}

	@Cacheable("txnByOrderNo")
	public List<Transactions> getTransactionByOrderNo(String orderNo) {
		return this.transactionRepository.findByOrderNoAndDeleteStatus(orderNo,2);
	}

	public List<Transactions> getOrdersTransaction(String orderNo) {
		return this.transactionRepository.findByOrderNoAndDeleteStatus(orderNo,2);
	}

	public Transactions findByUuid(String uuid) {
		return this.transactionRepository.findByUuidAndDeleteStatus(uuid,2);
	}
}
