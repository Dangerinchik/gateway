package rainchik.gateway.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import rainchik.gateway.dto.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class RegistrationService {

    private final WebClient authServiceWebClient;
    private final WebClient userServiceWebClient;

    public RegistrationService(WebClient authServiceWebClient, WebClient userServiceWebClient) {
        this.authServiceWebClient = authServiceWebClient;
        this.userServiceWebClient = userServiceWebClient;
    }

    public Mono<RegistrationResponse> register(RegistrationRequest registrationRequest) {

        return createAuthCredentials(registrationRequest)
                .flatMap(authRegistrationResponse -> {
                    return createUserProfile(registrationRequest, authRegistrationResponse.getUserId())
                            .onErrorResume(throwable -> {
                                return rollbackAuthCredentials(authRegistrationResponse.getUserId())
                                        .then(Mono.error(throwable));
                            });
                })
                .map(userResponse -> createSuccessResponse(userResponse.getId(), "User registered successfully"))
                .onErrorResume(throwable -> {
                    return Mono.just(createErrorResponse(throwable.getMessage()));
                });



    }

    private Mono<AuthRegistrationResponse> createAuthCredentials(RegistrationRequest registrationRequest) {
        AuthRegistrationRequest authRegistrationRequest = new AuthRegistrationRequest();

        authRegistrationRequest.setUsername(registrationRequest.getUsername());
        authRegistrationRequest.setPassword(registrationRequest.getPassword());
        authRegistrationRequest.setEmail(registrationRequest.getEmail());

        return authServiceWebClient.post()
                .uri("/token/save")
                .bodyValue(authRegistrationRequest)
                .retrieve()
                .bodyToMono(AuthRegistrationResponse.class);
    }

    private Mono<UserRegistrationResponse> createUserProfile(RegistrationRequest userRegistrationRequest, long userId) {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail(userRegistrationRequest.getEmail());
        request.setName(userRegistrationRequest.getName());
        request.setSurname(userRegistrationRequest.getSurname());
        request.setBirthDate(userRegistrationRequest.getBirthday());

        return userServiceWebClient.post()
                .uri("/user/create")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserRegistrationResponse.class);
    }

    private Mono<Void> rollbackAuthCredentials(long userId) {

        return authServiceWebClient.delete()
                .uri("/token/credentials/{userId}", userId)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(ex -> Mono.empty());
    }

    private RegistrationResponse createSuccessResponse(long userId, String message) {
        RegistrationResponse response = new RegistrationResponse();
        response.setUserId(userId);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.setSuccess(true);
        return response;
    }

    private RegistrationResponse createErrorResponse(String errorMessage) {
        RegistrationResponse response = new RegistrationResponse();
        response.setMessage(errorMessage);
        response.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.setSuccess(false);
        return response;
    }

}
