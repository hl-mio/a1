package hlmio.oauth2.conf.security.access;

import lombok.Setter;
import org.springframework.security.access.ConfigAttribute;

public class MyConfigAttribute implements ConfigAttribute {

    @Setter
    String attribute;

    MyConfigAttribute(){
    }

    MyConfigAttribute(String attribute){
        this.attribute = attribute;
    }

    @Override
    public String getAttribute(){
        return attribute;
    }
}
