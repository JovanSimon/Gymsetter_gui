package gui.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClientUpdatedDto {
    private String email, username, name, lastName;
    private String dateOfBirth;
}
