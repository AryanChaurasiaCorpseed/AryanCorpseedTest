package com.corpseed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.corpseed.entity.orderentity.Orders;
import com.corpseed.serviceImpl.orderserviceimpl.OrderPackageService;
import com.corpseed.serviceImpl.orderserviceimpl.OrdersService;
import com.corpseed.serviceImpl.TransactionService;

@Controller
@RequestMapping(value = "/admin/order")
public class OrdersController {

	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private OrderPackageService orderPkgService;
	
	@Autowired
	private TransactionService txnService;
	
	@GetMapping("/")
	public String orders(Model model) {
		model.addAttribute("appendClass", "order");
		model.addAttribute("orders", this.ordersService.getAllOrders());
		return "admin/orders";
	}
	
	@GetMapping("/packages/{orderUuid}")
	public String ordersPackage(@PathVariable("orderUuid") String orderUuid,Model model) {
		Orders order=this.ordersService.getOrderByUuid(orderUuid);
		model.addAttribute("appendClass", "order");
		if(order!=null)
			model.addAttribute("packages", this.orderPkgService.getOrderPackageByOrder(order));
		return "admin/order-packages";
	}
	@GetMapping("/transactions/{orderUuid}")
	public String ordersTransactions(@PathVariable("orderUuid") String orderUuid,Model model) {
		Orders order=this.ordersService.getOrderByUuid(orderUuid);
		model.addAttribute("appendClass", "order");
		if(order!=null)
			model.addAttribute("transactions", this.txnService.getOrdersTransaction(order.getOrderNo()));
		return "admin/order-transaction";
	}
}
