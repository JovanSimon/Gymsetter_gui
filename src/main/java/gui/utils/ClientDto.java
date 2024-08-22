package gui.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto {
    private Long id;
    private String email, username, name, lastName;
    private Long isZabrana;

    private String dateOfBirth;
    private Role role;
}
