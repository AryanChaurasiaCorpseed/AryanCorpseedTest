package com.corpseed.service.lifeatcorpseedservice;

import com.corpseed.entity.lifeentity.LifeCategory;

import java.util.List;

public interface LifeCategoryService {

    LifeCategory findLifeCategoryBySlug(String slug);

    List<LifeCategory> findAll();

    LifeCategory save(LifeCategory lifeCategory);

    LifeCategory findLifeCategoryBySlugAndIdNot(String slug, int id);

    LifeCategory update(LifeCategory lifeCategory);

    LifeCategory findById(int id);

    void deleteLifeCategory(LifeCategory lifeCategory);

    List<LifeCategory> findByDisplayStatusAndDeleteStatus(String s,int dStatus);

    List<LifeCategory> findByDisplayStatus(String s);

//    List<LifeCategory> findByDisplayStatusAndLifeCategoryListNotIn(String s, List<LifeCategory> lifeCategories);
}
