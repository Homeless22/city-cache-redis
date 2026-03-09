package ru.javarush.mapper;

import ru.javarush.domain.CountryLanguage;
import ru.javarush.dto.LanguageDto;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class LanguageMapper {
    private LanguageMapper() {
    }

    public static LanguageDto toDto(CountryLanguage entity) {
        LanguageDto dto = new LanguageDto();
        dto.setLanguage(entity.getLanguage());
        dto.setIsOfficial(entity.getIsOfficial());
        dto.setPercentage(entity.getPercentage());
        return dto;
    }

    public static Set<LanguageDto> toDtos(Set<CountryLanguage> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptySet();
        }
/*
        Set<LanguageDto> dtos = new HashSet<>();
        for (CountryLanguage entity : entities) {
            dtos.add(toDto(entity));
        }
        return dtos;
 */
        return entities.stream().map(LanguageMapper::toDto).collect(Collectors.toSet());
    }
}
