package com.corpseed.service.newsservice;

import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.newsentity.NewsCategory;

public interface NewsCategoryService {

	List<NewsCategory> findAll();

	NewsCategory findNewsCategoryByTitle(String title);

	NewsCategory saveNewsCategory(@Valid NewsCategory newsCategory);

	NewsCategory findById(long id);

	NewsCategory findNewsCategoryByTitleAndIdNot(String title, long id);

	NewsCategory updateNewsCategory(@Valid NewsCategory newsCategory);

	List<NewsCategory> findByDisplayStatusAndDeleteStatus(String i, int j);

	NewsCategory findByIdAndDeleteStatus(long typeId, int i);

	void deleteNewsCategory(NewsCategory newsCategory);

	NewsCategory findBySlug(String slug);

	List<NewsCategory> findTop10ByVisited();

}
