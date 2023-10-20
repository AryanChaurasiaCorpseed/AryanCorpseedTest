package com.corpseed.serviceImpl.newsserviceimpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.corpseed.entity.newsentity.NewsCategory;
import com.corpseed.repository.newsrepo.NewsCategoryRepo;
import com.corpseed.service.newsservice.NewsCategoryService;
@Service
public class NewsCategoryServiceImpl implements NewsCategoryService {

	@Autowired
	private NewsCategoryRepo newsCategoryRepo;
	
	@Override
	public List<NewsCategory> findAll() {
		// TODO Auto-generated method stub
		return this.newsCategoryRepo.findByDeleteStatus(2);
	}

	@Override
	public NewsCategory findNewsCategoryByTitle(String title) {
		// TODO Auto-generated method stub
		return this.newsCategoryRepo.findByTitle(title);
	}

	@Override
	public NewsCategory saveNewsCategory(@Valid NewsCategory newsCategory) {
		// TODO Auto-generated method stub
		return this.newsCategoryRepo.save(newsCategory);
	}

	@Override
	public NewsCategory findById(long id) {
		// TODO Auto-generated method stub
		return this.newsCategoryRepo.findByIdAndDeleteStatus(id,2);
	}

	@Override
	public NewsCategory findNewsCategoryByTitleAndIdNot(String title, long id) {
		// TODO Auto-generated method stub
		return this.newsCategoryRepo.findByTitleAndIdNot(title,id);
	}

	@Override
	public NewsCategory updateNewsCategory(@Valid NewsCategory newsCategory) {
		// TODO Auto-generated method stub
		return this.newsCategoryRepo.save(newsCategory);
	}

	@Override
	public List<NewsCategory> findByDisplayStatusAndDeleteStatus(String i, int j) {
		// TODO Auto-generated method stub
		return this.newsCategoryRepo.findByDisplayStatusAndDeleteStatus(i,j);
	}

	@Override
	public NewsCategory findByIdAndDeleteStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.newsCategoryRepo.findByIdAndDeleteStatus(typeId, i);
	}

	@Override
	public void deleteNewsCategory(NewsCategory newsCategory) {
		// TODO Auto-generated method stub
		this.newsCategoryRepo.delete(newsCategory);
	}

	@Override
	public NewsCategory findBySlug(String slug) {
		// TODO Auto-generated method stub
		return this.newsCategoryRepo.findBySlug(slug);
	}

	@Override
	public List<NewsCategory> findTop10ByVisited() {
		// TODO Auto-generated method stub
		return this.newsCategoryRepo.findTop10ByVisited(PageRequest.of(0,10));
	}

}
