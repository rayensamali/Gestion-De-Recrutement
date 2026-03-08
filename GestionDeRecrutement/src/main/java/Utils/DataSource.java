
package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    private static DataSource instance;   // <-- Singleton
    private Connection connection;

    static final String URL = "jdbc:mysql://localhost:3306/gestionderecrutement";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Constructeur privé = personne ne peut créer un objet DataSource
    private DataSource() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // important pour MySQL 8
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection error : " + e.getMessage());
        }
    }

    // Méthode Singleton
    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    // Retourne la connexion unique
    public Connection getConnection() {
        return connection;
    }
}

