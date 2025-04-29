package bot.client;

import com.tinkoff_lab.dto.SendEmailDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface EmailClient {
    @PostExchange()
    void sendEmail(@RequestBody SendEmailDTO sendEmailDTO);
}
