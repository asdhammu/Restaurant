package my.restaurant.controller;

import my.restaurant.dto.StateDTO;
import my.restaurant.service.StateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class StateController {

    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping("/states")
    @ResponseBody
    public List<StateDTO> getStates(@RequestParam("countryName") String countryName) {
        return this.stateService.getStatesByCountry(countryName);
    }

}
