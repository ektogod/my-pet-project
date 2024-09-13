package bot;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

@Getter
@Setter
@Component
public class CurrentState {
    private States state = States.NONE;
}
