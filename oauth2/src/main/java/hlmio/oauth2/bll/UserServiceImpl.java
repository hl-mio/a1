package hlmio.oauth2.bll;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hlmio.oauth2.mapper.UserMapper;
import hlmio.oauth2.model.Permission;
import hlmio.oauth2.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  {


    public List<Permission> queryUserAuthorities(String userId) {
        return baseMapper.queryUserAuthorities(userId);
    }


    public User queryUserByUsername(String username) {
        return baseMapper.queryUserByUsername(username);
    }
}
