package lk.ijse.dep7.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {


    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(){

        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7_customer", "root", "th15@rud@51th");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
