package com.tinkoff_lab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<City> cities = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Email> emails = new HashSet<>();

    public User(long chatId, String username, String firstname, String lastname) {
        this.chatId = chatId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public void addCity(City city){
        this.cities.add(city);
        city.getUsers().add(this);
    }

    public void removeCity(City city){
        this.cities.remove(city);
        city.getUsers().remove(this);
    }

    public void addEmail(Email email){
        this.emails.add(email);
        email.setUser(this);
    }

    public void removeEmail(Email email){
        this.emails.remove(email);
        email.setUser(null);
    }
}
