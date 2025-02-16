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
    boolean isVerified;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<City> cities = new HashSet<>();

    @ManyToMany(mappedBy = "emails", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    public Email(String email, String name) {
        this.email = email;
        this.name = name;
        this.isVerified = false;
    }
}
