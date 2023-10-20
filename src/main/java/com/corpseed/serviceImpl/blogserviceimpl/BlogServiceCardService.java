package com.corpseed.serviceImpl.blogserviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.blogentity.BlogServiceCardList;
import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.serviceentity.Services;
import com.corpseed.repository.blogrepo.BlogServiceCardRepo;

@Service
public class BlogServiceCardService {

	@Autowired
	private BlogServiceCardRepo blogServiceCardRepo;

	@CacheEvict(value = "blogServiceCardList",allEntries = true)
	public List<BlogServiceCardList> saveAll(List<BlogServiceCardList> blogServiceCardList) {
		return this.blogServiceCardRepo.saveAll(blogServiceCardList);
	}

	public BlogServiceCardList findByBlogsAndService(Blogs saveBlog, Services eservice) {
		// TODO Auto-generated method stub
		return this.blogServiceCardRepo.findByBlogsAndServiceAndDeleteStatus(saveBlog, eservice,2);
	}

	@CacheEvict(value = "blogServiceCardList",allEntries = true)
	public void removeAll(List<BlogServiceCardList> allBlogServiceCard) {
		this.blogServiceCardRepo.deleteInBatch(allBlogServiceCard);
	}

	public List<BlogServiceCardList> findByBlogs(Blogs saveBlog) {
		// TODO Auto-generated method stub
		return this.blogServiceCardRepo.findByBlogs(saveBlog);
	}

	@Cacheable(value = "blogServiceCardList")
    public List<BlogServiceCardList> findBlogServiceCardByBlog(Blogs blog) {
		return this.blogServiceCardRepo.findByBlogsAndDeleteStatus(blog,2);
    }
}
