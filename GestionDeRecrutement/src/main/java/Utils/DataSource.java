package Utils;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

public class DataSource {

    private static Connection con;
    private static final String URL = "jdbc:mysql://localhost:3306/gestionderecrutement";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        if (con == null) {
            try {
                con = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return con;
    }
}
