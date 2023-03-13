package companyx.ResourceServer.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

public class JwtDecoderConfigurer {

    @Value("spring.security.oauth2.resourceserver.jwt.issuer-uri")
    private String issuerUri;

    @Bean
    JwtDecoder jwtDecoder() {
        final NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);

        final OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        final OAuth2TokenValidator<Jwt> blacklistedTokenValidator = new BlacklistedTokenValidator(new LettuceConnectionFactory());
        final OAuth2TokenValidator<Jwt> standardValidatorWithBlacklistValidation = new DelegatingOAuth2TokenValidator<>(withIssuer, blacklistedTokenValidator);
        jwtDecoder.setJwtValidator(standardValidatorWithBlacklistValidation);
        return jwtDecoder;
    }
}
