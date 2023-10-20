package com.corpseed.repository.blogrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.blogentity.BlogFaq;
import com.corpseed.entity.blogentity.Blogs;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogFaqRepo extends JpaRepository<BlogFaq,Long> {

	List<BlogFaq> findByBlogsAndDeleteStatusAndDisplayStatus(Blogs blogs, int i,String dStatus);

	BlogFaq findByBlogsAndTitle(Blogs blog, String title);

	BlogFaq findByUuid(String faqUUID);

	BlogFaq findByTitleAndBlogsAndUuidNot(String title, Blogs blog, String uuid);

	BlogFaq findByIdAndDeleteStatus(long typeId, int i);

}
