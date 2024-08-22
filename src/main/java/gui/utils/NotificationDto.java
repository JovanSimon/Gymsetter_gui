package gui.utils;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class NotificationDto {
    private String emailTo;
    private Long clientId;
    @SerializedName("createdDate")
    private Date createdDate;

    @SerializedName("updatedDate")
    private Date updatedDate;
    private NotificationType notificationType;

    public NotificationDto(String emailTo, NotificationType notificationType) {
        this.emailTo = emailTo;
        this.notificationType = notificationType;
    }

    public NotificationDto() {
    }
}
