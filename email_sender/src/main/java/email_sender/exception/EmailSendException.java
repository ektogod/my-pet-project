package email_sender.exception;

public class EmailSendException extends RuntimeException{
    public EmailSendException(String message) {
        super(message);
    }
}
