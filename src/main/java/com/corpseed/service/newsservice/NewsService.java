package com.corpseed.service.newsservice;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.corpseed.entity.newsentity.News;
import com.corpseed.entity.newsentity.NewsCategory;

public interface NewsService {

	List<News> findAll();

	void saveAllNews(List<News> news);

	News saveNews(@Valid News news);

	News findBySlug(String slug);

	News findById(long id);

	News findBySlugAndIdNot(String slug, long id);

	News findByIdAndDeleteStatus(long typeId, int i);

	void deleteNews(News news);

	Page<News> findByCategoryAndDisplayStatusAndDeleteStatus(NewsCategory category, String i, int j, Pageable pageable);

	Page<News> findByDisplayStatusAndDeleteStatus(String i, int j, Pageable pageable);

	List<News> findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String string, int i);

	List<News> findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc(String string, int i);

	List<News> findByTitleContaining(String value);

	void updateNewsRssFeed();
}
