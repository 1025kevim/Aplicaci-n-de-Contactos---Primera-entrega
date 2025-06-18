package vista;

import modelo.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UsuariosView extends JFrame {
    // Campos de texto
    private JTextField txtNombre = new JTextField();
    private JTextField txtApellido = new JTextField();
    private JTextField txtCorreo = new JTextField();
    private JTextField txtTelefono = new JTextField();

    // Botones
    private JButton btnNuevo = new JButton("Nuevo");
    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnActualizar = new JButton("Actualizar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnCancelar = new JButton("Cancelar");

    // Tabla
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    private JLabel lblEstado = new JLabel("Conectado: Ok");
    private JLabel lblUsuario = new JLabel();

    private String usuarioLogueado;
    private int idSeleccionado = -1;

    public UsuariosView(String usuario) {
        this.usuarioLogueado = usuario;

        setTitle("Agenda de Contactos");
        setSize(700, 500);
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
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del contacto"));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Apellido:"));
        panelFormulario.add(txtApellido);
        panelFormulario.add(new JLabel("Correo:"));
        panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCancelar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "Correo", "Teléfono"}, 0);
        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);

        // Panel inferior - estado y usuario
        JPanel panelInferior = new JPanel(new BorderLayout());
        lblUsuario.setText("Usuario: " + usuario);
        panelInferior.add(lblEstado, BorderLayout.WEST);
        panelInferior.add(lblUsuario, BorderLayout.EAST);

        // Agregar al frame
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Eventos
        itemCerrar.addActionListener(e -> {
            new DashboardView(usuario).setVisible(true);
            dispose();
        });

        btnGuardar.addActionListener(e -> guardarContacto());
        btnActualizar.addActionListener(e -> actualizarContacto());
        btnEliminar.addActionListener(e -> eliminarContacto());
        btnCancelar.addActionListener(e -> limpiarCampos());
        btnNuevo.addActionListener(e -> limpiarCampos());

        tablaUsuarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaUsuarios.getSelectedRow();
                if (fila >= 0) {
                    idSeleccionado = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtApellido.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtCorreo.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtTelefono.setText(modeloTabla.getValueAt(fila, 4).toString());
                }
            }
        });

        // Cargar al iniciar
        cargarContactos();
    }

    private void guardarContacto() {
        try (Connection conexion = Conexion.conectar()) {
            String sql = "INSERT INTO usuarios_agenda (nombre, apellido, correo, telefono) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, txtNombre.getText());
            stmt.setString(2, txtApellido.getText());
            stmt.setString(3, txtCorreo.getText());
            stmt.setString(4, txtTelefono.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Contacto guardado.");
            limpiarCampos();
            cargarContactos();
        } catch (SQLException e) {
            mostrarError("guardar el contacto", e);
        }
    }

    private void actualizarContacto() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto para actualizar.");
            return;
        }

        try (Connection conexion = Conexion.conectar()) {
            String sql = "UPDATE usuarios_agenda SET nombre=?, apellido=?, correo=?, telefono=? WHERE id=?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, txtNombre.getText());
            stmt.setString(2, txtApellido.getText());
            stmt.setString(3, txtCorreo.getText());
            stmt.setString(4, txtTelefono.getText());
            stmt.setInt(5, idSeleccionado);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Contacto actualizado.");
            limpiarCampos();
            cargarContactos();
        } catch (SQLException e) {
            mostrarError("actualizar el contacto", e);
        }
    }

    private void eliminarContacto() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el contacto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conexion = Conexion.conectar()) {
            String sql = "DELETE FROM usuarios_agenda WHERE id=?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idSeleccionado);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Contacto eliminado.");
            limpiarCampos();
            cargarContactos();
        } catch (SQLException e) {
            mostrarError("eliminar el contacto", e);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        idSeleccionado = -1;
    }

    private void cargarContactos() {
        modeloTabla.setRowCount(0); // limpiar tabla
        try (Connection conexion = Conexion.conectar()) {
            String sql = "SELECT * FROM usuarios_agenda";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                });
            }
        } catch (SQLException e) {
            mostrarError("cargar los contactos", e);
        }
    }

    private void mostrarError(String accion, SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al " + accion + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
