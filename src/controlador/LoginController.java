package controlador;

import vista.InicioView;
import vista.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginController {
    private LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;

        // Acción al presionar "Ingresar"
        this.loginView.btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = loginView.txtUsuario.getText();
                String contrasena = new String(loginView.txtContrasena.getPassword());

                if (validarCredenciales(usuario, contrasena)) {
                    JOptionPane.showMessageDialog(loginView, "¡Bienvenido!");
                    loginView.dispose();
                    new vista.DashboardView(usuario).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(loginView, "Credenciales incorrectas.");
                }
            }
        });

        // Acción al presionar "Cancelar"
        this.loginView.btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginView.dispose();
                new InicioView().setVisible(true);
            }
        });
    }

    private boolean validarCredenciales(String usuario, String contrasena) {
        boolean resultado = false;

        try {
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenda", "root", "");
            String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            resultado = rs.next(); // true si encontró un usuario

            rs.close();
            stmt.close();
            conexion.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + ex.getMessage());
        }

        return resultado;
    }
}
