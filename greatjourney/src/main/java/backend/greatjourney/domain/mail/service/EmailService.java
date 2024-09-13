package backend.greatjourney.domain.mail.service;

public interface EmailService {
    String sendSimpleMessage(String to)throws Exception;
}
