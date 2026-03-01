package ru.javarush.mapper;

import ru.javarush.domain.City;
import ru.javarush.dto.CityCountryDto;

import java.util.List;


public class CityMapper {
    private CityMapper() {
    }

    public static CityCountryDto toDto(City entity) {
        CityCountryDto dto = new CityCountryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDistrict(entity.getDistrict());
        dto.setPopulation(entity.getPopulation());
        dto.setCountryCode(entity.getCountry().getCode());
        dto.setAlternativeCountryCode(entity.getCountry().getAlternativeCode());
        dto.setCountryName(entity.getCountry().getName());
        dto.setContinent(entity.getCountry().getContinent());
        dto.setCountryRegion(entity.getCountry().getRegion());
        dto.setCountrySurfaceArea(entity.getCountry().getSurfaceArea());
        dto.setCountryPopulation(entity.getCountry().getPopulation());
        dto.setLanguages(LanguageMapper.toDtos(entity.getCountry().getLanguages()));
        return dto;
    }

    public static List<CityCountryDto> toDtos(List<City> entities) {
        return entities.stream().map(CityMapper::toDto).toList();
    }
}