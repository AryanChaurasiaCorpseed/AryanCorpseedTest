package com.corpseed.serviceImpl.lifeatcorpseedserviceimpl;

import com.corpseed.entity.lifeentity.LifeCategory;
import com.corpseed.repository.lifeatcorpseedrepo.LifeCategoryRepo;
import com.corpseed.service.lifeatcorpseedservice.LifeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LifeCategoryServiceImpl implements LifeCategoryService {

    @Autowired
    private LifeCategoryRepo lifeCategoryRepo;


    @Override
    public LifeCategory findLifeCategoryBySlug(String slug) {
        return this.lifeCategoryRepo.findBySlug(slug);
    }

    @Override
    public List<LifeCategory> findAll() {
        return this.lifeCategoryRepo.findAll();
    }

    @CacheEvict(value = "lifeAllTags",allEntries = true)
    @Override
    public LifeCategory save(LifeCategory lifeCategory) {
        return this.lifeCategoryRepo.save(lifeCategory);
    }

    @Override
    public LifeCategory findLifeCategoryBySlugAndIdNot(String slug, int id) {
        return this.lifeCategoryRepo.findBySlugAndIdNot(slug,id);
    }

    @CacheEvict(value = "lifeAllTags",allEntries = true)
    @Override
    public LifeCategory update(LifeCategory lifeCategory) {
        return this.lifeCategoryRepo.save(lifeCategory);
    }

    @Override
    public LifeCategory findById(int id) {
        return this.lifeCategoryRepo.findById(id).orElse(null);
    }

    @CacheEvict(value = "lifeAllTags",allEntries = true)
    @Override
    public void deleteLifeCategory(LifeCategory lifeCategory) {
        this.lifeCategoryRepo.delete(lifeCategory);
    }

    @Cacheable(value = "lifeAllTags")
    @Override
    public List<LifeCategory> findByDisplayStatusAndDeleteStatus(String displayStatus,int deleteStatus) {
        return this.lifeCategoryRepo.findByDisplayStatusAndDeleteStatus(displayStatus,deleteStatus);
    }

    @Override
    public List<LifeCategory> findByDisplayStatus(String s) {
        return this.lifeCategoryRepo.findByDisplayStatus(s);
    }

//    @Override
//    public List<LifeCategory> findByDisplayStatusAndLifeCategoryListNotIn(String displayStatus, List<LifeCategory> lifeCategories) {
//        return this.lifeCategoryRepo.findByDisplayStatusAndLifeCategoryListNotIn(displayStatus,lifeCategories);
//    }
}
