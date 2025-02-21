package bot.external;

import bot.client.weather.SubscribeClient;
import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.dto.n.UserCitiesDTO;
import com.tinkoff_lab.dto.n.UserDTO;
import com.tinkoff_lab.dto.weather.CityDTOOO;
import com.tinkoff_lab.dto.weather.request.telegram.WeatherTelegramRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscribeHandler {
    private final SubscribeClient client;

    public String subscribe(Update update) {
        List<CityDTO> cities;
        try {
            cities = parseCities(update.getMessage().getText());
        }
        catch (RuntimeException ex){
            return "City not found.";
        }
        UserCitiesDTO request = new UserCitiesDTO(new UserDTO(
                update.getMessage().getChatId(),
                update.getMessage().getChat().getUserName(),
                update.getMessage().getChat().getFirstName(),
                update.getMessage().getChat().getLastName()),
                cities);

        String response;
        try {
           response = client.response(request);
        }
        catch (RestClientResponseException ex){
            response = ex.getResponseBodyAs(String.class);
        }
        return response;
    }

    private List<CityDTO> parseCities(String msg) {
        List<CityDTO> cities = new ArrayList<>();

        String[] lines = msg.split("\n");
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
