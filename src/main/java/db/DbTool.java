package db;

import java.sql.*;

public class DbTool {
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/stockdb?serverTimezone=UTC  ";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "ghjjj520";

    public static Connection getConn() throws Exception {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException e) {
            throw e;
        }
    }

    public static void closeConnection(Connection conn) {
        if(conn != null) {
            try {
                if(!conn.isClosed()) {
                    conn.rollback();
                    conn.close();
                }
            } catch (SQLException var2) {
                System.err.println("Error:" + var2.getMessage());
            }
        }

    }
}
