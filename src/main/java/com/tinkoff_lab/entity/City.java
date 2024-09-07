package com.tinkoff_lab.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "city")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class City {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private CityPK pk;

    @Column(name = "latitude")
    @EqualsAndHashCode.Include
    private double latitude;

    @Column(name = "longitude")
    @EqualsAndHashCode.Include
    private double longitude;

    @ManyToMany(mappedBy = "cities", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    public City(CityPK pk, double latitude, double longitude) {
        this.pk = pk;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public City() {
    }
}
