package com.corpseed.serviceImpl.servicesserviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.serviceentity.ServiceBlogs;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.repository.servicerepo.ServicesBlogRepository;

@Service
public class ServicesBlogsService {

	@Autowired
	private ServicesBlogRepository servicesBlogRepository;

	public List<ServiceBlogs> saveServiceBlogs(List<ServiceBlogs> serviceBlogList) {
		return this.servicesBlogRepository.saveAll(serviceBlogList);
	}

	public ServiceBlogs findByBlogsAndServices(Blogs findBlog, Services serviceById) {
		return this.servicesBlogRepository.findByBlogsAndServicesAndDeleteStatus(findBlog, serviceById,2);
	}

	public List<ServiceBlogs> findByBlogsAndServicesNotIn(Blogs saveBlog, List<Services> servicesList) {
		return this.servicesBlogRepository.findByBlogsAndDeleteStatusAndServicesNotIn(saveBlog,2,servicesList);
	}

	public void deleteById(long id) {
		this.servicesBlogRepository.deleteById(id);
	}

	public void deleteAlls(List<ServiceBlogs> allServiceBlogs) {
		this.servicesBlogRepository.deleteInBatch(allServiceBlogs);
	}

	public List<ServiceBlogs> findByBlogs(Blogs saveBlog) {
		// TODO Auto-generated method stub
		return this.servicesBlogRepository.findByBlogs(saveBlog);
	}
}
