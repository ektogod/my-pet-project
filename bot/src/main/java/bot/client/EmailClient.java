package bot.client;

import bot.dto.EmailTextRequest;
import com.tinkoff_lab.dto.EmailDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface EmailClient {
    @PostExchange()
    void sendEmail(@RequestBody EmailDTO emailDTO);
}
