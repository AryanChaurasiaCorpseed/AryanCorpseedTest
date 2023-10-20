package com.corpseed.repository.lifeatcorpseedrepo;

import com.corpseed.entity.lifeentity.LifeCategory;
import com.corpseed.entity.lifeentity.LifeUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LifeUserRepo extends JpaRepository<LifeUser,Integer> {

    List<LifeUser> findByDisplayStatus(Pageable pageable, String displayStatus);

    List<LifeUser> findByLifeCategoriesIn(List<LifeCategory> catList, Pageable pageable);

    List<LifeUser> findTop4ByDisplayStatusAndDeleteStatusOrderByIdDesc(String s, int i);

    LifeUser findBySlug(String slug);

    LifeUser findBySlugAndIdNot(String slug, int id);

    List<LifeUser> findTop4ByLifeCategoriesAndDeleteStatusAndDisplayStatusOrderByIdDesc(LifeCategory lifeCategory,int i,String s);

    List<LifeUser> findTop4ByLifeCategoriesInAndDeleteStatusAndDisplayStatusOrderByIdDesc(List<LifeCategory> lifeCategories, int i, String s);

}
