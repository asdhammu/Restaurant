package my.restaurant.service.impl;

import jakarta.transaction.Transactional;
import my.restaurant.dto.StateDTO;
import my.restaurant.entity.State;
import my.restaurant.repository.StateRepository;
import my.restaurant.service.StateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public List<StateDTO> getStatesByCountry(long countryId) {
        List<State> states = this.stateRepository.findStatesByCountryCountryId(countryId);
        return states.stream().map(state -> new StateDTO(state.getStateId(), state.getName())).toList();
    }

    @Override
    public List<StateDTO> getStatesByCountry(String name) {
        List<State> states = this.stateRepository.findStatesByCountryName(name);
        return states.stream().map(state -> new StateDTO(state.getStateId(), state.getName())).toList();
    }
}
