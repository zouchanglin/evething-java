package com.xpu.everything.core.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.xpu.everything.config.EverythingConfig;
import org.apache.commons.io.IOUtils;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据源工厂，此工厂为单例模式
 */
public class DataSourceFactory {
    /**
     * 数据源（单例）
     */
    private static volatile DruidDataSource dataSource;

    private DataSourceFactory() {
    }

    /**
     * 获取数据源
     * @return 数据源
     */
    public static DataSource getDataSource() {
        if (dataSource == null) {
            synchronized (DataSourceFactory.class) {
                if (dataSource == null) {
                    dataSource = new DruidDataSource();
                    dataSource.setDriverClassName("org.h2.Driver");
                    //获取当前工程路径
                    dataSource.setUrl("jdbc:h2:" + EverythingConfig.getInstance().getH2IndexPath());
                    //方式一：dataSource.setValidationQuery("select now()");
                    dataSource.setTestWhileIdle(false);
                }
            }
        }
        return dataSource;
    }

    /**
     * 初始化数据库，此时执行everything.sql里的SQL语句
     */
    public static void initDatabase(){
        //获取数据源
        DataSource dataSource = getDataSource();
        //获取SQL语句
        InputStream inputStream = DataSourceFactory.class.getClassLoader()
                .getResourceAsStream("everything.sql");

        //try-with-resource
        String sql = null;
        try {
            assert inputStream != null;
            sql = IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //执行SQL
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();

            connection.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}