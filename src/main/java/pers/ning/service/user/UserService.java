package pers.ning.service.user;

import pers.ning.pojo.User;

import java.util.List;

public interface UserService {
    // 用户登录
    User login(String userCode, String userPassword);

    // 根据用户id更改密码
    boolean updatePwd(int id, String password);

    int getUserCount(String username, int userRole);

    List<User> getUserList(String queryUsername, int queryUserRole, int currentPageNo, int pageSize);
}
