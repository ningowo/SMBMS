package pers.ning.dao.user;

import com.mysql.cj.util.StringUtils;
import pers.ning.dao.BaseDao;
import pers.ning.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    public User getLoginUser(Connection conn, String userCode) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        // 如果链接成功
        if (conn != null) {
            String sql = "select * from `smbms_user` where userCode=?";
            Object[] params = {userCode};

            rs = BaseDao.execute(conn, ps, rs, sql, params);
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null, ps, rs);

        }
        return user;
    }

    public int updatePwd(Connection conn, int id, String password) throws SQLException {
        PreparedStatement ps = null;
        int execute = 0 ;

        if (conn != null) {
            String sql = "update smbms_user set userPassword = ? where id  = ?";
            Object[] params = {password, id};
            execute = BaseDao.execute(conn, ps, sql, params);
            BaseDao.closeResource(null, ps, null);
        }

        return execute;
    }

    public int getUserCount(Connection conn, String username, int userRole) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;

        if (conn != null) {
            StringBuffer sql = new StringBuffer("select count(1) as count from smbms_user u, smbms_role r where u.userRole = r.id");
            ArrayList<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole=?");
                list.add(userRole);
            }

            rs = BaseDao.execute(conn, ps, rs, sql.toString(), list.toArray());
            if (rs.next()) {
                count = rs.getInt("count");
            }
        }

        return count;
    }

    public List<User> getUserList(Connection conn, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<User>();
        if(conn != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("sql ----> " + sql.toString());
            rs = BaseDao.execute(conn, pstm, rs, sql.toString(), params);
            while(rs.next()){
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return userList;
    }
}
