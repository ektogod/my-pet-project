package com.tinkoff_lab.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "email")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Getter
@Setter

public class Email {
    @Id
    @Column(name = "email")
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "name")
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "code")
    @EqualsAndHashCode.Include
    private String code;

    @Column(name = "is_verified", columnDefinition = "BIT(1)")
    @EqualsAndHashCode.Include
    Boolean isVerified;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<City> cities = new HashSet<>();

    public Email(String email, String name) {
        this.email = email;
        this.name = name;
        this.isVerified = false;
    }

    public void addCity(City city){
        this.cities.add(city);
        city.getEmails().add(this);
    }

    public void removeCity(City city){
        this.cities.remove(city);
        city.getEmails().remove(this);
    }
}
