package hlmio.oauth2.conf.security.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MyGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Autowired
    AccessDecisionManager myAccessDecisionManager;

    @Autowired
    MethodSecurityMetadataSource myMethodSecurityMetadataSource;



    protected AccessDecisionManager accessDecisionManager() {
        return myAccessDecisionManager;
    }

    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return myMethodSecurityMetadataSource;
    }


}
