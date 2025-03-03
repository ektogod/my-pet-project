    package bot.client;

    import bot.dto.IPResponse;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import org.springframework.web.service.annotation.GetExchange;

    public interface IPClient {
        @GetExchange
        IPResponse getIp();
    }