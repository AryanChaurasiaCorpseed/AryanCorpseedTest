package com.corpseed.repository.lifeatcorpseedrepo;

import com.corpseed.entity.lifeentity.LifeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LifeCategoryRepo extends JpaRepository<LifeCategory,Integer> {

    LifeCategory findBySlug(String slug);

    LifeCategory findBySlugAndIdNot(String slug, int id);

    List<LifeCategory> findByDisplayStatusAndDeleteStatus(String displayStatus,int deleteStatus);

//    List<LifeCategory> findByDisplayStatusAndLifeCategoryListNotIn(String displayStatus, List<LifeCategory> lifeCategories);

    List<LifeCategory> findByDisplayStatus(String s);
}
