package com.corpseed.repository.regionrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.Country;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

	Country findByCountryCodeAndShortName(String countryCode, String shortName);

	Country findByUuidAndDeleteStatus(String countryuuid,int dStatus);

	Country findByCountryCodeAndShortNameAndUuidNot(String countryCode, String shortName,String countryuuid);

	List<Country> findByDeleteStatusOrderByIdDesc(int dStatus);

	Country findByIdAndDeleteStatus(long typeId, int dStatus);

}
