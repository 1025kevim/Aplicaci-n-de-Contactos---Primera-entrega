package vista;

import javax.swing.*;

public class LoginView extends JFrame {
    public JTextField txtUsuario;
    public JPasswordField txtContrasena;
    public JButton btnIngresar, btnCancelar;

    public LoginView() {
        setTitle("Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(50, 50, 100, 30);
        add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(150, 50, 180, 30);
        add(txtUsuario);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(50, 100, 100, 30);
        add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(150, 100, 180, 30);
        add(txtContrasena);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(80, 160, 100, 30);
        add(btnCancelar);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(200, 160, 100, 30);
        add(btnIngresar);
    }
}
