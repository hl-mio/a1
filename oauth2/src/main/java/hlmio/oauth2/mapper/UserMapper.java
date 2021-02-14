package hlmio.oauth2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hlmio.oauth2.model.Permission;
import hlmio.oauth2.model.User;
import org.apache.ibatis.annotations.Mapper;



import java.util.List;


//@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<Permission> queryUserAuthorities(String userId);
    User queryUserByUsername(String username);
}
