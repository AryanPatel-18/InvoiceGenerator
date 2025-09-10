package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnect {

    private static final String USER = "postgres";
    private static final String PASSWORD = "YXHBCgrqq6h3CQaM";

    public static synchronized Connection getConnection() throws SQLException {
        String url = String.format(
                "jdbc:postgresql://db.ylmkaiohujgkdpalqdkp.supabase.co:5432/postgres?user=%s&password=%s",
                USER, PASSWORD
        );
        return DriverManager.getConnection(url);
    }

}