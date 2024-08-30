package com.tinkoff_lab.services.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CountryCodeService {
    public String getCountryCode(String country) {
        ObjectMapper mapper = new ObjectMapper();
        String code;
        try {
            File file = ResourceUtils.getFile("classpath:countryCodes.json");
            var map = mapper.readValue(file, Map.class);
            code = (String) map.get(country);
            if(code == null){
                throw new IOException("bebra");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return code;
    }
}
