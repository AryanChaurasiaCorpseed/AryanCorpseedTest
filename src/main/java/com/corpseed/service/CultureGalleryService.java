package com.corpseed.service;

import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.lifeentity.CultureGallery;
import com.corpseed.entity.lifeentity.OurCulture;

public interface CultureGalleryService {

	List<CultureGallery> allCultureGallery();

	CultureGallery findByUuid(String uuid);

	CultureGallery saveCultureGallery(@Valid CultureGallery cultureGallery);

	CultureGallery findByIdAndStatus(long typeId, int i);

	void deleteCultureGallery(CultureGallery cultureGallery);

	List<CultureGallery> findCultureGalleryByCulture(OurCulture findByUuid,int dStatus);
}
