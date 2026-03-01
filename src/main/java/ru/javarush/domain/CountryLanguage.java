package ru.javarush.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(schema = "world", name = "country_language")
@NoArgsConstructor
@Getter
@Setter
public class CountryLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column
    private String language;

    @Column(name = "is_official", columnDefinition = "TINYINT(1)")
    private Boolean isOfficial;

    @Column
    private BigDecimal percentage;
}
