package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.TrandingSearch;
import org.springframework.stereotype.Repository;

@Repository
public interface TrandingSearchRepo extends JpaRepository<TrandingSearch, Long> {

	TrandingSearch findBySearchName(String key);

	List<TrandingSearch> findTop5ByOrderBySearchedDesc();

}
