package com.xpu.everything.core.dao;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.io.IOUtils;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataSourceFactory {
    /**
     * 数据源（单例）
     */
    private static volatile DruidDataSource dataSource;

    private DataSourceFactory() {
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            synchronized (DataSourceFactory.class) {
                if (dataSource == null) {
                    dataSource = new DruidDataSource();
                    //dataSource.setDriverClassName("com.mysql");
                    dataSource.setDriverClassName("org.h2.Driver");
                    //获取当前工程路径
                    String workdir = System.getProperty("user.dir");
                    dataSource.setUrl("jdbc:h2:" + workdir + File.separator + "everything");
                }
            }
        }
        return dataSource;
    }
    public static void initDatabase(){
        //获取数据源
        DataSource dataSource = getDataSource();

        //获取SQL语句
        InputStream inputStream = DataSourceFactory.class.getClassLoader()
                .getResourceAsStream("everything.sql");

        //try-with-resource
        String sql = null;
        try {
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

    public static void main(String[] args) {
        initDatabase();
    }

}