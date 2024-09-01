package com.tinkoff_lab.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
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
    private String latitude;

    @Column(name = "longitude")
    @EqualsAndHashCode.Include
    private String longitude;

    @ManyToMany(mappedBy = "cities")
    private List<User> users;

    public City(CityPK pk, String latitude, String longitude) {
        this.pk = pk;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public City() {
    }
}
