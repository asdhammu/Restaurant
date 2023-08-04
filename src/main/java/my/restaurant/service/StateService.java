package my.restaurant.service;

import my.restaurant.dto.StateDTO;

import java.util.List;

public interface StateService {
    List<StateDTO> getStatesByCountry(long countryId);
    List<StateDTO> getStatesByCountry(String name);
}
