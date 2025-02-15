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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
//    @Autowired
    private final WebClient webClient;
//    @Autowired
//    public IdentityService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.build();
//    }
    public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
        return webClient.post()
//                .uri("http://common-service.datnns.svc.cluster.local:8081/api/v1/auth/introspect") // Gọi service qua Load Balancer

            .uri("http://common-service:8081/api/v1/auth/introspect") // Gọi service qua Load Balancer
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-api-key", "internalApikey")
                .bodyValue("{\"token\": \"" + token + "\"}")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<IntrospectResponse>>() {}); // Chuyển đổi đúng kiểu dữ liệu
}
//    @Autowired

//    IdentityClient identityClient;
//    public Mono<ApiResponse<IntrospectResponse>> introspect(
//            String token) {
//        log.info("enter service");
//        log.info("enter service, token is{}" , token);
//        IntrospectRequest introspectRequest = IntrospectRequest.builder()
//                .token(token)
//                .build();
//        var res = identityClient.introspect(introspectRequest,"internalApikey");
//        log.info(res.toString());
//        return res;
//
//                };
    }

