package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CountryControllerTest {

    @Mock
    private CountryService countryService;

    @InjectMocks
    private CountryController countryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
    }



    @Test
    void obtenerPaisesTodos_ReturnsAllCountries() throws Exception {
        List<Country> countries = List.of(new Country("Argentina", 45000000, 2780000, "ARG", "South America", null, null));
        when(countryService.getAllCountries()).thenReturn(countries);

        mockMvc.perform(get("/api/countries/tests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void getPaisesEnContinente_ReturnsCountriesInContinent() throws Exception {
        List<CountryDTO> countries = List.of(new CountryDTO("ARG", "Argentina"));
        when(countryService.returnCountryByContinent(anyString())).thenReturn(countries);

        mockMvc.perform(get("/api/countries/Asia/continent"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].code").value("ARG"));
    }

    @Test
    void getPaisesPorIdioma_ReturnsCountriesByLanguage() throws Exception {
        List<CountryDTO> countries = List.of(new CountryDTO("ARG", "Argentina"));
        when(countryService.returnCountryByLanguage(anyString())).thenReturn(countries);

        mockMvc.perform(get("/api/countries/Spanish/language"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].code").value("ARG"));
    }

    @Test
    void getPaisConMasFronteras_ReturnsCountryWithMostBorders() throws Exception {
        CountryDTO country = new CountryDTO("ARG", "Argentina");
        when(countryService.returnPaisConMasFronteras()).thenReturn(country);

        mockMvc.perform(get("/api/countries/most-borders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("ARG"));
    }

    @Test
    void postPaisesAleatorios_ReturnsCountries() throws Exception {
        List<CountryDTO> countries = List.of(new CountryDTO("ARG", "Argentina"));
        when(countryService.insertRandomCountries(anyInt())).thenReturn(countries);

        mockMvc.perform(post("/api/countries")
                        .contentType("application/json")
                        .content("5"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].code").value("ARG"));
    }

    @Test
    void postPaisesAleatorios_ReturnsBadRequest_WhenInvalidAmount() throws Exception {
        mockMvc.perform(post("/api/countries")
                        .contentType("application/json")
                        .content("11"))
                .andExpect(status().isBadRequest());
    }
}