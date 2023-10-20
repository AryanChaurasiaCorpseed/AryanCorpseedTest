package com.corpseed.service.lifeatcorpseedservice;

import com.corpseed.entity.lifeentity.LifeVideo;

import java.util.List;

public interface LifeVideoService {
    void deleteLifeVideo(LifeVideo lifeVideo);

    LifeVideo findById(int id);

    LifeVideo update(LifeVideo lifeVideo);

    LifeVideo save(LifeVideo lifeVideo);

    List<LifeVideo> findAll();

    List<LifeVideo> findByDisplayStatus(String s);

    List<LifeVideo> findByDisplayStatusAndDeleteStatus(String s, int i);
}
