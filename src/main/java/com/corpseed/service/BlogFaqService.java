package com.corpseed.service;

import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.blogentity.BlogFaq;
import com.corpseed.entity.blogentity.Blogs;

public interface BlogFaqService {

	public List<BlogFaq> allBlogFaq(Blogs blogs);

	public BlogFaq isBlogFaqExist(String title, Blogs blog);

	public BlogFaq saveBlogFaq(@Valid BlogFaq faq);

	public BlogFaq getBlogFaqByUUID(String faqUUID);

	public BlogFaq isEditBlogFaqExist(String title, Blogs blog, String uuid);

	public BlogFaq getBlogFaqByIdAndStatus(long typeId, int i);

	public void deleteBlogFaq(BlogFaq blogFaq);
}
