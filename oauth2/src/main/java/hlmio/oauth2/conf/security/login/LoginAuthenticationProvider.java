package hlmio.oauth2.conf.security.login;

import hlmio.oauth2.model.User;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * 登录认证的Provider，自定义实现{@link AuthenticationProvider} <br>
 */
@Log
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsServiceImpl;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // http请求的账户密码
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // 根据用户名查询数据库
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        log.info(String.format("[http请求的账户密码]: %s/%s", username, password));

        if (userDetails == null) {
            throw new BadCredentialsException("用户名未找到");
        }

        String rawPassword = password + ((User)userDetails).getSalt();
        String encodedPassword = "{MD5}"+userDetails.getPassword();
        if(!passwordEncoder.matches(rawPassword,encodedPassword)){
            throw new BadCredentialsException("密码不正确");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    /**
     * 是否支持处理当前Authentication对象类型
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
