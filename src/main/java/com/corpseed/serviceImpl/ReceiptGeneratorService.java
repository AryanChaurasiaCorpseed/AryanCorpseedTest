package com.corpseed.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.ReceiptGenerator;
import com.corpseed.repository.ReceiptRepository;

@Service
public class ReceiptGeneratorService {

	@Autowired
	private ReceiptRepository receiptRepository;

	public ReceiptGenerator getLastNo() {
		return this.receiptRepository.findTop1ByOrderByIdDesc();
	}

	public void addReceiptNo(String newSn,String today) {
		ReceiptGenerator receipt=new ReceiptGenerator(0, newSn,today);
		this.receiptRepository.save(receipt);
	}

}
