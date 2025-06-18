package vista;

import modelo.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CrearUsuarios extends JFrame {

    private JTextField txtDocumento;
    private JTextField txtNombre;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    private JLabel lblEstado = new JLabel("Conectado: OK");
    private JLabel lblUsuario = new JLabel();
    private String usuarioLogueado;

    public CrearUsuarios(String usuario) {
        this.usuarioLogueado = usuario;

        setTitle("Crear Usuarios");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemCerrar = new JMenuItem("Cerrar");
        itemCerrar.addActionListener(e -> {
            new DashboardView(usuarioLogueado).setVisible(true);
            dispose();
        });
        menuArchivo.add(itemCerrar);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Crear nuevo usuario"));

        txtDocumento = new JTextField();
        txtNombre = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        panelFormulario.add(new JLabel("Documento:"));
        panelFormulario.add(txtDocumento);
        panelFormulario.add(new JLabel("Nombre completo:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Usuario:"));
        panelFormulario.add(txtUsername);
        panelFormulario.add(new JLabel("Contraseña:"));
        panelFormulario.add(txtPassword);

        // Botón guardar
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarUsuario());
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnGuardar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBoton, BorderLayout.SOUTH);

        // Tabla
        String[] columnas = {"ID", "Documento", "Nombre", "Usuario", "Contraseña"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);

        // Estado e info usuario
        lblUsuario.setText("Usuario: " + usuarioLogueado);
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(lblEstado, BorderLayout.WEST);
        panelInferior.add(lblUsuario, BorderLayout.EAST);

        // Agregar al frame
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        cargarUsuariosDesdeBD();
    }

    private void guardarUsuario() {
        String documento = txtDocumento.getText().trim();
        String nombre = txtNombre.getText().trim();
        String usuario = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (documento.isEmpty() || nombre.isEmpty() || usuario.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        Connection conn = Conexion.conectar();
        if (conn == null) return;

        try {
            String sql = "INSERT INTO usuarios (documento, nombre, username, password) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, documento);
            stmt.setString(2, nombre);
            stmt.setString(3, usuario);
            stmt.setString(4, pass);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usuario creado correctamente.");
            limpiarCampos();
            cargarUsuariosDesdeBD();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtDocumento.setText("");
        txtNombre.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
    }

    private void cargarUsuariosDesdeBD() {
        Connection conn = Conexion.conectar();
        if (conn == null) return;

        try {
            String sql = "SELECT id, documento, nombre, username, password FROM usuarios";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            modeloTabla.setRowCount(0);
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("documento"),
                        rs.getString("nombre"),
                        rs.getString("username"),
                        rs.getString("password")
                });
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }
    }
}
