package com.corpseed.serviceImpl.lifeatcorpseedserviceimpl;

import com.corpseed.entity.lifeentity.LifeVideo;
import com.corpseed.repository.lifeatcorpseedrepo.LifeVideoRepo;
import com.corpseed.service.lifeatcorpseedservice.LifeVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LifeVideoServiceImpl implements LifeVideoService {

    @Autowired
    private LifeVideoRepo lifeVideoRepo;

    @CacheEvict(value = "lifeVideo",allEntries = true)
    @Override
    public void deleteLifeVideo(LifeVideo lifeVideo) {
        this.lifeVideoRepo.delete(lifeVideo);
    }

    @Override
    public LifeVideo findById(int id) {
        return this.lifeVideoRepo.findById(id).orElse(null);
    }

    @CacheEvict(value = "lifeVideo",allEntries = true)
    @Override
    public LifeVideo update(LifeVideo lifeVideo) {
        return this.lifeVideoRepo.save(lifeVideo);
    }

    @CacheEvict(value = "lifeVideo",allEntries = true)
    @Override
    public LifeVideo save(LifeVideo lifeVideo) {
        return this.lifeVideoRepo.save(lifeVideo);
    }

    @Override
    public List<LifeVideo> findAll() {
        return this.lifeVideoRepo.findAll();
    }

    @Cacheable(value = "lifeVideo")
    @Override
    public List<LifeVideo> findByDisplayStatus(String displayStatus) {
        return this.lifeVideoRepo.findByDisplayStatus(displayStatus);
    }

    @Override
    public List<LifeVideo> findByDisplayStatusAndDeleteStatus(String s, int i) {
        return this.lifeVideoRepo.findByDisplayStatusAndDeleteStatus(s,i);
    }
}
