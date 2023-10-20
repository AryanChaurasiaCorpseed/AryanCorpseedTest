package com.corpseed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.corpseed.serviceImpl.TransactionService;

@Controller
@RequestMapping("/admin/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@GetMapping("/")
	public String allTransactions(Model model) {
		model.addAttribute("appendClass", "transaction");
		model.addAttribute("transactions", this.transactionService.getAllTransactions());
		return "admin/transactions";
	}
}
