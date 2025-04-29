package bot.client.email_weather;

import com.tinkoff_lab.dto.n.EmailDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface EmailRegisterClient {
    @PostExchange("/email/add")
    String response(@RequestBody EmailDTO emailDTO);
}
