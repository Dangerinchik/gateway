package rainchik.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rainchik.gateway.dto.RegistrationRequest;
import rainchik.gateway.dto.RegistrationResponse;
import rainchik.gateway.service.RegistrationService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<RegistrationResponse>> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        return registrationService.register(registrationRequest)
                .map(registrationResponse -> {
                    if(registrationResponse.isSuccess()){
                        return ResponseEntity.ok(registrationResponse);
                    }
                    else{
                        return ResponseEntity.badRequest().body(registrationResponse);
                    }
                });
    }
}
