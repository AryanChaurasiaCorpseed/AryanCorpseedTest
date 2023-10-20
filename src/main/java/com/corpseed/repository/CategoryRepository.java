package com.corpseed.repository;

import java.util.List;

import com.corpseed.entity.serviceentity.Services;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.corpseed.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByCategoryNameAndDeleteStatus(String categoryName,int deleteStatus);

	Category findByCategoryNameAndSubCategoryName(String category, String subCategoryName);

	Category findByCategoryNameAndId(String categoryName, long id);

	Category findByUuidAndDeleteStatus(String uuid,int dStatus);

	Category findByCategoryNameAndSubCategoryNameAndUuidNot(String categoryName, String subCategoryName, String uuid);

	List<Category> findByShowHomeStatusAndDisplayStatusAndDeleteStatus(String status,String displayStatus,int dStatus);

	Category findBySlugAndDisplayStatusAndDeleteStatus(String slug,String displayStatus,int dStatus);

	List<Category> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	List<Category> findByCategoryNameAndDisplayStatusAndDeleteStatus(String categoryName, String status,int dStatus);

	/*
	 * @Query("SELECT c FROM Category c where c.displayStatus=?1 GROUP BY c.categoryName ORDER BY c.id"
	 * ) List<Category> getCategoryByNameStatus(String status);
	 */
	
	List<Category> findByDeleteStatusOrderByIdDesc(int dStatus);

	List<Category> findDistinctCategoryNameByDisplayStatusAndDeleteStatus(String status,int dStatus);
	
	@Query("select DISTINCT c from Blogs b, Category c WHERE b.category=c and c.deleteStatus=2 order by b.visited desc")
	List<Category> findTop10ByVisited(PageRequest pageRequest);

	Category findBySlugAndDeleteStatus(String slug,int dStatus);

	List<Category> findTop4ByShowHomeStatusAndDisplayStatusAndDeleteStatus(String status1, String status2,int dStatus);

	Category findByIdAndDeleteStatus(long typeId, int dStatus);

    Category findByServices(Services service);
}
