package bot.external.email_weather;

import bot.client.email_weather.EmailSubscribeClient;
import bot.client.weather.SubscribeClient;
import com.tinkoff_lab.dto.weather.CityDTO;
import com.tinkoff_lab.dto.weather.request.email.WeatherEmailRequest;
import com.tinkoff_lab.dto.weather.request.telegram.WeatherTelegramRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailSubscribeHandler {
    EmailSubscribeClient emailSubscribeClient;

    public String subscribe(String email, String msg) {
        String[] lines = msg.split("\n");
        List<CityDTO> cities;
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

    private List<CityDTO> parseCities(String[] lines) {
        List<CityDTO> cities = new ArrayList<>();
        for (String line : lines) {
            String[] cityData = line.split(", ");
            if(cityData.length != 2){
                throw new RuntimeException();
            }
            cities.add(new CityDTO(cityData[0], cityData[1]));
        }

        return cities;
    }
}
