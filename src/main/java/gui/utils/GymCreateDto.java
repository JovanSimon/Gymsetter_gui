package gui.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GymCreateDto {
    private String name;
    private String description;
    private Long managerId;
}