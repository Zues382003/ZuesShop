package com.zuesshopfrontend.setting.RestController;

import com.zuesshop.entity.Country;
import com.zuesshop.entity.State;
import com.zuesshop.entity.StateDTO;
import com.zuesshopfrontend.setting.Repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StateRestController {
    @Autowired
    private StateRepository repo;

    @GetMapping("/settings/list_states_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId){
        List<StateDTO> result = new ArrayList<>();
        List<State> listStates = repo.findByCountryOrderByNameAsc(new Country(countryId));

        listStates.forEach(state -> {
            result.add(new StateDTO(state.getId(), state.getName()));
        });

        return result;
    }
}
