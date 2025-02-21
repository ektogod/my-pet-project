package com.tinkoff_lab.service.n.impl;

import com.tinkoff_lab.dao.jpa.CityRepository;
import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.mapper.CityMapper;
import com.tinkoff_lab.service.n.CityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CityServiceImpl implements CityService {
    CityMapper mapper;
    CityRepository repository;

    @Override
    public List<CityDTO> getCities() {
        log.debug("Starting getCities method.");
        var cities = mapper.entitiesToDto(repository.findAll());
        log.info("Successfully retrieved cities.");
        log.debug("Leaving getCities method.");
        return cities;
    }

    @Override
    public CityDTO getCity(String cityName, String country) {
        log.debug("Starting getCity method with id {}.", cityName);
        var city_ = repository
                .findById(new CityPK(cityName, country))
                .orElseThrow(() -> {
                    log.error("City with id {} not found!", cityName);
                    return new EntityNotFoundException("City not found!");
                });

        log.info("Successfully retrieved cityName with id {}.", cityName);
        log.debug("Leaving getCity method.");
        return mapper.entityToDto(city_);
    }

    @Override
    public void deleteCity(String cityName, String country) {
        log.debug("Starting deleteCity method with id {}.", cityName);
        var city = repository
                .findById(new CityPK(cityName, country))
                .orElseThrow(() -> {
                    log.error("Attempt to delete city with non-existent id {}.", cityName);
                    return new EntityNotFoundException("City not found!");
                });

        repository.delete(city);
        log.info("Successfully removed city with id {}.", cityName);
        log.debug("Leaving deleteCity method.");
    }

    @Override
    public CityDTO updateCity(CityDTO cityDTO) {
        log.debug("Starting updateCity method with id {}.", cityDTO.city());
        var city = repository
                .findById(new CityPK(cityDTO.city(), cityDTO.country()))
                .orElseThrow(() -> {
                    log.error("Attempt to update city with non-existent id {}.", cityDTO.city());
                    return new EntityNotFoundException("City not found.");
                });

        log.debug("Updating city fields with new values.");
        city.setLatitude(cityDTO.latitude());
        city.setLongitude(cityDTO.longitude());

        var updatedCity = mapper.entityToDto(repository.save(city));
        log.info("Successfully updated city with id {}.", cityDTO.city());
        log.debug("Leaving updateCity method.");
        return updatedCity;
    }

    @Override
    public CityDTO addCity(CityDTO cityDTO) {
        log.debug("Starting addCity method.");
        City city = mapper.dtoToEntity(cityDTO);
        var createdCity = mapper.entityToDto(repository.save(city));
        log.info("Successfully created city with id {}.", cityDTO.city());
        log.debug("Leaving addCity method.");
        return createdCity;
    }
}
