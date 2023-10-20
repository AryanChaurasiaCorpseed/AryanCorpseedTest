package com.corpseed.serviceImpl;

import com.corpseed.entity.State;
import com.corpseed.repository.regionrepo.StateRepository;
import com.corpseed.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateServiceImpl implements StateService {

    @Autowired
    private StateRepository stateRepository;

    @Override
    public List<State> findStateByCountryId(Long countryId) {
        return this.stateRepository.findByCountryId(countryId);
    }

    @Override
    public State findByStateId(Long stateId) {
        return this.stateRepository.findById(stateId).orElse(null);
    }

    @Override
    public State findByStateName(String stateName) {
        return this.stateRepository.findByStateName(stateName);
    }
}
