package me.lgd.service;

import me.lgd.dynamicdatasource.DataSourceSelector;
import me.lgd.dynamicdatasource.DynamicDataSourceEnum;
import me.lgd.entity.User;
import me.lgd.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lgd
 * @date 2020/12/1 21:44
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @DataSourceSelector(value = DynamicDataSourceEnum.REPLICA)
    @Override
    public List<User> listUser() {
        return userMapper.selectAll();
    }

    @DataSourceSelector
    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @DataSourceSelector(value = DynamicDataSourceEnum.REPLICA)
    @Override
    public User findById(Integer id) {
        User user = new User();
        user.setId(id);
        return userMapper.selectByPrimaryKey(user);
    }
}
