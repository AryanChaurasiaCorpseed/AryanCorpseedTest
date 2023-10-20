package com.corpseed.repository.blogrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.blogentity.BlogServiceCardList;
import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogServiceCardRepo extends JpaRepository<BlogServiceCardList, Long> {

	BlogServiceCardList findByBlogsAndServiceAndDeleteStatus(Blogs saveBlog, Services eservice,int dStatus);

	List<BlogServiceCardList> findByBlogs(Blogs saveBlog);

	List<BlogServiceCardList> findByBlogsAndDeleteStatus(Blogs blog, int i);
}
