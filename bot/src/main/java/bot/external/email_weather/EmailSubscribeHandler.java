package bot.external.email_weather;

import bot.client.email_weather.EmailSubscribeClient;
import com.tinkoff_lab.dto.weather.CityDTOOO;
import com.tinkoff_lab.dto.weather.request.email.WeatherEmailRequest;
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
public class EmailSubscribeHandler {
    EmailSubscribeClient emailSubscribeClient;

    public String subscribe(String email, String msg) {
        String[] lines = msg.split("\n");
        List<CityDTOOO> cities;
        try {
            cities = parseCities(lines);
        }
        catch (RuntimeException ex){
            return "City not found.";
        }
        WeatherEmailRequest request = new WeatherEmailRequest(email, cities);

        String response;
        try {
           response = emailSubscribeClient.response(request);
        }
        catch (RestClientResponseException ex){
            response = ex.getResponseBodyAs(String.class);
        }
        return response;
    }

    private List<CityDTOOO> parseCities(String[] lines) {
        List<CityDTOOO> cities = new ArrayList<>();
        for (String line : lines) {
            String[] cityData = line.split(", ");
            if(cityData.length != 2){
                throw new RuntimeException();
            }
            cities.add(new CityDTOOO(cityData[0], cityData[1]));
        }

        return cities;
    }
}
