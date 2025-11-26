package rainchik.gateway.dto;

import lombok.Data;

@Data
public class RegistrationResponse {
    private long userId;
    private String message;
    private String timestamp;
    private boolean success;
}
