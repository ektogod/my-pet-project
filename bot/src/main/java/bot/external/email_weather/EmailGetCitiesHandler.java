package bot.external.email_weather;

import bot.client.email_weather.EmailGetCitiesClient;
import com.tinkoff_lab.dto.weather.request.email.EmailRequest;
import com.tinkoff_lab.entity.CityPK;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailGetCitiesHandler {
    private final EmailGetCitiesClient client;

    public String get(String email) {
        String response;
        try {
            List<CityPK> cities = client.getCities(new EmailRequest(email)).getBody();
            if (cities == null || cities.isEmpty()) {
                response = "You have no any cities.";
            } else {
                response = cities
                        .stream()
                        .map(CityPK::toString)
                        .collect(Collectors.joining("\n"));
            }
        } catch (RestClientResponseException ex) {
            response = ex.getMessage();
        }

        return response;
    }

}
