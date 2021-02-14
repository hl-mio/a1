package hlmio.oauth2.conf.security;


import hlmio.oauth2.conf.props.Oauth2Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.security.KeyPair;


/**
 * oauth2相关的配置 <br>
 * 这个配置文件加上WebSecurityConfiguration，就可以组成认证服务端 <br>
 * <br>
 *
 * @author hlmio
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private String tokenStoreType;

    @Autowired
    Oauth2Properties oauth2Properties;
    @Autowired
    DataSource dataSource;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsServiceImpl;


    @PostConstruct
    void init(){
        tokenStoreType = oauth2Properties.getTokenStoreType().toLowerCase();
    }


    /**
     * (1) 访问权限相关 <br>
     * 可配置属性项： <br>
     * 1. ClientDetail加密方式 <br>
     * 2. allowFormAuthenticationForClients 允许表单认证。针对/oauth/token端点。 <br>
     * 3. 添加开发配置tokenEndpointAuthenticationFilters <br>
     * 4. tokenKeyAccess、checkTokenAccess访问权限。 <br>
     */
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("permitAll()")
                // 允许 check_token 访问
                .checkTokenAccess("permitAll()")
                // 允许将客户端的用户名、密码直接写入表单
                .allowFormAuthenticationForClients();
    }


    /**
     * (2) 客户端信息 <br>
     * 配置从哪里获取ClientDetails信息。 <br>
     */
    // region
    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        // 信息从数据库获取
        clients.withClientDetails(clientDetails());

        // 使用本地内存
        //clients.inMemory()
        //        .withClient("admin")
        //        .secret(passwordEncoder.encode("123456"))
        //        .authorizedGrantTypes("password", "authorization_code", "refresh_token")
        //        .scopes("all")
        //        .authorities("oauth2")
        //        .accessTokenValiditySeconds(60 * 60 * 2) //2小时
        //        .refreshTokenValiditySeconds(60 * 60 * 24 * 7); // 7 天
    }

    @Bean
    public ClientDetailsService clientDetails() {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        //clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }
    // endregion


    /**
     * (3) 访问端点
     */
    // region
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                //启用oauth2管理 password认证方式，需要有这个
                .authenticationManager(authenticationManager)
                ////用户管理
                //.userDetailsService(userDetailsServiceImpl)
                //接收GET和POST
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                //token存储介质
                .tokenStore(tokenStore());

        switch (tokenStoreType){
            case "jwt" :
                endpoints
                    .accessTokenConverter(jwtAccessTokenConverter());
                break;
            default:
                break;
        }

    }

    @Bean
    public TokenStore tokenStore() {
        switch (tokenStoreType){
            case "jwt" :
                return new JwtTokenStore(jwtAccessTokenConverter());
            case "redis" :
                return new RedisTokenStore(redisConnectionFactory);
            default:
                return new InMemoryTokenStore();
        }
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(oauth2Properties.getJwtSigningKey());
        return jwtAccessTokenConverter;
    }
    // endregion

}
