package my.restaurant.service.impl;

import jakarta.transaction.Transactional;
import my.restaurant.dto.CountryDTO;
import my.restaurant.entity.Country;
import my.restaurant.repository.CountryRepository;
import my.restaurant.service.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<CountryDTO> getCountries() {
        List<Country> countries = this.countryRepository.findAll();
        return countries.stream().map(country -> new CountryDTO(country.getCountryId(), country.getName())).toList();
    }
}
