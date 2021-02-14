package hlmio.oauth2.conf.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties("oauth2")
public class Oauth2Properties {
    String tokenStoreType;
    String jwtSigningKey;
}
