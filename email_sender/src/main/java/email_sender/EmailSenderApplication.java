package email_sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

@SpringBootApplication()
@EntityScan(basePackages = "email_sender")

public class EmailSenderApplication {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(EmailSenderApplication.class);
        logger.info("Application has started working");

        SpringApplication app = new SpringApplication(EmailSenderApplication.class);
        app.addListeners((ApplicationListener<ContextClosedEvent>) event -> {
            logger.info("Application has terminated working\n");
        });

        app.run(args);
    }
}
