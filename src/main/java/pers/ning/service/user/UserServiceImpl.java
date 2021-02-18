package pers.ning.service.user;

import pers.ning.dao.BaseDao;
import pers.ning.dao.user.UserDao;
import pers.ning.dao.user.UserDaoImpl;
import pers.ning.pojo.User;

import java.sql.*;
import java.util.List;

public class UserServiceImpl implements UserService {
    // 业务层都会调用dao层，所以需要引入dao层
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    public User login(String userCode, String password) {
        Connection conn = null;
        User user = null;

        try {
            conn = BaseDao.getConnection();
            // 通过业务层调用具体的数据库操作
            user = userDao.getLoginUser(conn, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // 每一层只关自己创建的资源
            BaseDao.closeResource(conn, null, null);
        }

        if (null != user){
            if (!user.getUserPassword().equals(password))
                user = null;
        }

        return user;
    }

    public boolean updatePwd(int id, String password) {
        Connection conn = null;
        boolean flag = false;

        try {
            conn = BaseDao.getConnection();
            if (userDao.updatePwd(conn, id, password) > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return flag;
    }

    public int getUserCount(String username, int userRole) {
        Connection conn = null;
        int count = 0;
        try {
            conn = BaseDao.getConnection();
            count = userDao.getUserCount(conn, username, userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }

        return count;
    }

    public List<User> getUserList(String queryUsername, int queryUserRole, int currentPageNo, int pageSize) {
        Connection conn = null;
        List<User> list = null;

        try {
            conn = BaseDao.getConnection();
            list = userDao.getUserList(conn, queryUsername, queryUserRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return list;
    }


}
