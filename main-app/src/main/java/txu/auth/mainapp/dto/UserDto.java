package txu.auth.mainapp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    String username;
    String password;
    String role;
}
