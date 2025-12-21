package rainchik.gateway.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegistrationResponse {

    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;

}
