package com.zhao.rocketmq.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
@Slf4j
public class JDBCTransactional {
    public static void main(String[] args) {
            //测试时通过后续方法调用

    }

    public  static  void query(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBHelper.getConn();

            //String sql = "UPDATE user SET amount=? WHERE userid=?";
            String sql = "SELECT * from  user" ;
            ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(Integer.MIN_VALUE);
            rs = ps.executeQuery();


        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                rs.close();
                ps.close();
                conn.close();

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public  static  void update(){
        Connection conn = null;
        PreparedStatement  stmt = null;
        ResultSet rs = null;

        try {
            conn = DBHelper.getConn();

            //String sql = "UPDATE user SET amount=? WHERE userid=?";
            String sql = "UPDATE user SET amount=? WHERE userid=?" ;
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, 0);

            stmt.setInt(2, 10);

            stmt.execute();


        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public  static  void delete(){
        Connection conn = null;
        PreparedStatement  stmt = null;
        ResultSet rs = null;

        try {
            conn = DBHelper.getConn();

            //String sql = "UPDATE user SET amount=? WHERE userid=?";
            String sql = "delete from user  WHERE userid=?" ;
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, 0);
            stmt.setDouble(2,10);

            stmt.execute();


        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
    public  static  void insert(){
        Connection conn = null;
        PreparedStatement  stmt = null;
        ResultSet rs = null;

        try {
            conn = DBHelper.getConn();

            //String sql = "UPDATE user SET amount=? WHERE userid=?";
            String sql = "insert into user(userid,amount) values (?,?)" ;
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, 0);


            stmt.execute();


        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public  static  void batch (){
        Connection conn = null;
        Statement stmt = null;
              try {

                      conn.setAutoCommit(false);
                       stmt = conn.createStatement();
                       stmt.addBatch("insert into dept values (1, 'A', 'A')");
                   stmt.addBatch("insert into dept values (2, 'B', 'B')");
                      stmt.addBatch("insert into dept values (3, 'C', 'C')");
                    stmt.executeBatch();
                    conn.commit();
                     conn.setAutoCommit(true);
                 } catch (SQLException e) {
                      e.printStackTrace();

                    try {
                     if (conn != null) {
                           conn.rollback();
                      conn.setAutoCommit(true);
                        }
                    } catch (SQLException e1) {
                         e1.printStackTrace();
                    }

               } finally {
               try {
                          if (stmt != null) {
                                  stmt.close();
                         }
                            if (conn != null) {
                                  conn.close();
                            }
                   } catch (SQLException e) {
                           e.printStackTrace();
                         }
                 }
    }
}
