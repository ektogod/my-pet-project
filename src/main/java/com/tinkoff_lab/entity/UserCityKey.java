package com.tinkoff_lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class UserCityKey {
    @Column(name = "userID")
    private int userID;

    @Column(name = "cityID")
    private int cityID;
}
