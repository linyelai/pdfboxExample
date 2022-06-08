package com.linseven.test;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.sql.*;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/6/7 17:09
 */
public class MySQLDBApp {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        String url = "jdbc:mysql://127.0.0.1:3306/test?serverTimezone=UTC";
        String username = "root";
        String password = "123456";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        con.setAutoCommit(false);
        PreparedStatement preparedStatement = con.prepareStatement("insert into bank values(?,?)");
        preparedStatement.setString(1,"jimmy");
        preparedStatement.setInt(2,100);
        boolean flag = preparedStatement.execute();
        System.out.println(flag);
        ResultSet resultSet =  preparedStatement.getResultSet();
        System.out.println(resultSet);
        con.commit();
        con.setSavepoint();
        resultSet =  preparedStatement.getResultSet();
        System.out.println(resultSet);
    }
}
