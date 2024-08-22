package gui.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class TrainingDto {
    private Long id;
    private String typeOfTrening, individualOrGroup, name;
    private Integer price;
    private String userList;
    private String date;
}
