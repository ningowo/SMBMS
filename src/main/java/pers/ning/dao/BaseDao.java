package pers.ning.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    static {
        Properties params=new Properties();
        InputStream is=BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            params.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver=params.getProperty("driver");
        url=params.getProperty("url");
        username=params.getProperty("username");
        password=params.getProperty("password");
    }

    // 获取数据库链接
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    // 编写查询公共类
    public static ResultSet execute(Connection conn, PreparedStatement ps, ResultSet rs, String sql, Object[] params) throws SQLException {
        ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i+1, params[i]);
        }
        rs = ps.executeQuery();

        return rs;
    }

    // 编写增删改公共方法
    public static int execute(Connection conn, PreparedStatement ps, String sql, Object[] params) throws SQLException {
        ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i+1, params[i]);
        }
        return ps.executeUpdate();
    }

    public static boolean closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
        boolean flag = true;

        if (conn != null) {
            try {
                conn.close();
                // 关闭完让GC回收一下
                conn = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }

        return flag;
    }

}
