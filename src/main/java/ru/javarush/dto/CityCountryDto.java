package ru.javarush.dto;

import lombok.*;
import ru.javarush.domain.enums.Continent;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class CityCountryDto {
    private Long id;

    private String name;

    private String district;

    private Long population;

    private String countryCode;

    private String alternativeCountryCode;

    private String countryName;

    private Continent continent;

    private String countryRegion;

    private BigDecimal countrySurfaceArea;

    private Integer countryPopulation;

    private Set<LanguageDto> languages;
}
