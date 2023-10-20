package com.corpseed.repository.newsrepo;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corpseed.entity.newsentity.NewsCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsCategoryRepo extends JpaRepository<NewsCategory, Long> {

	List<NewsCategory> findByDeleteStatus(int i);

	NewsCategory findByTitle(String title);

	NewsCategory findByIdAndDeleteStatus(long id, int i);

	NewsCategory findByTitleAndIdNot(String title, long id);

	List<NewsCategory> findByDisplayStatusAndDeleteStatus(String i, int j);

	NewsCategory findBySlug(String slug);

	@Query("SELECT DISTINCT c from News b, NewsCategory c WHERE b.newsCategory=c and c.deleteStatus=2 order by b.visited desc")
	List<NewsCategory> findTop10ByVisited(PageRequest pageRequest);

}
