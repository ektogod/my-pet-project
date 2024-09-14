package com.tinkoff_lab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tg-user")
@NoArgsConstructor
@Getter
@Setter
public class TelegramUser {
    @Id
    @Column(name = "chat-ID")
    private long chatId;

    @Column(name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<City> cities = new HashSet<>();

    public TelegramUser(long chatId, String username, String firstname, String lastname) {
        this.chatId = chatId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
