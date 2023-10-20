package com.corpseed.repository.blogrepo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blogs, Long> {

	Blogs findByUuidAndDeleteStatus(String blogUUID,int dStatus);

	Blogs findBySlugAndDeleteStatus(String slug,int dStatus);

	List<Blogs> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	List<Blogs> findByTitleAndDeleteStatus(String value,int dStatus);

	List<Blogs> findTop5ByDeleteStatusOrderByIdDesc(int dStatus);

	List<Blogs> findTop6ByDeleteStatusOrderByIdDesc(int dStatus);

	Blogs findBySlugAndDeleteStatusAndUuidNot(String slug,int dStatus, String uuid);

	List<Blogs> findByDeleteStatusOrderByIdDesc(int dStatus);

	List<Blogs> findTop8ByCategoryAndDisplayStatusAndDeleteStatusAndUuidNotOrderByIdDesc(Category category, String status,int dStatus, String uuid);

	Page<Blogs> findByDisplayStatusAndDeleteStatus(String string,int dStatus, Pageable pageable);

	Page<Blogs> findByCategoryAndDisplayStatusAndDeleteStatus(Category category,String status,int dStatus,Pageable pageable);

	Blogs findBySlugOrTitle(String slug, String title);

	@Query("select b from Blogs b where (b.slug= :slug or b.title= :title) and b.uuid!= :uuid")
	Blogs findBySlugOrTitleAndUuidNot(@Param("slug") String slug, @Param("title") String title, @Param("uuid") String uuid);

	Blogs findByIdAndDeleteStatus(long id, int dStatus);

	@Query(nativeQuery = true,value="SELECT * FROM Blogs where " +
			"display_status= ?1 and delete_status= ?2 order by visited desc limit 5")
	List<Blogs> findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String string, int i);

	List<Blogs> findByPostedByUuidAndDeleteStatus(String uuid, int deleteStatus);

	@Query(nativeQuery = true,value="SELECT * FROM Blogs where " +
			"display_status= ?1 and delete_status= ?3 and " +
			"(title like %?2% or summary like %?2% or meta_description like %?2% or " +
			"meta_title like %?2% or meta_keyword like %?2% or slug like %?2% or " +
			"image like %?2% or description like %?2%) order by visited desc limit 5")
	List<Blogs> findTop5ByDisplayStatusAndTitleContainingIgnoreCaseAndDeleteStatus(String status,String value, int i);

	List<Blogs> findByDeleteStatusAndDisplayStatusAndTitleIgnoreCaseContaining(int i, String status, String value);

	List<Blogs> findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc(String ds, int i);

	List<Blogs> findTop5ByDisplayStatusAndDeleteStatusAndPostedByUuidOrderByIdDesc(String ds, int d, String uuid);

	Page<Blogs> findByDisplayStatusAndDeleteStatusAndPostedByUuid(String s, int i, Pageable pageable, String uuid);

	Page<Blogs> findByCategoryAndDisplayStatusAndDeleteStatusAndPostedByUuid(Category category, String s, int i, Pageable pageable, String uuid);

	List<Blogs> findTop10ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String displayStatus, int deleteStatus);
}
