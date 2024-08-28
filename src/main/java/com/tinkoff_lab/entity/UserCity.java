package com.tinkoff_lab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user-city")
@Getter
@Setter
public class UserCity {
    @EmbeddedId
    private UserCityKey id;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "userID", referencedColumnName = "id")
    private City city;

    @ManyToOne
    @MapsId("cityID")
    @JoinColumn(name = "cityID", referencedColumnName = "id")
    private User user;
}
