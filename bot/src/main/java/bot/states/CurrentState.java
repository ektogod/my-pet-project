package bot.states;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CurrentState {
    private States state = States.NONE;
}
