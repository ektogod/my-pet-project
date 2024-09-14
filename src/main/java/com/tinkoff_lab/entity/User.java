package com.tinkoff_lab.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Getter
@Setter

public class User {
    @Id
    @Column(name = "email")
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "name")
    @EqualsAndHashCode.Include
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<City> cities = new HashSet<>();

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
