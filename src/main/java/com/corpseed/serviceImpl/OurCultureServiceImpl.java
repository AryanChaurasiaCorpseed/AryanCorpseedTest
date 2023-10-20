package com.corpseed.serviceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corpseed.entity.lifeentity.OurCulture;
import com.corpseed.repository.OurCultureRepo;
import com.corpseed.service.OurCultureService;
@Service
public class OurCultureServiceImpl implements OurCultureService {

	@Autowired
	private OurCultureRepo ourCultureRepo;
	
	@Override
	public List<OurCulture> allCulture() {
		// TODO Auto-generated method stub
		return this.ourCultureRepo.findByDeleteStatus(2);
	}
 
	@Override
	public OurCulture findByTitle(String title) {
		// TODO Auto-generated method stub
		return this.ourCultureRepo.findByTitle(title);
	}

	@Override
	public OurCulture saveOurCulture(@Valid OurCulture ourCulture) {
		// TODO Auto-generated method stub
		return this.ourCultureRepo.save(ourCulture);
	}

	@Override
	public OurCulture findByUuid(String uuid) {
		// TODO Auto-generated method stub
		return this.ourCultureRepo.findByUuid(uuid);
	}

	@Override
	public OurCulture findByTitleAndUuidNot(String title, String uuid) {
		// TODO Auto-generated method stub
		return this.ourCultureRepo.findByTitleAndUuidNot(title, uuid);
	}

	@Override
	public OurCulture findByDisplayStatusAndDeleteStatusAndTitle(int i, int j, String title) {
		// TODO Auto-generated method stub
		return this.ourCultureRepo.findByDisplayStatusAndDeleteStatusAndTitle(i, j, title);
	}

	@Override
	public List<OurCulture> findByDisplayStatusAndDeleteStatus(int i, int j) {
		// TODO Auto-generated method stub
		return this.ourCultureRepo.findByDisplayStatusAndDeleteStatus(i, j);
	}

	@Override
	public OurCulture findByIdAndStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.ourCultureRepo.findByIdAndDeleteStatus(typeId,i);
	}

	@Override
	public void deleteOurCulture(OurCulture ourCulture) {
		// TODO Auto-generated method stub
		this.ourCultureRepo.delete(ourCulture);
	}

}
