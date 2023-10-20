package com.corpseed.repository.regionrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.Country;
import com.corpseed.entity.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

	State findByStateNameAndCountryAndDeleteStatus(String stateName, Country country,int dStatus);

	List<State> findByCountryAndDeleteStatus(Country country,int dStatus);

	State findByUuidAndDeleteStatus(String stateuuid,int dStatus);

	State findByStateNameAndCountryAndUuidNot(String stateName, Country country, String stateuuid);

	List<State> findByDeleteStatusOrderByIdDesc(int dStatus);

	State findByIdAndDeleteStatus(long typeId, int dStatus);

	@Query(nativeQuery = true,value = "SELECT * FROM state where country_id= ?1")
	List<State> findByCountryId(Long countryId);

    State findByStateName(String stateName);
}
