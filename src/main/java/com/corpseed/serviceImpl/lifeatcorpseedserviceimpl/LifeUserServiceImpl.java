package com.corpseed.serviceImpl.lifeatcorpseedserviceimpl;

import com.corpseed.entity.lifeentity.LifeCategory;
import com.corpseed.entity.lifeentity.LifeUser;
import com.corpseed.repository.lifeatcorpseedrepo.LifeUserRepo;
import com.corpseed.service.lifeatcorpseedservice.LifeUserService;
import com.corpseed.serviceImpl.AzureBlobAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LifeUserServiceImpl implements LifeUserService {

    @Autowired
    private LifeUserRepo lifeUserRepo;

    @Autowired
    private AzureBlobAdapter azureBlobAdapter;

    @Cacheable(value = "lifeUser")
    @Override
    public List<LifeUser> findLifeUserByDisplayStatus(Pageable pageable, String displayStatus) {
        return this.lifeUserRepo.findByDisplayStatus(pageable,displayStatus);
    }

    @Cacheable(value = "lifeCategoryIn")
    @Override
    public List<LifeUser> findByLifeCategoryListIn(Pageable pageable, List<LifeCategory> catList) {
        return lifeUserRepo.findByLifeCategoriesIn(catList, pageable);
    }

    @Override
    public List<LifeUser> findAll() {
        return this.lifeUserRepo.findAll();
    }

    @Override
    public LifeUser findById(int id) {
        return this.lifeUserRepo.findById(id).orElse(null);
    }

    @Caching(evict = {
        @CacheEvict(value = "lifeUser",allEntries = true),
            @CacheEvict(value = "lifeCategoryIn",allEntries = true)
    })
    @Override
    public LifeUser save(LifeUser lifeUser) {
        return this.lifeUserRepo.save(lifeUser);
    }

    @Caching(evict = {
            @CacheEvict(value = "lifeUser",allEntries = true),
            @CacheEvict(value = "lifeCategoryIn",allEntries = true)
    })
    @Override
    public LifeUser update(LifeUser lifeUser) {
        return this.lifeUserRepo.save(lifeUser);
    }

    @Caching(evict = {
            @CacheEvict(value = "lifeUser",allEntries = true),
            @CacheEvict(value = "lifeCategoryIn",allEntries = true)
    })
    @Override
    public void deleteLifeUser(LifeUser lifeUser) {
        if(lifeUser.getPictureName()!=null)
            this.azureBlobAdapter.deleteFile(lifeUser.getPictureName());
        this.lifeUserRepo.delete(lifeUser);
    }

    @Override
    public List<LifeUser> findTop4LifeUserByDisplayStatusAndDeleteStatus(String s, int i) {
        return this.lifeUserRepo.findTop4ByDisplayStatusAndDeleteStatusOrderByIdDesc(s,i);
    }

    @Override
    public LifeUser findBySlug(String slug) {
        return this.lifeUserRepo.findBySlug(slug);
    }

    @Override
    public LifeUser findBySlugAndIdNot(String slug, int id) {
        return this.lifeUserRepo.findBySlugAndIdNot(slug,id);
    }

    @Override
    public List<LifeUser> findTop4ByLifeCategoriesAndDeleteStatusAndDisplayStatus(LifeCategory lifeCategory, String s, int i) {
        return this.lifeUserRepo.findTop4ByLifeCategoriesAndDeleteStatusAndDisplayStatusOrderByIdDesc(lifeCategory,i,s);
    }

    @Override
    public List<LifeUser> findTop4ByLifeCategoriesInAndDeleteStatusAndDisplayStatus(List<LifeCategory> lifeCategories, String s, int i) {
        return this.lifeUserRepo.findTop4ByLifeCategoriesInAndDeleteStatusAndDisplayStatusOrderByIdDesc(lifeCategories,i,s);
    }
}
