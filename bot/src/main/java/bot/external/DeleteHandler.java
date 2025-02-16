package bot.external;

import bot.client.weather.DeleteClient;
import com.tinkoff_lab.dto.weather.CityDTOOO;
import com.tinkoff_lab.dto.weather.request.telegram.TelegramCitiesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteHandler {
    @Autowired
    private DeleteClient client;

    public String delete(Update update) {
        List<CityDTOOO> cities;
        try {
            cities = parseCities(update.getMessage().getText());
        }
        catch (RuntimeException ex){
            return "City not found.";
        }

        TelegramCitiesRequest request = new TelegramCitiesRequest(
                update.getMessage().getChatId(),
                update.getMessage().getChat().getUserName(),
                cities);

        String response;
        try {
            response = client.delete(request);
        } catch (RestClientResponseException ex) {
            response = ex.getResponseBodyAs(String.class);
        }
        return response;
    }

    private List<CityDTOOO> parseCities(String msg) {
        List<CityDTOOO> cities = new ArrayList<>();

        String[] lines = msg.split("\n");
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
