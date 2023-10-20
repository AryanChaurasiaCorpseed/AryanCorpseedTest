package com.corpseed.service.pressservice;

import com.corpseed.entity.pressentity.PressCategory;

import javax.validation.Valid;
import java.util.List;

public interface PressCategoryService {

    List<PressCategory> findAllByDeleteStatus(int dStatus);

    PressCategory findBySlugOrTitle(String slug,String title);

    PressCategory savePressCategory(@Valid PressCategory pressCategory);

    PressCategory findById(long id);

    PressCategory findPressCategoryBySlugOrTitleAndIdNot(String slug,String title, long id);

    PressCategory updatePressCategory(@Valid PressCategory pressCategory);

    List<PressCategory> findByDisplayStatusAndDeleteStatus(String i, int j);

    PressCategory findByIdAndDeleteStatus(long typeId, int i);

    void deletePressCategory(PressCategory pressCategory);

    PressCategory findBySlug(String slug);

    List<PressCategory> findTop10ByVisited();

}
