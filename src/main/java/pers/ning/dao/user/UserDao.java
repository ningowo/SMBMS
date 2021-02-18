package pers.ning.dao.user;

import pers.ning.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    // 得到登录的用户
    User getLoginUser(Connection connection, String userCode) throws SQLException;

    // 添加用户
    int updatePwd(Connection conn, int id, String password) throws SQLException;

    // 查询用户数量
    int getUserCount(Connection conn, String username, int userRole) throws SQLException;

    // 通过条件查询
    List<User> getUserList(Connection conn, String userName, int userRole, int currPage, int pageSize) throws Exception;
}
