package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;
    @Mock
    private  RestTemplate restTemplate;

    @InjectMocks
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void returnAllCountries_ReturnsCountriesByNameAndCode() {
        List<Country> countries = List.of(new Country("Argentina", 45, 27, "ARG", "South America", null, null));
        when(countryService.getAllCountries()).thenReturn(countries);

        List<CountryDTO> result = countryService.returnAllCountries(Optional.of("Argentina"), Optional.of("ARG"));

        assertEquals(1, result.size());
        assertEquals("ARG", result.get(0).getCode());
        assertEquals("Argentina", result.get(0).getName());
    }

    @Test
    void returnAllCountries_ReturnsEmptyList_WhenNoMatch() {
        List<Country> countries = List.of(new Country("Argentina", 45, 27, "ARG", "South America", null, null));
        when(countryService.getAllCountries()).thenReturn(countries);

        List<CountryDTO> result = countryService.returnAllCountries(Optional.of("Argentina"), Optional.of("ARG"));

        assertTrue(result.isEmpty());
    }

    @Test
    void returnCountryByContinent_ReturnsCountriesInContinent() {
        List<Country> countries = List.of(new Country("Argentina", 45, 27, "ARG", "South America", null, null));
        when(countryService.getAllCountries()).thenReturn(countries);

        List<CountryDTO> result = countryService.returnCountryByContinent("South America");

        assertEquals(1, result.size());
        assertEquals("ARG", result.get(0).getCode());
    }

    @Test
    void returnCountryByLanguage_ReturnsCountriesByLanguage() {
        List<Country> countries = List.of(new Country("Argentina", 45, 27, "ARG", "South America", null, Map.of("spa", "Spanish")));
        when(countryService.getAllCountries()).thenReturn(countries);

        List<CountryDTO> result = countryService.returnCountryByLanguage("Spanish");

        assertEquals(1, result.size());
        assertEquals("ARG", result.get(0).getCode());
    }

    @Test
    void returnPaisConMasFronteras_ReturnsCountryWithMostBorders() {
        List<Country> countries = List.of(new Country("Argentina", 45, 27, "ARG", "South America", List.of("BRA", "CHL"), null));
        when(countryService.getAllCountries()).thenReturn(countries);

        CountryDTO result = countryService.returnPaisConMasFronteras();

        assertEquals("ARG", result.getCode());
    }



    @Test
    void insertRandomCountries_ReturnsEmptyList_WhenInvalidAmount() {
        List<CountryDTO> countries = countryService.insertRandomCountries(11);
        assertTrue(countries.isEmpty());
    }
}