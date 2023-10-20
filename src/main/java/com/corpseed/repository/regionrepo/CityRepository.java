package com.corpseed.repository.regionrepo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.City;
import com.corpseed.entity.State;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

	City findByCityNameAndStateAndDeleteStatus(String cityName, State state,int dStatus);

	City findByUuidAndDeleteStatus(String cityuuid,int dStatus);

	City findByCityNameAndStateAndDeleteStatusAndUuidNot(String cityName, State state,int dStatus, String cityuuid);

	List<City> findByDeleteStatusOrderByIdDesc(int dStatus);

	List<City> findByCityNameContainingAndDeleteStatus(String value,int dStatus);

	City findByIdAndDeleteStatus(long typeId, int dStatus);

    List<City> findByState(State state);

    City findByCityName(String cityName);
}
