package gui.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Role {

    private Long id;
    private String name, description;
    public Role(){

    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
