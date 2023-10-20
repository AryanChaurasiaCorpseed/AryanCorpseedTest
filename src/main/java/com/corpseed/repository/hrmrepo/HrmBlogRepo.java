package com.corpseed.repository.hrmrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.hrmentity.HrmBlog;
import com.corpseed.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface HrmBlogRepo extends JpaRepository<HrmBlog, Long> {

	List<HrmBlog> findByDeleteStatus(int i);

	HrmBlog findBySlugAndDeleteStatus(String slug, int i);

	HrmBlog findByUuidAndDeleteStatus(String uuid, int i);

	HrmBlog findByIdAndDeleteStatus(long typeId, int i);

	HrmBlog findBySlugAndUuidNot(String slug, String uuid);

	List<HrmBlog> findByDisplayStatusAndDeleteStatus(int status, int i);

	List<HrmBlog> findByUser(User saveUser);

	List<HrmBlog> findTop8ByDisplayStatusAndUuidNotOrderByIdDesc(int status, String uuid);

    List<HrmBlog> findTop6ByDisplayStatusAndDeleteStatusOrderByIdDesc(int s, int i);
}
