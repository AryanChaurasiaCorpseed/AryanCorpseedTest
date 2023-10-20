package com.corpseed.service.servicesservice;

import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.serviceentity.ServiceBrands;
import com.corpseed.entity.serviceentity.Services;

public interface ServiceBrand {
	public ServiceBrands saveServiceBrand(@Valid ServiceBrands brand);

	public ServiceBrands getServiceBrand(String uuid,int dStatus);

	public ServiceBrands getServiceBrandByIdAndStatus(long typeId, int dStatus);

	public void deleteServiceBrand(ServiceBrands brand);

	public List<ServiceBrands> findByServicesAndDeleteStatus(Services service, int dStatus);
}
