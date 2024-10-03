package bot.controller;

import bot.UserNotificator;
import com.tinkoff_lab.dto.weather.TelegramUserMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ektogod/telegram")
@RequiredArgsConstructor
public class CurWeatherController {
    private final UserNotificator notificator;
    @PostMapping("/currentWeather")
    public ResponseEntity<Void> notifyUsers(@RequestBody TelegramUserMessages userMessages){
        notificator.notifyUsers(userMessages);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
