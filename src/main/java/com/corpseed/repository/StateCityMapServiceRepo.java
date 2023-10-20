package com.corpseed.repository;

import com.corpseed.entity.serviceentity.Services;
import com.corpseed.entity.serviceentity.StateCityService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateCityMapServiceRepo extends JpaRepository<StateCityService,Long> {
    List<StateCityService> findByServices(Services service);
}
