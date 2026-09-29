package in.learnatorium.platform.security;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.auth")
public record AuthProperties(String issuer, Duration accessTokenTtl, Duration refreshTokenTtl, String jwtSecret) {}

