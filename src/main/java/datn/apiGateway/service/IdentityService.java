package datn.apiGateway.service;

import datn.apiGateway.dto.ApiResponse;
import datn.apiGateway.dto.request.IntrospectRequest;
import datn.apiGateway.dto.response.IntrospectResponse;
import datn.apiGateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {

    @Autowired
    IdentityClient identityClient;
    public Mono<ApiResponse<IntrospectResponse>> introspect(
            String token) {
        log.info("enter service");
        log.info("enter service, token is{}" , token);
        IntrospectRequest introspectRequest = IntrospectRequest.builder()
                .token(token)
                .build();

        return identityClient.introspect(introspectRequest,"internalApikey");

                };
    }

