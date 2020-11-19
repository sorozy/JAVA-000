package com.zhao.rocketmq.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

public class HikaricpDBHelper {
    public static final String url = "jdbc:mysql://rm-uf663392qn814i9acmo.mysql.rds.aliyuncs.com:3306/eladmin?useSSL=true";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "351848327";

    public static Connection conn = null;

    public static Connection getConn() {
        try {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(name);
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            // HikariDataSource 也可以配置
            DataSource ds = new HikariDataSource(config);

            return ds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
