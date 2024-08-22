package gui.utils;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class TrainingCreateDto {
    private String name;
    private String typeOfTrening;
    private Integer price;
    private Long gymId;
    private String individualOrGroup;

    private String date;

    private String userList;
}
