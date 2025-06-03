package vista;

import controlador.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InicioView extends JFrame {
    public JButton btnLogin;

    public InicioView() {
        setTitle("Agenda de contactos");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel central con logo y texto
        JPanel panelCentro = new JPanel(new GridLayout(1, 2));
        JLabel lblLogo = new JLabel("Logo", SwingConstants.CENTER);
        lblLogo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel lblTexto = new JLabel("<html>Lorem Ipsum Lorem Ipsum<br>Lorem Ipsum Lorem Ipsum</html>", SwingConstants.LEFT);
        panelCentro.add(lblLogo);
        panelCentro.add(lblTexto);
        add(panelCentro, BorderLayout.CENTER);

        // Panel inferior con botón
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        btnLogin = new JButton("Ingresar");
        panelInferior.add(btnLogin);
        add(panelInferior, BorderLayout.SOUTH);

        // Acción del botón
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginView loginView = new LoginView();
                new LoginController(loginView);
                loginView.setVisible(true);
                dispose(); // Cerramos esta ventana
            }
        });
    }
}
