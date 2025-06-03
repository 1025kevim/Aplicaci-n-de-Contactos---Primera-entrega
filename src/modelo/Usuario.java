package modelo;

import java.sql.*;

public class Usuario {
    public static boolean autenticar(String username, String password) {
        Connection con = Conexion.conectar();
        if (con == null) return false;

        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // hay un usuario válido
        } catch (SQLException e) {
            System.out.println("Error al autenticar: " + e.getMessage());
            return false;
        }
    }
}
