package bot.external;

import bot.client.GetClient;
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

    public String get(TelegramRequest request){
        String response;
        try {
            List<CityPK> cities = client.getCities(request).getBody();
            if(cities == null || cities.isEmpty()) {
                response = "You have no any cities.";
            }
            else {
                response = cities
                        .stream()
                        .map(CityPK::toString)
                        .collect(Collectors.joining("\n"));
            }
        }
        catch (RestClientResponseException ex){
            response = "You have no any cities.";
        }

        return response;
    }
}
