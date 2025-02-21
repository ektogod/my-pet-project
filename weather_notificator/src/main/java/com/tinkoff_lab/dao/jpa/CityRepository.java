package com.tinkoff_lab.dao.jpa;

import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, CityPK> {
}
