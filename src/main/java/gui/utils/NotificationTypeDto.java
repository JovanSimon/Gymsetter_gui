package gui.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationTypeDto {
    private Long id;
    private String notificationType, subject, body;
}
