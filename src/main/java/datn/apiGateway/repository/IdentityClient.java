package datn.apiGateway.repository;

import datn.apiGateway.dto.ApiResponse;
import datn.apiGateway.dto.request.IntrospectRequest;
import datn.apiGateway.dto.response.IntrospectResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

import java.awt.*;

public interface IdentityClient {
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request, @RequestHeader("X-api-key") String apiKey);
}
