package email_sender.controller;

import bot.dto.EmailTextRequest;
import com.tinkoff_lab.dto.EmailDTO;
import email_sender.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ektogod/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailSender sender;
    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestBody EmailDTO request){
        sender.sendEmail(request.email(), request.message());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
