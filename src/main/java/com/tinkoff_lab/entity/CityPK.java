package com.tinkoff_lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;

@Embeddable
@AllArgsConstructor
public class CityPK {
    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;
}
