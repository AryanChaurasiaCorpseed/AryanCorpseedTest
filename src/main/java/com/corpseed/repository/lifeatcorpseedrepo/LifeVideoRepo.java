package com.corpseed.repository.lifeatcorpseedrepo;

import com.corpseed.entity.lifeentity.LifeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LifeVideoRepo extends JpaRepository<LifeVideo,Integer> {

    List<LifeVideo> findByDisplayStatus(String displayStatus);

    List<LifeVideo> findByDisplayStatusAndDeleteStatus(String s, int i);
}
