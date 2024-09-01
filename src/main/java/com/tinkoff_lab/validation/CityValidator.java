package com.tinkoff_lab.validation;

import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.dto.CityDTO;
import com.tinkoff_lab.services.weather.CountryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CityValidator {
    private final AppConfig config;
    private final CountryCodeService codeService;

    @Autowired
    public CityValidator(AppConfig config, CountryCodeService codeService) {
        this.config = config;
        this.codeService = codeService;
    }

    public void validate(CityDTO city){
        String code = codeService.getCountryCode(city.country());
        if(code == null){

        }

        String coordinateUrl = String.format(config.getCountryCoordinatesUrl(), city.city(), city.country());
        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(coordinateUrl, String.class);
        if(response.isEmpty()){

        }
    }
}
