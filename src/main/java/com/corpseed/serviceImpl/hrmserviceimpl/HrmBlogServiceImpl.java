package com.corpseed.serviceImpl.hrmserviceimpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.hrmentity.HrmBlog;
import com.corpseed.entity.User;
import com.corpseed.repository.hrmrepo.HrmBlogRepo;
import com.corpseed.service.hrmservice.HrmBlogService;
@Service
public class HrmBlogServiceImpl implements HrmBlogService {

	@Autowired
	private HrmBlogRepo hrmBlogRepo;
	
	@Override
	public List<HrmBlog> allBlogs(int status) {
		// TODO Auto-generated method stub
		return this.hrmBlogRepo.findByDisplayStatusAndDeleteStatus(status,2);
	}

	@Override
	public HrmBlog saveBlog(@Valid HrmBlog blog) {
		// TODO Auto-generated method stub
		return this.hrmBlogRepo.save(blog);
	}

	@Override
	public HrmBlog findBySlug(String slug) {
		// TODO Auto-generated method stub
		return this.hrmBlogRepo.findBySlugAndDeleteStatus(slug,2);
	}

	@Override
	public HrmBlog findByUuid(String uuid) {
		// TODO Auto-generated method stub
		return this.hrmBlogRepo.findByUuidAndDeleteStatus(uuid,2);
	}

	@Override
	public HrmBlog findByIdAndDeleteStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.hrmBlogRepo.findByIdAndDeleteStatus(typeId,i);
	} 

	@Override
	public void deleteBlog(HrmBlog blogs) {
		this.hrmBlogRepo.delete(blogs);
	}

	@Override
	public HrmBlog findBySlugAndUuidNot(String slug, String uuid) {
		// TODO Auto-generated method stub
		return this.hrmBlogRepo.findBySlugAndUuidNot(slug, uuid);
	}

	@Override
	public List<HrmBlog> allBlogs() {
		// TODO Auto-generated method stub
		return this.hrmBlogRepo.findAll();
	}

	@Override
	public List<HrmBlog> allBlogsByDeleteStatus(int i) {
		// TODO Auto-generated method stub
		return this.hrmBlogRepo.findByDeleteStatus(i);
	}

	@Override
	public List<HrmBlog> findBlogsByUser(User saveUser) {
		// TODO Auto-generated method stub
		return this.hrmBlogRepo.findByUser(saveUser);
	}

	@Override
	public void updateAll(List<HrmBlog> hrmBlogs) {
		this.hrmBlogRepo.saveAll(hrmBlogs);
	}

	@Override
	public List<HrmBlog> findTop8ByDisplayStatusAndUuidNotOrderByIdDesc(int status, String uuid) {
		// TODO Auto-generated method stub
		return this.hrmBlogRepo.findTop8ByDisplayStatusAndUuidNotOrderByIdDesc(status, uuid);
	}

	@Override
	public List<HrmBlog> findTop6ByDisplayStatusAndDeleteStatus(int s, int i) {
		return this.hrmBlogRepo.findTop6ByDisplayStatusAndDeleteStatusOrderByIdDesc(s,i);
	}

}
