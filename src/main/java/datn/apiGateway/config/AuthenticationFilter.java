package datn.apiGateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datn.apiGateway.dto.ApiResponse;
import datn.apiGateway.service.IdentityService;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    @Autowired
    @Lazy
    IdentityService identityService;
    @Autowired
    ObjectMapper objectMapper;
    @NonFinal
    String[] publicEndpoints = {"/api/v1/auth/login","/api/v1/auth/register","/api/v1/auth/introspect","/api/v1/parking/get-all","/api/v1/parking/get-all-by-district/.*","/api/v1/parking/get-all-by-ward/.*"};
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(isPublicEndpoint(exchange.getRequest())){
            return chain.filter(exchange);
        }
        log.info("enter authen filter");
        // get token from auth header
        List<String> authHeader =  exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        String authHeadera = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).toString();
        log.info("eauthHeadera below {}",authHeadera);

        // Kiểm tra nếu chuỗi Authorization có giá trị hay không
        if (!authHeadera.startsWith("[Bearer ")) {
            log.info("enter unauthen ");

            try {
                log.info("khong co bearer ");

                return unauthenticated(exchange.getResponse());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        // Cắt chuỗi Authorization để lấy phần token
        String token = authHeadera.substring(8, authHeadera.length() - 1);  // Cắt từ vị trí 7 để bỏ qua từ "Bearer "

        // Log token để kiểm tra
        log.info("token below");
        System.out.println(token);

        // call introspec token

        return identityService.introspect(token).flatMap(introspectResponseApiResponse -> {
            if(introspectResponseApiResponse.isSucess()){
                return chain.filter(exchange);
            } else {
                try {
                    log.info("khong sucess ");

                    return unauthenticated(exchange.getResponse());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }).onErrorResume(throwable -> {
            log.info("loi tai",throwable);

            try {
                log.info("loi ");

                return unauthenticated(exchange.getResponse());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        // verify token

        // delegate -> service
        // sucess -> filterchain

//        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
    private boolean isPublicEndpoint(ServerHttpRequest request){
        return Arrays.stream(publicEndpoints).anyMatch(s -> request.getURI().getPath().matches(s));
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) throws JsonProcessingException {

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1001)
                .message("gateway unauth")
                .build();
        String body = null;
        body = objectMapper.writeValueAsString(apiResponse);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes())));




    }
}
