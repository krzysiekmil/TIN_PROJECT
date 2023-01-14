package pjwstk.s20124.tin.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@ConfigurationProperties(value = "application",ignoreUnknownFields = false)
@Data
public class ApplicationProperties {
    private CorsConfiguration cors;
    private Security security;
    private Mail mail;
    private OTP otp;

    @Data
    public static class Security {
        private String jwtSecret;
        private Long jwtExpirationMs;
        private Long jwtRefreshExpirationMs;
    }

    @Data
    public static class Mail {
        private MailUser registration;
        private MailUser announcement;
        private int port;
        private String host;
        private String contactMail;
    }

    @Data
    public static class MailUser {
        private String username;
        private String mailFrom;
        private String password;
    }

    @Data
    public static class OTP {
        private int userId;
        private int unitId;
        private String token;
    }
}
