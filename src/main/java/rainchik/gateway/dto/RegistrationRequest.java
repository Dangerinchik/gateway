package rainchik.gateway.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrationRequest {

    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    private LocalDate birthday;

}
