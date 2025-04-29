package bot.external;

import bot.client.weather.GetClient;
import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.dto.weather.request.telegram.TelegramRequest;
import com.tinkoff_lab.entity.CityPK;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetHandler {
    private final GetClient client;

    public String get(long id){
        String response;
        try {
            List<CityDTO> cities = client.getCities(id).getBody();
            if(cities == null || cities.isEmpty()) {
                response = "You have no any cities.";
            }
            else {
                response = cities
                        .stream()
                        .map(city -> city.city() + " " + city.country())
                        .collect(Collectors.joining("\n"));
            }
        }
        catch (RestClientResponseException ex){
            response = "You have no any cities.";
        }

        return response;
    }
}
