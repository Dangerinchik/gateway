package rainchik.gateway.dto;

import lombok.Data;

@Data
public class RegistrationResponse {
    private Long userId;
    private String message;
    private String timestamp;
    private Boolean success;
}
