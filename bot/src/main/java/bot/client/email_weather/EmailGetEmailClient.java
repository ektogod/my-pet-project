package bot.client.email_weather;

import com.tinkoff_lab.dto.n.EmailDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface EmailGetEmailClient {
    @GetExchange("/email/{email}/get")
    ResponseEntity<EmailDTO> getEmail(@PathVariable String email);
}
