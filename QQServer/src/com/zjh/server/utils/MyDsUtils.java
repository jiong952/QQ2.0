package com.zjh.server.utils;

import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Mono
 */
public class MyDsUtils {
    private static final DataSource dataSource;
    static{
        InputStream in = MyDsUtils.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("属性文件加载异常");
        }
        String driverClassName = properties.getProperty("driverClassName");
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        String poolSizeString = properties.getProperty("poolSize");
        int poolSize=Integer.parseInt(poolSizeString);
        dataSource = new MyDataSource(driverClassName, url, username,password,poolSize);

    }

    /**

     * 直接获取一个Connection

     */

    public static Connection getConnection(){

        Connection con ;

        try {

            con= dataSource.getConnection();

        } catch (SQLException e) {

            throw new RuntimeException(e.getMessage(),e);

        }

        return con;

    }

    /**获取一个DataSource*/

    public static DataSource getDataSource(){

        return dataSource;

    }

    public static QueryRunner queryRunner = new QueryRunner(dataSource);
}
