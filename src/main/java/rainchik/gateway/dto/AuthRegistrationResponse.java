package rainchik.gateway.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class AuthRegistrationResponse {

    private String email;
    private String accessToken;
    private String refreshToken;
    private String username;
    private Collection authorities;

}
