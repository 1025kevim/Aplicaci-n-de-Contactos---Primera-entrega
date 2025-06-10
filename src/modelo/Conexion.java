package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane; // Import JOptionPane

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/agenda?useSSL=false&serverTimezone=UTC"; // Added options
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection conectar() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure the driver is loaded
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
            return connection;
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Show error message
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: MySQL JDBC Driver not found.  Make sure it's in your classpath.");
            JOptionPane.showMessageDialog(null, "Error: MySQL JDBC Driver not found.  Make sure it's in your classpath.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static void main(String[] args) { // Simple test
        conectar();
    }
}
