package gui.utils;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ManagerDto {
    private Long id,isZabrana;
    private String username, email, name, lastName;
    private String dateOfBirth, dateOfEmpl;
    private Role role;
}
