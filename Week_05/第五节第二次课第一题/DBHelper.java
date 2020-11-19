package com.zhao.rocketmq.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBHelper {
    public static final String url = "jdbc:mysql://rm-uf663392qn814i9acmo.mysql.rds.aliyuncs.com:3306/eladmin?useSSL=true";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "351848327";

    public static Connection conn = null;

    public static Connection getConn() {
        try {
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);//获取连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
