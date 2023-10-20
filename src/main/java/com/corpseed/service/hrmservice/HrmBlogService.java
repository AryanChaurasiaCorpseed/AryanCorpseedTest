package com.corpseed.service.hrmservice;

import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.hrmentity.HrmBlog;
import com.corpseed.entity.User;

public interface HrmBlogService {

	public List<HrmBlog> allBlogs(int status);

	public HrmBlog saveBlog(@Valid HrmBlog blog);

	public HrmBlog findBySlug(String slug);

	public HrmBlog findByUuid(String uuid);

	public HrmBlog findByIdAndDeleteStatus(long typeId, int i);

	public void deleteBlog(HrmBlog blogs);

	public HrmBlog findBySlugAndUuidNot(String slug, String uuid);

	public List<HrmBlog> allBlogs();

	public List<HrmBlog> allBlogsByDeleteStatus(int i);

	public List<HrmBlog> findBlogsByUser(User saveUser);

	public void updateAll(List<HrmBlog> hrmBlogs);

	public List<HrmBlog> findTop8ByDisplayStatusAndUuidNotOrderByIdDesc(int string, String uuid);

	List<HrmBlog> findTop6ByDisplayStatusAndDeleteStatus(int s, int i);
}
