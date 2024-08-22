package gui.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationType {
    private Long id;
    private String notificationType, subject, body;

    public NotificationType() {

    }

    public NotificationType(String notificationType, String subject, String body) {
        this.notificationType = notificationType;
        this.subject = subject;
        this.body = body;
    }

    public NotificationType(String notificationType) {
            this.notificationType = notificationType;
        }
}
