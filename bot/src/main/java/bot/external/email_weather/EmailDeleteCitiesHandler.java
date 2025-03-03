package bot.external.email_weather;


import bot.client.email_weather.EmailDeleteCitiesClient;
import com.tinkoff_lab.dto.n.CityDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailDeleteCitiesHandler {
    EmailDeleteCitiesClient client;
    public String deleteCities(String email, long chatId, String msg) {
        String[] lines = msg.split("\n");
        List<CityDTO> cities;
        try {
            cities = parseCities(lines);
        }
        catch (RuntimeException ex){
            return "City not found.";
        }

        String response;
        try {
            response = client.response(email, cities, chatId);
        }
        catch (RestClientResponseException ex){
            response = ex.getResponseBodyAs(String.class);
        }
        return response;
    }

    private List<CityDTO> parseCities(String[] lines) {
        List<CityDTO> cities = new ArrayList<>();
        for (String line : lines) {
            String[] cityData = line.split(", ");
            if(cityData.length != 2){
                throw new RuntimeException();
            }
            cities.add(new CityDTO(cityData[0], cityData[1], 0, 0));
        }

        return cities;
    }
}
