package companyx.ResourceServer.token;


import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Additional token validator, that checks if the received token are blacklisted or not.
 */
@Service
@ConditionalOnClass(RedisConnectionFactory.class)
@ConditionalOnProperty(value = "spring.cache.type", havingValue = "redis")
public class BlacklistedTokenValidator implements OAuth2TokenValidator<Jwt> {

    private static final String BLOCKLIST_KEY_PREFIX = "cc:auth:blocklist:";

    @NonNull
    private static final OAuth2Error error = new OAuth2Error("blacklisted_token", "Token has been blacklisted", null);

    @NonNull
    private final RedisTemplate<String, String> blocklistRedisTemplate;

    public BlacklistedTokenValidator(@NonNull final RedisConnectionFactory redisConnectionFactory) {
        this.blocklistRedisTemplate = new RedisTemplate<>();
        this.blocklistRedisTemplate.setKeySerializer(new StringRedisSerializer());
        this.blocklistRedisTemplate.setValueSerializer(new StringRedisSerializer());
        this.blocklistRedisTemplate.setConnectionFactory(redisConnectionFactory);
        this.blocklistRedisTemplate.afterPropertiesSet();
    }

    @Override
    public OAuth2TokenValidatorResult validate(@NonNull final Jwt jwt) {
        final String accessTokensOnBlocklistExpireDate = blocklistRedisTemplate.opsForValue().get(BLOCKLIST_KEY_PREFIX + jwt.getId());
        if (accessTokensOnBlocklistExpireDate != null) {
            return OAuth2TokenValidatorResult.failure(error);
        }
        return OAuth2TokenValidatorResult.success();
    }
}
