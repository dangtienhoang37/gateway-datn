package datn.apiGateway.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Builder
@Setter
public class IntrospectRequest {
    String token;
}
