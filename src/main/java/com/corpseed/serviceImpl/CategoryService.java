package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.serviceentity.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.corpseed.entity.Category;
import com.corpseed.entity.User;
import com.corpseed.repository.CategoryRepository;

@Service("categoryService")
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository; 
	  	  
	@Caching(evict = {
		    @CacheEvict(value = "categoryByUuid", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true),
		    @CacheEvict(value = "categoryBySlug", allEntries = true),
		    @CacheEvict(value = "categoryByStatus", allEntries = true),
		    @CacheEvict(value = "categoryDt", allEntries = true),
		    @CacheEvict(value = "categoryByNameAndStatus", allEntries = true),
		    @CacheEvict(value = "top10Category", allEntries = true),
		    @CacheEvict(value = "findBlogBySlug", allEntries = true),
		    @CacheEvict(value = "cardCategory", allEntries = true),
			@CacheEvict(value = "categoryByService",allEntries = true)
		}) 
	public Category saveCategory(Category category,User user) {
		
		if(category.getRatingUser()==null||category.getRatingUser().length()<=0)category.setRatingUser("0");
		if(category.getRatingValue()==null||category.getRatingValue().length()<=0)category.setRatingValue("0");
		
		if(user!=null) {
			category.setAddedByUUID(user.getUuid());
		}		
		Category catSave = this.categoryRepository.save(category);
		
		return catSave;
	}
	
	public List<Category> getAllSubCategory(String categoryName) {
		return this.categoryRepository.findByCategoryNameAndDeleteStatus(categoryName,2);
	}
	
	public Category isSubCategoryExist(String category,String subCategoryName) {
		return this.categoryRepository.findByCategoryNameAndSubCategoryName(category,subCategoryName); 
	}
	
	public List<Category> getAllSubCategory(){
		return this.categoryRepository.findByDeleteStatusOrderByIdDesc(2);
	}
	
	public Category getSubCategory(String categoryName, String subCategoryID) {
		// TODO Auto-generated method stub
		return this.categoryRepository.findByCategoryNameAndId(categoryName,Long.parseLong(subCategoryID));
	}

	@Cacheable(value="categoryByUuid",key="#uuid")
	public Category findByCategoryUUID(String uuid) {
		return this.categoryRepository.findByUuidAndDeleteStatus(uuid,2);
	}

	@Caching(evict = {
		    @CacheEvict(value = "categoryByUuid", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true),
		    @CacheEvict(value = "categoryBySlug", allEntries = true),
		    @CacheEvict(value = "categoryByStatus", allEntries = true),
		    @CacheEvict(value = "categoryDt", allEntries = true),
		    @CacheEvict(value = "categoryByNameAndStatus", allEntries = true),
		    @CacheEvict(value = "top10Category", allEntries = true),
		    @CacheEvict(value = "findBlogBySlug", allEntries = true),
		    @CacheEvict(value = "cardCategory", allEntries = true),
			@CacheEvict(value = "categoryByService",allEntries = true)
		})
	public Category updateCategory(@Valid Category category) {
		return this.categoryRepository.save(category);
	}

	public Category findSubCategoryExceptOne(String categoryName, String subCategoryName, String uuid) {
		return this.categoryRepository.findByCategoryNameAndSubCategoryNameAndUuidNot(categoryName,subCategoryName,uuid);
	}
	@Caching(evict = {
		    @CacheEvict(value = "categoryByUuid", allEntries = true),
		    @CacheEvict(value = "allMenuCategory", allEntries = true),
		    @CacheEvict(value = "categoryBySlug", allEntries = true),
		    @CacheEvict(value = "categoryByStatus", allEntries = true),
		    @CacheEvict(value = "categoryDt", allEntries = true),
		    @CacheEvict(value = "categoryByNameAndStatus", allEntries = true),
		    @CacheEvict(value = "top10Category", allEntries = true),
		    @CacheEvict(value = "findBlogBySlug", allEntries = true),
		    @CacheEvict(value = "cardCategory", allEntries = true),
			@CacheEvict(value = "categoryByService",allEntries = true)
		})
	public void deleteCategory(Category category) {
		this.categoryRepository.delete(category);
	}
	@Cacheable(value="allMenuCategory",key="#categoryName")
	public List<Category> getCategoryByCategoryNameAndDisplayStatus(String categoryName) {
		return this.categoryRepository.findByCategoryNameAndDisplayStatusAndDeleteStatus(categoryName,"1",2);
	}

	public List<Category> getAllHomeSubCategory() {
		return this.categoryRepository.findByShowHomeStatusAndDisplayStatusAndDeleteStatus("1","1",2);
	}

	@Cacheable(value="categoryBySlug",key="#slug")
	public Category findBySlugAndDisplayStatus(String slug,String status) {
		return this.categoryRepository.findBySlugAndDisplayStatusAndDeleteStatus(slug,status,2);
	}

	@Cacheable("categoryByStatus")
	public List<Category> findByDisplayStatus(String status) {
		return this.categoryRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}
	
	@Cacheable("categoryDt")
	public List<Category> findByCategoryNameAndDisplayStatus(String categoryName, String status) {
		return this.categoryRepository.findByCategoryNameAndDisplayStatusAndDeleteStatus(categoryName, status,2);
	}

	@Cacheable("categoryByNameAndStatus")
	public List<Category> getCategoryByNameAndStatus(String status) {
		return this.categoryRepository.findDistinctCategoryNameByDisplayStatusAndDeleteStatus(status,2);
	}

	public Category findByCategoryNameAndId(String categoryName, String subCategoryID) {
		return this.categoryRepository.findByCategoryNameAndId(categoryName, Long.parseLong(subCategoryID));
	}

	@Cacheable("top10Category")
	public List<Category> findTop10ByVisited() {
		// TODO Auto-generated method stub
		return this.categoryRepository.findTop10ByVisited(PageRequest.of(0,10));
	}
	
	@Cacheable(value = "findBlogBySlug",key = "#slug")
	public Category findBySlug(String slug) {
		// TODO Auto-generated method stub
		return this.categoryRepository.findBySlugAndDeleteStatus(slug,2);
	}

	@Cacheable("cardCategory")
	public List<Category> findTop4ByShowHomeStatusAndDisplayStatus(String status1,String status2) {
		// TODO Auto-generated method stub
		return this.categoryRepository.findTop4ByShowHomeStatusAndDisplayStatusAndDeleteStatus(status1, status2,2);
	}

	public Category findByIdAndDeleteStatus(long typeId, int dStatus) {
		// TODO Auto-generated method stub
		return this.categoryRepository.findByIdAndDeleteStatus(typeId,dStatus);
	}

	@Cacheable("categoryByService")
	public Category findByService(Services service) {
		return this.categoryRepository.findByServices(service);
	}
}
