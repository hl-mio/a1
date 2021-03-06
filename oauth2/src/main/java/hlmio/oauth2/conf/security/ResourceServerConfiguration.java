package hlmio.oauth2.conf.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;

    public ResourceServerConfiguration(final TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    private static final String ROOT_PATTERN = "/**";

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 排除路径验证
//                .antMatchers("/hello")
//                .permitAll()
                .antMatchers(ROOT_PATTERN)
                .authenticated();
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore);
    }

}
