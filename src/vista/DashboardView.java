package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardView extends JFrame {
    public JLabel lblEstado = new JLabel("Conectado: Ok");
    public JLabel lblUsuario = new JLabel("Username: pepito");

    public DashboardView(String usuario) {
        setTitle("Agenda de contactos - Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Menú
        JMenuBar menuBar = new JMenuBar();
        JMenu archivo = new JMenu("Archivo");
        JMenu contactos = new JMenu("Contactos");

        JMenuItem itemUsuarios = new JMenuItem("Usuarios");
        JMenuItem itemCerrar = new JMenuItem("Cerrar"); // Este es el botón que vamos a usar
        archivo.add(itemUsuarios);
        archivo.addSeparator();
        archivo.add(itemCerrar);

        contactos.add(new JMenuItem("Listado"));
        menuBar.add(archivo);
        menuBar.add(contactos);
        setJMenuBar(menuBar);

        // Acción al hacer clic en "Cerrar"
        itemCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // cerrar el Dashboard
                new InicioView().setVisible(true); // volver a la pantalla de inicio
            }
        });

        // Centro
        JPanel panelCentro = new JPanel(new GridLayout(1, 2));
        panelCentro.add(new JLabel("Logo", SwingConstants.CENTER));
        panelCentro.add(new JLabel("<html>Lorem Ipsum Lorem Ipsum<br>Lorem Ipsum Lorem Ipsum</html>"));

        // Inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        lblUsuario.setText("Username: " + usuario);
        panelInferior.add(lblEstado, BorderLayout.WEST);
        panelInferior.add(lblUsuario, BorderLayout.EAST);

        add(panelCentro, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
}
