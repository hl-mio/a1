package hlmio.oauth2.conf.security.access;

import hlmio.oauth2.conf.security.other.NoAccess;
import hlmio.oauth2.conf.security.other.NoAuth;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.AbstractMethodSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


@Component
public class MyMethodSecurityMetadataSource extends AbstractMethodSecurityMetadataSource {

    public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {

        if (method.getDeclaringClass() == Object.class) {
            return Collections.emptyList();
        }
        if(targetClass.getName().indexOf("hlmio.oauth2.api") == -1){
            return Collections.emptyList();
        }

        System.out.println("method: '" + method.getName() + "'     class: '" + targetClass + "'");

        ArrayList<ConfigAttribute> attrs = new ArrayList(5);

        // region 权限（1）：控制器名::方法名
        // 获取控制器的名称
        String controllerName_all = targetClass.getName();
        String[] controllerName_arr = controllerName_all.split("\\.");
        String controllerName = controllerName_arr[controllerName_arr.length-1];
        // 获取方法名
        String funcName = method.getName();
        // 权限字段的名称
        String permName = controllerName + "::" + funcName;
        System.out.println(permName);
        attrs.add(new MyConfigAttribute(permName));
        // endregion

        // region 权限（2）：注解NoAuth
        NoAuth noAuth = (NoAuth)this.findAnnotation(method, targetClass, NoAuth.class);
        NoAccess noAccess = (NoAccess)this.findAnnotation(method, targetClass, NoAccess.class);
        if(noAuth!=null){
            attrs.add(new MyConfigAttribute("NoAuth"));
        }
        if(noAccess!=null){
            attrs.add(new MyConfigAttribute("NoAccess"));
        }
        // endregion


        attrs.trimToSize();
        return attrs;
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    private <A extends Annotation> A findAnnotation(Method method, Class<?> targetClass, Class<A> annotationClass) {
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        A annotation = AnnotationUtils.findAnnotation(specificMethod, annotationClass);
        if (annotation != null) {
            this.logger.debug(annotation + " found on specific method: " + specificMethod);
            return annotation;
        } else {
            if (specificMethod != method) {
                annotation = AnnotationUtils.findAnnotation(method, annotationClass);
                if (annotation != null) {
                    this.logger.debug(annotation + " found on: " + method);
                    return annotation;
                }
            }

            annotation = AnnotationUtils.findAnnotation(specificMethod.getDeclaringClass(), annotationClass);
            if (annotation != null) {
                this.logger.debug(annotation + " found on: " + specificMethod.getDeclaringClass().getName());
                return annotation;
            } else {
                return null;
            }
        }
    }
}

