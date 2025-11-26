package rainchik.gateway.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class AuthRegistrationResponse {

    private long userId;
    private String accessToken;
    private String refreshToken;
    private String username;
    private Collection authorities;

}
