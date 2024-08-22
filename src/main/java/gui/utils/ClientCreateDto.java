package gui.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCreateDto {
    private String email, username;
    private String password;
    private Role role;

}