package com.tinkoff_lab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "city")
@Getter
@Setter
public class City {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "temperature")
    private String temperature;

    @Column(name = "weather")
    private String weather;

    @OneToMany(mappedBy = "cityID", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<UserCity> subs;
}
