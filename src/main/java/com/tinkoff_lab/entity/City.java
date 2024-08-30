package com.tinkoff_lab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "city")
@Getter
@Setter
public class City {
    @EmbeddedId
    private CityPK pk;

    @ManyToMany(mappedBy = "cities")
    private List<User> users;

    public City(CityPK pk) {
        this.pk = pk;
    }
}
