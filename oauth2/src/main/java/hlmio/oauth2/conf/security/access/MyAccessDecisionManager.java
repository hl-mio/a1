package hlmio.oauth2.conf.security.access;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

        List<String> targetAccessList =  configAttributes.stream()
                                    .map( i -> i.getAttribute())
                                    .collect(Collectors.toList());
        // 放行无需登录和权限的注解
        if(targetAccessList.contains("NoAuth")){
            return;
        }
        // 拒绝未登录用户
        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            throw new AccessDeniedException("当前访问需要登录");
        }
        // 放行无需权限的注解
        if(targetAccessList.contains("NoAccess")){
            return;
        }

        List<String> userAccessList =  authentication.getAuthorities().stream()
                                            .map( i -> i.getAuthority())
                                            .collect(Collectors.toList());

        for (String s : userAccessList) {
            for (String s1 : targetAccessList) {
                if(s != null){
                    if(s.equals(s1)){
                        return;
                    }
                }
            }
        }


        throw new AccessDeniedException("当前访问没有权限");
    }

    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    public boolean supports(Class<?> clazz) {
        return MethodInvocation.class.isAssignableFrom(clazz);
    }
}