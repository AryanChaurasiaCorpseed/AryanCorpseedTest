package com.corpseed.service;

import com.corpseed.entity.State;

import java.util.List;

public interface StateService {

    List<State> findStateByCountryId(Long countryId);

    State findByStateId(Long stateId);

    State findByStateName(String stateName);
}
