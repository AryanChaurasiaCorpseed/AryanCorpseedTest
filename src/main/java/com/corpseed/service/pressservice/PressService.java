package com.corpseed.service.pressservice;

import javax.validation.Valid;

import com.corpseed.entity.pressentity.Press;
import com.corpseed.entity.pressentity.PressCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PressService {

    List<Press> findAll();

    void saveAllPress(List<Press> Press);

    Press savePress(@Valid Press Press);

     Press findBySlug(String slug);

    Press findById(long id);

    Press findBySlugAndIdNot(String slug, long id);

    Press findByIdAndDeleteStatus(long typeId, int i);

    void deletePress(Press Press);

    Page<Press> findByCategoryAndDisplayStatusAndDeleteStatus(PressCategory category, String i, int j, Pageable pageable);

    Page<Press> findByDisplayStatusAndDeleteStatus(String i, int j, Pageable pageable);

    List<Press> findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String string, int i);

    List<Press> findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc(String string, int i);

    List<Press> findByTitleContainingAndDeleteStatus(String value,int dStatus);

    void updatePressRssFeed();
}
