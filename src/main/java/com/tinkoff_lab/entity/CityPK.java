package com.tinkoff_lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class CityPK {
    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    public CityPK() {
    }
}
