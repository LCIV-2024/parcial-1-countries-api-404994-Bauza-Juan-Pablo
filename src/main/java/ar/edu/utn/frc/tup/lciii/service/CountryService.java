package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryEntity;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        private final CountryRepository countryRepository;

        private final RestTemplate restTemplate;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        public List<CountryDTO> returnAllCountries(Optional<String> nombreOpc, Optional<String> codigoOpc){
                List<Country> countries = getAllCountries();
                List<CountryDTO> countriesDto = new ArrayList<>();
                if(nombreOpc.isEmpty() && codigoOpc.isEmpty()){
                        for(Country country : countries){
                                countriesDto.add(mapToDTO(country));
                        }
                        return countriesDto;
                }
                if(nombreOpc.isPresent() && codigoOpc.isPresent()){
                        String nombre = nombreOpc.get();
                        String codigo = codigoOpc.get();
                        for(Country country : countries){
                                if(Objects.equals(country.getName(), nombre) && Objects.equals(country.getCode(), codigo)){
                                        countriesDto.add(mapToDTO(country));
                                }
                        }
                        return countriesDto;
                }

                if(nombreOpc.isPresent() && codigoOpc.isEmpty()){
                        String nombre = nombreOpc.get();
                        for(Country country : countries){
                                if(Objects.equals(country.getName(), nombre)){
                                        countriesDto.add(mapToDTO(country));
                                }
                        }
                        return countriesDto;
                }
                if(nombreOpc.isEmpty() && codigoOpc.isPresent()){
                        String codigo = codigoOpc.get();
                        for(Country country : countries){
                                if(Objects.equals(country.getCode(), codigo)){
                                        countriesDto.add(mapToDTO(country));
                                }
                        }
                        return countriesDto;
                }
                return countriesDto;
        }

        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                return Country.builder()
                        .name((String) nameData.get("common"))
                        .code((String) countryData.get("cca3"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .borders((List<String>) countryData.get("borders"))
                        .build();
        }

        public List<CountryDTO> returnCountryByContinent(String continent){
                List<Country> countries = getAllCountries();
                List<CountryDTO> countriesDtos = new ArrayList<>();
                for (Country country : countries){
                        if(Objects.equals(country.getRegion(), continent)){
                                countriesDtos.add(mapToDTO(country));
                        }
                }
                return countriesDtos;
        }

        public List<CountryDTO> returnCountryByLanguage(String language){
                List<Country> countries = getAllCountries();
                List<CountryDTO> countriesDtos = new ArrayList<>();
                for (Country country : countries){
                        if(country.getLanguages() != null){
                                Map<String, String> idiomas = country.getLanguages();
                                for(String idioma: idiomas.values()){
                                        if(Objects.equals(idioma, language)){
                                                countriesDtos.add(mapToDTO(country));
                                        }
                                }
                        }
                }
                return countriesDtos;
        }

        public CountryDTO returnPaisConMasFronteras(){
                List<Country> countries = getAllCountries();
                int cantidadFronteras = 0;
                CountryDTO paisConMasFronteras = null;

                for (Country country : countries){
                        if(country.getBorders() != null){
                                int cant = country.getBorders().size();
                                if(cant > cantidadFronteras){
                                        cantidadFronteras = cant;
                                        paisConMasFronteras = mapToDTO(country);
                                }
                        }
                }
                return paisConMasFronteras;
        }

        public List<CountryDTO> insertRandomCountries(int cantidad){
                List<Country> countries = getAllCountries();
                List<CountryDTO> countriesDtos = new ArrayList<>();
                for (int i = 0; i < cantidad; i++){
                        int randNum = (int) (Math.random() * countries.size()) + 1;
                        Country country = countries.get(randNum);
                        countriesDtos.add(mapToDTO(country));
                        //Mapear la entidad
                        CountryEntity entidad = new CountryEntity();
                        entidad.setCodigo(country.getCode());
                        entidad.setNombre(country.getName());
                        entidad.setPopulation(country.getPopulation());
                        entidad.setArea(country.getArea());
                        countryRepository.save(entidad);
                        countries.remove(randNum);
                }
                return countriesDtos;
        }


        private CountryDTO mapToDTO(Country country) {
                return new CountryDTO(country.getCode(), country.getName());
        }
}