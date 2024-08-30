package com.tinkoff_lab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<City> cities;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
