package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnect {

    private static final String USER = "postgres";
    private static final String PASSWORD = "YXHBCgrqq6h3CQaM";
    private static Connection connection;

    public static synchronized Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed()){
            String url = String.format(
                    "jdbc:postgresql://db.ylmkaiohujgkdpalqdkp.supabase.co:5432/postgres?user=%s&password=%s",
                    USER, PASSWORD
            );

            connection = DriverManager.getConnection(url);
        }
        return connection;
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = getConnection();
        System.out.println(connection);
    }
}