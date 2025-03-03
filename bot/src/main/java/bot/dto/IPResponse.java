package bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IPResponse(@JsonProperty("ip") String ip) {
}
