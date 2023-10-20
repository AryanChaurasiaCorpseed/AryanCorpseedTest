package com.corpseed.serviceImpl.pressserviceimpl;

import javax.validation.Valid;

import com.corpseed.entity.pressentity.PressCategory;
import com.corpseed.repository.pressrepo.PressCategoryRepo;
import com.corpseed.service.pressservice.PressCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PressCategoryServiceImpl implements PressCategoryService {

    @Autowired
    private PressCategoryRepo pressCategoryRepo;

    @Override
    public List<PressCategory> findAllByDeleteStatus(int dStatus) {
        // TODO Auto-generated method stub
        return this.pressCategoryRepo.findByDeleteStatus(dStatus);
    }

    @Override
    public PressCategory findBySlugOrTitle(String slug,String title){
        // TODO Auto-generated method stub
        return this.pressCategoryRepo.findBySlugOrTitle(slug,title);
    }

    @Override
    public PressCategory savePressCategory(@Valid PressCategory pressCategory) {
        // TODO Auto-generated method stub
        return this.pressCategoryRepo.save(pressCategory);
    }

    @Override
    public PressCategory findById(long id) {
        // TODO Auto-generated method stub
        return this.pressCategoryRepo.findByIdAndDeleteStatus(id,2);
    }

    @Override
    public PressCategory findPressCategoryBySlugOrTitleAndIdNot(String slug,String title, long id) {
        // TODO Auto-generated method stub
        return this.pressCategoryRepo.findBySlugOrTitleAndIdNot(slug,title,id);
    }

    @Override
    public PressCategory updatePressCategory(@Valid PressCategory pressCategory) {
        // TODO Auto-generated method stub
        return this.pressCategoryRepo.save(pressCategory);
    }

    @Override
    public List<PressCategory> findByDisplayStatusAndDeleteStatus(String i, int j) {
        // TODO Auto-generated method stub
        return this.pressCategoryRepo.findByDisplayStatusAndDeleteStatus(i,j);
    }

    @Override
    public PressCategory findByIdAndDeleteStatus(long typeId, int i) {
        // TODO Auto-generated method stub
        return this.pressCategoryRepo.findByIdAndDeleteStatus(typeId, i);
    }

    @Override
    public void deletePressCategory(PressCategory pressCategory) {
        // TODO Auto-generated method stub
        this.pressCategoryRepo.delete(pressCategory);
    }

    @Override
    public PressCategory findBySlug(String slug) {
        // TODO Auto-generated method stub
        return this.pressCategoryRepo.findBySlug(slug);
    }

    @Override
    public List<PressCategory> findTop10ByVisited() {
        // TODO Auto-generated method stub
        return this.pressCategoryRepo.findTop10ByVisited(PageRequest.of(0,10));
    }


}
