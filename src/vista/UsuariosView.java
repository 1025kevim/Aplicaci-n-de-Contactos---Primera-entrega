package vista;

import modelo.Conexion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuariosView extends JFrame {
    // Campos de texto
    public JTextField txtNombre = new JTextField();
    public JTextField txtApellido = new JTextField();
    public JTextField txtCorreo = new JTextField();
    public JTextField txtTelefono = new JTextField();

    // Botones
    public JButton btnNuevo = new JButton("Nuevo");
    public JButton btnGuardar = new JButton("Guardar"); // Added Save button
    public JButton btnActualizar = new JButton("Update");
    public JButton btnEliminar = new JButton("Delete");
    public JButton btnCancelar = new JButton("Cancel");

    // Tabla
    public JTable tablaUsuarios = new JTable();
    public JLabel lblEstado = new JLabel("Conected: Ok");
    public JLabel lblUsuario = new JLabel("Usuario: pepito");

    private String usuarioLogueado; // Store the logged-in username

    public UsuariosView(String usuario) {
        this.usuarioLogueado = usuario; // Store the username
        setTitle("Usuarios");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemCerrar = new JMenuItem("Cerrar");
        menuArchivo.add(itemCerrar);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);

        // Panel superior - formulario
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Listado de usuarios"));
        panelFormulario.add(new JLabel("Nombre"));
        panelFormulario.add(txtNombre);
         panelFormulario.add(new JLabel("Apellido"));
        panelFormulario.add(txtApellido);
        panelFormulario.add(new JLabel("Correo"));
        panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel("Telefono"));
        panelFormulario.add(txtTelefono);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar); // Add Save button
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCancelar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Tabla
        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);

        // Panel inferior - estado y usuario
        JPanel panelInferior = new JPanel(new BorderLayout());
        lblUsuario.setText("Usuario: " + usuario);
        panelInferior.add(lblEstado, BorderLayout.WEST);
        panelInferior.add(lblUsuario, BorderLayout.EAST);

        // Agregar todo al frame
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Acción para "Cerrar" del menú
        itemCerrar.addActionListener(e -> {
            new DashboardView(usuario).setVisible(true);
            dispose(); // cerrar esta vista
        });

        // Acción para el botón "Guardar"
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarContacto();
            }
        });
    }

    private void guardarContacto() {
        try {
            Connection conexion = Conexion.conectar();
            if (conexion != null) {
                String sql = "INSERT INTO usuarios_agenda (nombre, apellido, correo, telefono) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setString(1, txtNombre.getText());
                stmt.setString(2, txtApellido.getText());
                stmt.setString(3, txtCorreo.getText());
                stmt.setString(4, txtTelefono.getText());

                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(null, "Contacto guardado exitosamente.");
                    limpiarCampos(); // Clear the text fields after saving
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo guardar el contacto.");
                }

                stmt.close();
                conexion.close();
            } else {
                JOptionPane.showMessageDialog(null, "Error: No se pudo conectar a la base de datos.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el contacto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // VERY important for debugging!
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
    }
}
