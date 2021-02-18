package pers.ning.dao.role;

import pers.ning.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleDao {
    // 查询角色列表
    List<Role> getRoleList(Connection conn) throws SQLException;
}
