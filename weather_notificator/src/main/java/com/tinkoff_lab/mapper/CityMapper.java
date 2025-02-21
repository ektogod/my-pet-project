package com.tinkoff_lab.mapper;

import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {
    @Mapping(target = "pk", expression = "java(new CityPK(cityDTO.city(), cityDTO.country()))")
    City dtoToEntity(CityDTO cityDTO);

    @Mapping(target = "city", source = "pk.city")
    @Mapping(target = "country", source = "pk.country")
    CityDTO entityToDto(City city);

    List<CityDTO> entitiesToDto(List<City> cities);
}
