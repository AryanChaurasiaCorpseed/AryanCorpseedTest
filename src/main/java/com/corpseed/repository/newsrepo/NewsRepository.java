package com.corpseed.repository.newsrepo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.newsentity.News;
import com.corpseed.entity.newsentity.NewsCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

	News findBySlug(String slug);

	News findByIdAndDeleteStatus(long id, int i);

	News findBySlugAndIdNot(String slug, long id);

	Page<News> findByNewsCategoryAndDisplayStatusAndDeleteStatus(NewsCategory category, String i, int j,
			Pageable pageable);

	Page<News> findByDisplayStatusAndDeleteStatus(String i, int j, Pageable pageable);

	List<News> findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String ds, int i);

	List<News> findByTitleContaining(String value);

	List<News> findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc(String ds, int i);

	List<News> findByDeleteStatusOrderByIdDesc(int i);


}
