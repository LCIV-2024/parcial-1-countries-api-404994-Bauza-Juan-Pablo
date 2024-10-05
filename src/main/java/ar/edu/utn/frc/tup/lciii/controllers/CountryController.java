package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/countries")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public List<CountryDTO> getPaises(@RequestParam (required = false) String nombre, @RequestParam (required = false) String codigo){
        return countryService.returnAllCountries(Optional.ofNullable(nombre), Optional.ofNullable(codigo));
    }
    @GetMapping("/tests")
    public List<Country> obtenerPaisesTodos(){
        return countryService.getAllCountries();
    }

    @GetMapping("/{continent}/continent")
    public List<CountryDTO> getPaisesEnContinente (@PathVariable("continent") String continent){
        return countryService.returnCountryByContinent(continent);
    }

    @GetMapping("/{language}/language")
    public List<CountryDTO> getPaisesPorIdioma (@PathVariable("language") String language){
        return countryService.returnCountryByLanguage(language);
    }

    @GetMapping("/most-borders")
    public CountryDTO getPaisConMasFronteras(){
        return countryService.returnPaisConMasFronteras();
    }

    @PostMapping
    public ResponseEntity<List<CountryDTO>> postPaisesAleatorios(@RequestBody int cantidad){
        if(cantidad > 10 || cantidad < 1){
            List<CountryDTO> countries = new ArrayList<>();
            return new ResponseEntity<List<CountryDTO>>(countries, HttpStatusCode.valueOf(400));
        }
        List<CountryDTO> countries = countryService.insertRandomCountries(cantidad);
        return new ResponseEntity<List<CountryDTO>>(countries, HttpStatus.CREATED);
    }


}