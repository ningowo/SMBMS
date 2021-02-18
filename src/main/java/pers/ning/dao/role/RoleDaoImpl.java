package pers.ning.dao.role;

import pers.ning.dao.BaseDao;
import pers.ning.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    public List<Role> getRoleList(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Role> roleList = new ArrayList<Role>();

        if (conn != null) {
            String sql = "select * from smbms_role";
            Object[] params = {};
            rs = BaseDao.execute(conn, ps, rs, sql, params);
            while (rs.next()) {
                Role _role = new Role();
                _role.setId(rs.getInt("id"));
                _role.setRoleCode(rs.getString("roleCode"));
                _role.setRoleName(rs.getString("roleName"));
                roleList.add(_role);
            }
            BaseDao.closeResource(null, ps, rs);
        }

        return roleList;
    }
}
