package lk.ijse.dep7.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBConnectionPoll {
    
    
    private static final int MAX_NUMBER = 4;
    private static   ArrayList<Connection> connectionspoll = new ArrayList<>();
    private static  ArrayList<Connection> concumerpoll = new ArrayList<>();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < MAX_NUMBER; i++) {

                connectionspoll.add(DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7_customer", "root", "th15@rud@51th"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static Connection getConnection(){
        Connection connection = connectionspoll.get(0);
        concumerpoll.add(connection);
        connectionspoll.remove(connection);
        return connection;

    }

    public static void releaseConnection(Connection connection){
        concumerpoll.remove(connection);
        connectionspoll.add(connection);
    }

    public static void releaseAllConnection(){
        for (Connection connection : concumerpoll) {
                connectionspoll.add(connection);

        }
        connectionspoll.clear();
    }
}
