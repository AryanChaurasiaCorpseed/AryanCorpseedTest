package com.corpseed.service.lifeatcorpseedservice;

import com.corpseed.entity.lifeentity.LifeCategory;
import com.corpseed.entity.lifeentity.LifeUser;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LifeUserService {
    List<LifeUser> findLifeUserByDisplayStatus(Pageable pageable, String displayStatus);

    List<LifeUser> findByLifeCategoryListIn(Pageable pageable, List<LifeCategory> asList);

    List<LifeUser> findAll();

    LifeUser findById(int id);

    LifeUser save(LifeUser lifeUser);

    LifeUser update(LifeUser lifeUser);

    void deleteLifeUser(LifeUser lifeUser);

    List<LifeUser> findTop4LifeUserByDisplayStatusAndDeleteStatus(String s, int i);

    LifeUser findBySlug(String slug);

    LifeUser findBySlugAndIdNot(String slug, int id);

    List<LifeUser> findTop4ByLifeCategoriesAndDeleteStatusAndDisplayStatus(LifeCategory lifeCategory, String s, int i);

    List<LifeUser> findTop4ByLifeCategoriesInAndDeleteStatusAndDisplayStatus(List<LifeCategory> lifeCategories, String s, int i);
}
