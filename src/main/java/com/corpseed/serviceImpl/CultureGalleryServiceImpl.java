package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.lifeentity.CultureGallery;
import com.corpseed.entity.lifeentity.OurCulture;
import com.corpseed.repository.CultureGalleryRepo;
import com.corpseed.service.CultureGalleryService;

@Service
public class CultureGalleryServiceImpl implements CultureGalleryService {

	@Autowired
	private CultureGalleryRepo cultureGalleryRepo;
	
	@Override
	public List<CultureGallery> allCultureGallery() {
		// TODO Auto-generated method stub
		return this.cultureGalleryRepo.findByDeleteStatus(2);
	}

	@Override
	public CultureGallery findByUuid(String uuid) {
		// TODO Auto-generated method stub
		return this.cultureGalleryRepo.findByUuid(uuid);
	}

	@Override
	public CultureGallery saveCultureGallery(@Valid CultureGallery cultureGallery) {
		// TODO Auto-generated method stub
		return this.cultureGalleryRepo.save(cultureGallery);
	}

	@Override
	public CultureGallery findByIdAndStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.cultureGalleryRepo.findByIdAndDeleteStatus(typeId,i);
	}

	@Override
	public void deleteCultureGallery(CultureGallery cultureGallery) {
		// TODO Auto-generated method stub
		this.cultureGalleryRepo.delete(cultureGallery);
	}

	@Override
	public List<CultureGallery> findCultureGalleryByCulture(OurCulture findByUuid,int dStatus) {
		// TODO Auto-generated method stub
		return this.cultureGalleryRepo.findByOurCultureAndDeleteStatus(findByUuid,dStatus);
	}

}
