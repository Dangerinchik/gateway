package rainchik.gateway.dto;

import lombok.Data;

@Data
public class AuthRegistrationRequest {
    private String username;
    private String password;
    private String email;
}
