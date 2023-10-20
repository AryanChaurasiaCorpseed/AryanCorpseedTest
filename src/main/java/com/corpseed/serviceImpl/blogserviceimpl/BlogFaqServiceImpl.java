package com.corpseed.serviceImpl.blogserviceimpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.blogentity.BlogFaq;
import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.repository.blogrepo.BlogFaqRepo;
import com.corpseed.service.BlogFaqService;

@Service
public class BlogFaqServiceImpl implements BlogFaqService {

	@Autowired
	private BlogFaqRepo blogFaqRepo;

	@Cacheable(value = "allBlogFaq")
	@Override
	public List<BlogFaq> allBlogFaq(Blogs blogs) {
		// TODO Auto-generated method stub
		return this.blogFaqRepo.findByBlogsAndDeleteStatusAndDisplayStatus(blogs,2,"1");
	}

	@Override
	public BlogFaq isBlogFaqExist(String title, Blogs blog) {
		// TODO Auto-generated method stub
		return this.blogFaqRepo.findByBlogsAndTitle(blog,title);
	}

	@CacheEvict(value = "allBlogFaq",allEntries = true)
	@Override
	public BlogFaq saveBlogFaq(@Valid BlogFaq faq) {
		// TODO Auto-generated method stub
		return this.blogFaqRepo.save(faq);
	}

	@Override
	public BlogFaq getBlogFaqByUUID(String faqUUID) {
		// TODO Auto-generated method stub
		return this.blogFaqRepo.findByUuid(faqUUID);
	}

	@Override
	public BlogFaq isEditBlogFaqExist(String title, Blogs blog, String uuid) {
		// TODO Auto-generated method stub
		return this.blogFaqRepo.findByTitleAndBlogsAndUuidNot(title,blog,uuid);
	}

	@Override
	public BlogFaq getBlogFaqByIdAndStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.blogFaqRepo.findByIdAndDeleteStatus(typeId,i);
	}

	@CacheEvict(value = "allBlogFaq",allEntries = true)
	@Override
	public void deleteBlogFaq(BlogFaq blogFaq) {
		this.blogFaqRepo.delete(blogFaq);
	}

}
