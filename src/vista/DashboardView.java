package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import vista.CrearUsuarios;
import vista.UsuariosView;

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
        JMenuItem itemCerrar = new JMenuItem("Cerrar");
        JMenuItem itemListado = new JMenuItem("Listado");

        archivo.add(itemUsuarios);
        archivo.addSeparator();
        archivo.add(itemCerrar);

        contactos.add(itemListado);

        menuBar.add(archivo);
        menuBar.add(contactos);
        setJMenuBar(menuBar);

        // Acción al hacer clic en "Usuarios"
        itemUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CrearUsuarios(usuario).setVisible(true); // ✅ Se pasa el usuario
                dispose(); // Opcional: cerrar dashboard
            }
        });

        // Acción al hacer clic en "Cerrar"
        itemCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new InicioView().setVisible(true); // Volver a la pantalla de login
            }
        });

        // Acción al hacer clic en "Listado" de contactos
        itemListado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UsuariosView(usuario).setVisible(true); // Abre agenda de contactos
                dispose(); // Opcional: cerrar dashboard
            }
        });

        // Panel central
        JPanel panelCentro = new JPanel(new GridLayout(1, 2));
        panelCentro.add(new JLabel("Logo", SwingConstants.CENTER));
        panelCentro.add(new JLabel("<html><h3>Bienvenido a la Agenda</h3><p>Selecciona una opción en el menú</p></html>", SwingConstants.CENTER));

        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        lblUsuario.setText("Username: " + usuario);
        panelInferior.add(lblEstado, BorderLayout.WEST);
        panelInferior.add(lblUsuario, BorderLayout.EAST);

        // Agregar al JFrame
        add(panelCentro, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
}
