package ar.edu.utn.frc.tup.lciii.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

@Data
@Entity
@Table(name = "COUNTRIES")
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String codigo;
    private String nombre;
    private long population;
    private double area;
}
