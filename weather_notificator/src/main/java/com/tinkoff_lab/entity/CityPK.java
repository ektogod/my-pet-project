package com.tinkoff_lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class CityPK {
    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Override
    public String toString() {
        return String.format("%s, %s", city, country);
    }
}
