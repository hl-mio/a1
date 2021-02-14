package hlmio.oauth2.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;


@Data
public class Permission implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 1L;

    private String permissionId;
    private String name;
    private String url;
    private String perms;
    private String description;
    private Long parentId;

    @Override
    public String getAuthority() {
        return perms;
    }

}

