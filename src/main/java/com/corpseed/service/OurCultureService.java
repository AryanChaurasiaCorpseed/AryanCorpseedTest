package com.corpseed.service;

import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.lifeentity.OurCulture;

public interface OurCultureService {

	List<OurCulture> allCulture();

	OurCulture findByTitle(String title);

	OurCulture saveOurCulture(@Valid OurCulture ourCulture);

	OurCulture findByUuid(String uuid);

	OurCulture findByTitleAndUuidNot(String title, String uuid);

	OurCulture findByDisplayStatusAndDeleteStatusAndTitle(int i, int j,String title);

	List<OurCulture> findByDisplayStatusAndDeleteStatus(int i, int j);

	OurCulture findByIdAndStatus(long typeId, int i);

	void deleteOurCulture(OurCulture ourCulture);
}
