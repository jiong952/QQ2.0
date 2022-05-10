package com.zjh.server.utils;


import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

/**
 * 数据库连接工具类
 *
 * @author 张俊鸿
 * @date 2022/05/10
 */
public class JdbcUtils {
    private static String driverClassName;
    private static String url;
    private static String username;
    private static String password;

    //加载驱动,放在static里，静态加载
    static {
        ClassLoader classLoader = JdbcUtils.class.getClassLoader();
        URL resource = classLoader.getResource("db.properties");
        assert resource != null;
        String path = resource.getPath();
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(path));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("属性文件加载异常");
        }
        driverClassName = properties.getProperty("driverClassName");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**使用用户信息和url进行数据库连接*/

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }

    /**释放连接*/
    public static void release(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        if(rs!=null){
            rs.close();
        }
        if (ps!=null){
            ps.close();
        }
        if(conn!=null){
            conn.close();
        }
    }
    /**关闭conn**/
    public static void close(Connection conn){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
