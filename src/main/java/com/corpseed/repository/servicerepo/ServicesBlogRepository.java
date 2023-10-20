package com.corpseed.repository.servicerepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.serviceentity.ServiceBlogs;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesBlogRepository extends JpaRepository<ServiceBlogs, Long> {

	ServiceBlogs findByBlogsAndServicesAndDeleteStatus(Blogs findBlog, Services serviceById,int dStatus);

	List<ServiceBlogs> findByBlogsAndDeleteStatus(Blogs saveBlog,int dStatus);

	List<ServiceBlogs> findByBlogsAndDeleteStatusAndServicesNotIn(Blogs saveBlog,int dStatus, List<Services> servicesList);

	List<ServiceBlogs> findByBlogs(Blogs saveBlog);

}
