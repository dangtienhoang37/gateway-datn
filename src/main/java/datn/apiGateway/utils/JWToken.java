//package datn.apiGateway.utils;
//
//
//import com.nimbusds.jose.JOSEException;
//import com.nimbusds.jose.JWSAlgorithm;
//import com.nimbusds.jose.JWSHeader;
//import com.nimbusds.jose.JWSVerifier;
//import com.nimbusds.jose.crypto.MACVerifier;
//import com.nimbusds.jwt.JWTClaimsSet;
//import com.nimbusds.jwt.SignedJWT;
//import lombok.experimental.NonFinal;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.spec.SecretKeySpec;
//import java.text.ParseException;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//import java.util.UUID;
//
//@Component
//public class JWToken {
//    @NonFinal
//    protected static final String SIGNER_KEY = "Ry5OGtcJ+5dTSUV30C1LMaNew2uOotz0zpaBT/F9DJ5fLbcC5EoYtK/Ldh3H8VZo";
//
//
//    public boolean verifyToken(String token) throws JOSEException, ParseException {
//        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
//        SignedJWT signedJWT = SignedJWT.parse(token);
//        Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();
//        return signedJWT.verify(verifier) && expireTime.after(new Date());
//    }
//    public Jwt jwtDecoder(String token) {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(),"HS512");
//        return NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build().decode(token);
//    }
//
//
//
//
//}