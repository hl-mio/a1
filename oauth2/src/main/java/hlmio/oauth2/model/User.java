package hlmio.oauth2.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable,UserDetails {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String username;
    private String password;
    private String salt;
    private String status;
    private Collection<? extends GrantedAuthority> authorities;


    public boolean isAccountNonExpired(){
        return "1".equals(status);
    }
    public boolean isAccountNonLocked(){
        return "1".equals(status);
    }
    public boolean isCredentialsNonExpired(){
        return "1".equals(status);
    }
    public boolean isEnabled(){
        return "1".equals(status);
    }
}
