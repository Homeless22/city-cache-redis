package ru.javarush.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class LanguageDto {
    private String language;

    private Boolean isOfficial;

    private BigDecimal percentage;
}
