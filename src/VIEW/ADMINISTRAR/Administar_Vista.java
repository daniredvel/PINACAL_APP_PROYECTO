package VIEW.ADMINISTRAR;

import CONTROLLER.CRUD.PUBLICACION.EliminarPublicacion;
import CONTROLLER.CRUD.USER.ActualizarUsuario;
import CONTROLLER.CRUD.USER.EliminarUsuario;
import CONTROLLER.CRUD.USER.LeerUsuario;
import CONTROLLER.ControladorDatos;
import MODEL.Mensaje;
import MODEL.Publicacion;
import MODEL.UTIL.Mensajes;
import MODEL.Usuario;
import VIEW.ERROR.Error_INICIAR_BD;
import VIEW.INICIO.Inicio_Vista;
import VIEW.RES.Rutas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static VIEW.INICIO.Inicio_Vista.LOGGER;

public class Administar_Vista extends JFrame {
    private List<Publicacion> publicaciones;
    private int currentIndex = 0;
    private final JTextArea publicacionArea;
    private final JTextField justificacionField = new JTextField(20);
    private final JRadioButton denegadaButton;
    private final JRadioButton aceptadaButton;
    private final ButtonGroup group;
    private final JTable table;
    private final DefaultTableModel tableModel;
    public static Connection conn = null;
    private static Usuario usuario_actual;

    public Administar_Vista(Usuario usuario_actual, Connection conexion) {
        Administar_Vista.usuario_actual = usuario_actual;
        LOGGER.log(Level.INFO, "Iniciando vista de administrar");
        Administar_Vista.conn = conexion;

        if (conn == null) {
            LOGGER.log(Level.SEVERE, "Conexión nula");
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        assert conn != null;

        setIconImage(Rutas.getImage(Rutas.ICONO));
        setTitle("Administrar Publicaciones y Usuarios");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(211, 205, 192));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel superior
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(211, 205, 192));
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);

        JButton inicioButton = new JButton("Inicio");
        //JButton personalButton = new JButton("Personal");
        //JButton anadirButton = new JButton("Mis Publicaciones");
        JButton adminButton = new JButton("Administrador");

        /*
        JButton[] buttons = {inicioButton, personalButton, anadirButton, adminButton};
        for (JButton button : buttons) {
            button.setFont(buttonFont);
            button.setBackground(new Color(174, 101, 7));
            button.setForeground(Color.WHITE);
            button.setPreferredSize(new Dimension(150, 50));
            button.setMargin(new Insets(10, 20, 10, 20));
        }

         */
        JButton[] buttons = {inicioButton, adminButton};
        for (JButton button : buttons) {
            button.setFont(buttonFont);
            button.setBackground(new Color(174, 101, 7));
            button.setForeground(Color.WHITE);
            button.setPreferredSize(new Dimension(150, 50));
            button.setMargin(new Insets(10, 20, 10, 20));
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(inicioButton, gbc);
        gbc.gridx = 1;
        /*
        topPanel.add(personalButton, gbc);
        gbc.gridx = 2;
        topPanel.add(anadirButton, gbc);
        gbc.gridx = 3;

         */
        topPanel.add(adminButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(topPanel, gbc);

        // Área de texto para la publicación
        publicacionArea = new JTextArea(10, 50);
        publicacionArea.setFont(new Font("Arial", Font.PLAIN, 18));
        publicacionArea.setEditable(false);
        publicacionArea.setPreferredSize(new Dimension(600, 200));
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        add(publicacionArea, gbc);

        // Botones de radio
        JPanel radioPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        radioPanel.setBackground(new Color(211, 205, 192));

        aceptadaButton = new JRadioButton("Aceptada");
        aceptadaButton.setFont(new Font("Arial", Font.PLAIN, 18));
        aceptadaButton.setBackground(new Color(211, 205, 192));

        denegadaButton = new JRadioButton("Denegada");
        denegadaButton.setFont(new Font("Arial", Font.PLAIN, 18));
        denegadaButton.setBackground(new Color(211, 205, 192));

        group = new ButtonGroup();
        group.add(aceptadaButton);
        group.add(denegadaButton);

        justificacionField.setPreferredSize(new Dimension(justificacionField.getWidth()+50, justificacionField.getHeight()+30));
        aceptadaButton.addActionListener(e -> justificacionField.setEnabled(false));
        denegadaButton.addActionListener(e -> justificacionField.setEnabled(true));

        radioPanel.add(aceptadaButton);
        radioPanel.add(denegadaButton);

        gbc.gridy = 2;
        gbc.gridwidth = 4;
        add(radioPanel, gbc);

        // Etiqueta para el campo de justificación
        gbc.gridy = 3; // Fila 3
        gbc.gridx = 0; // Primera columna
        gbc.gridwidth = 1; // Solo ocupa una columna
        add(new JLabel("Justificación:"), gbc);

        // Campo de justificación
        gbc.gridx = 1; // Segunda columna
        gbc.gridwidth = 3; // Ocupa el resto del ancho
        add(justificacionField, gbc);

        // Botón "Siguiente" debajo del campo de justificación
        JButton siguienteButton = new JButton("Siguiente");
        siguienteButton.setFont(new Font("Arial", Font.PLAIN, 18));
        siguienteButton.setBackground(new Color(174, 101, 7));
        siguienteButton.setForeground(Color.WHITE);

        siguienteButton.addActionListener(e -> {
            if (!aceptadaButton.isSelected() && !denegadaButton.isSelected()) {
                JOptionPane.showMessageDialog(this, "Debe aprobar o denegar la publicación antes de continuar.");
                return;
            }

            if (denegadaButton.isSelected() && justificacionField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe proporcionar una justificación para denegar la publicación.");
                return;
            }

            // Llamar al método gestionarPublicacion antes de avanzar
            gestionarPublicacion();

            if (currentIndex < publicaciones.size() - 1) {
                currentIndex++;
                mostrarPublicacion();
            } else {
                JOptionPane.showMessageDialog(this, "No hay más publicaciones para administrar.");
            }
        });
        JPanel siguientePanel = new JPanel();
        siguientePanel.add(siguienteButton);
        siguientePanel.setBackground(new Color(211, 205, 192));

        gbc.gridy = 4; // Fila 4, debajo del campo de justificación
        gbc.gridx = 0; // Primera columna
        gbc.gridwidth = 4;
        add(siguientePanel, gbc);


        // Tabla de usuarios
        JPanel bottomPanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Usuario", "Email", "Dirección", "Teléfono", "Tipo", "Permisos", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane tableScrollPane = new JScrollPane(table);
        bottomPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setFont(new Font("Arial", Font.PLAIN, 18));


        JButton modificarPermisosButton = new JButton("Modificar Permisos");
        modificarPermisosButton.setFont(new Font("Arial", Font.PLAIN, 18));
        modificarPermisosButton.setBackground(new Color(174, 101, 7));
        modificarPermisosButton.setForeground(Color.WHITE);

        JButton eliminarUsuarioButton = new JButton("Eliminar Usuario");
        eliminarUsuarioButton.setFont(new Font("Arial", Font.PLAIN, 18));
        eliminarUsuarioButton.setBackground(new Color(174, 101, 7));
        eliminarUsuarioButton.setForeground(Color.WHITE);

        buttonPanel.add(modificarPermisosButton);
        buttonPanel.add(eliminarUsuarioButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        add(bottomPanel, gbc);

        cargarDatosUsuarios();

        modificarPermisosButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String usuario = (String) tableModel.getValueAt(selectedRow, 0);
                modificarPermisos(Objects.requireNonNull(LeerUsuario.leerUsuarioPorNombre(usuario)));
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un usuario para modificar los permisos.");
            }
        });

        eliminarUsuarioButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String usuario = (String) tableModel.getValueAt(selectedRow, 0);
                eliminarUsuario(Objects.requireNonNull(LeerUsuario.leerUsuarioPorNombre(usuario)));
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un usuario para eliminar.");
            }
        });

        inicioButton.addActionListener(e -> {
            dispose();
            new Inicio_Vista(usuario_actual, conn).setVisible(true);
        });
        /*
        personalButton.addActionListener(e -> {
            dispose();
            new Personal_Empresa(usuario_actual, conn).setVisible(true);
        });
        anadirButton.addActionListener(e -> {
            dispose();
            new Add_Empresa(usuario_actual, conn).setVisible(true);
        });

         */
        adminButton.addActionListener(e -> {
            dispose();
            new Administar_Vista(usuario_actual, conn).setVisible(true);
        });

        cargarPublicaciones();
    }

    private void mostrarPublicacion() {
        Publicacion publicacion = publicaciones.get(currentIndex);
        publicacionArea.setText(
                "Título:" + publicacion.getTitulo() +
                        "\nTipo:" + publicacion.getTipo() +
                        "\nUsuario:" + publicacion.getUsuario() +
                        "\n\nDescripción:" + publicacion.getDescripcion() +
                        "\n\n\nFecha de publicación:" + publicacion.getFecha_publicacion()
        );
        justificacionField.setText("");
        group.clearSelection();
    }

    private void cargarPublicaciones() {
        publicaciones = ControladorDatos.obtenerPublicaciones(conn, true);
        if (!publicaciones.isEmpty()) {
            mostrarPublicacion();
        } else {
            JOptionPane.showMessageDialog(this, "No hay publicaciones para administrar.");
        }
    }

    private void gestionarPublicacion() {
        String asunto = "Publicación denegada";
        Publicacion publicacion = publicaciones.get(currentIndex);
        if (denegadaButton.isSelected()) {
            String justificacion = justificacionField.getText();
            if (justificacion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe proporcionar una justificación para denegar la publicación.");
                return;
            }
            Mensaje mensaje = new Mensaje(asunto, justificacion, usuario_actual, ControladorDatos.obtenerUsuarioPorNombre(conn, publicacion.getUsuario()), false);
            System.out.println("Mensaje: " + mensaje.toString());
            if (EliminarPublicacion.eliminarPublicacion(publicacion, mensaje , usuario_actual)) {
                System.out.println("Publicación eliminada");
                JOptionPane.showMessageDialog(this, "Publicación denegada y eliminada.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la publicación.");
                System.out.println("Error al eliminar la publicación.");
            }
        }
    }

    private void cargarDatosUsuarios() {
        tableModel.setRowCount(0);
        List<Usuario> usuarios = ControladorDatos.obtenerUsuarios(conn);
        for (Usuario usuario : usuarios) {
            tableModel.addRow(new Object[]{
                    usuario.getUsuario(),
                    usuario.getEmail(),
                    usuario.getDireccion(),
                    usuario.getTelefono(),
                    usuario.getIndice_tipo_usuario(),
                    usuario.getPermisos()
            });
        }
    }

    private void modificarPermisos(Usuario usuario) {
        final String[] opciones = new String[4];
        opciones[Usuario.ADMINISTRADOR] = Usuario.getTipos(Usuario.ADMINISTRADOR);
        opciones[Usuario.EMPRESA_ASOCIADA] = Usuario.getTipos(Usuario.EMPRESA_ASOCIADA);
        opciones[Usuario.EMPRESA_NO_ASOCIADA] = Usuario.getTipos(Usuario.EMPRESA_NO_ASOCIADA);
        opciones[Usuario.USUARIO] = Usuario.getTipos(Usuario.USUARIO);

        String nuevoPermiso = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el nuevo permiso para el usuario: " + usuario.getUsuario(),
                "Modificar Permisos",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                usuario.getPermisos()
        );
        if (nuevoPermiso != null && !nuevoPermiso.equals(usuario.getPermisos())) {
            usuario.setPermisos(nuevoPermiso);

            if (nuevoPermiso.equalsIgnoreCase(opciones[Usuario.ADMINISTRADOR]) ||
                    nuevoPermiso.equalsIgnoreCase(opciones[Usuario.EMPRESA_ASOCIADA]) ||
                    nuevoPermiso.equalsIgnoreCase(opciones[Usuario.EMPRESA_NO_ASOCIADA])) {
                usuario.setDireccion("");
            }

            if (ActualizarUsuario.actualizarUsuario(usuario).equalsIgnoreCase(Mensajes.getMensaje(Mensajes.USUARIO_ACTUALIZADO))) {
                if (nuevoPermiso.equalsIgnoreCase(opciones[Usuario.ADMINISTRADOR])) usuario.setIndice_tipo_usuario(Usuario.ADMINISTRADOR);
                else if (nuevoPermiso.equalsIgnoreCase(opciones[Usuario.EMPRESA_ASOCIADA])) usuario.setIndice_tipo_usuario(Usuario.EMPRESA_ASOCIADA);
                else if (nuevoPermiso.equalsIgnoreCase(opciones[Usuario.EMPRESA_NO_ASOCIADA])) usuario.setIndice_tipo_usuario(Usuario.EMPRESA_NO_ASOCIADA);
                else if (nuevoPermiso.equalsIgnoreCase(opciones[Usuario.USUARIO])) usuario.setIndice_tipo_usuario(Usuario.USUARIO);
                else usuario.setIndice_tipo_usuario(usuario.getIndice_tipo_usuario());

                JOptionPane.showMessageDialog(this, ActualizarUsuario.actualizarUsuario(usuario));
            } else {
                JOptionPane.showMessageDialog(this, Mensajes.getMensaje(Mensajes.ERROR_ACTUALIZAR_USUARIO));
            }
            cargarDatosUsuarios();
        }
    }

    private void eliminarUsuario(Usuario usuario) {
        int response = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar el usuario: " + usuario.getUsuario() + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION
        );
        if (response == JOptionPane.YES_OPTION) {
            EliminarUsuario.eliminarUsuario(conn, usuario);
            JOptionPane.showMessageDialog(this, "Usuario: " + usuario.getUsuario() + " eliminado.");
            cargarDatosUsuarios();
        }
    }

    private void siguientePublicacion() {
        Publicacion publicacion = publicaciones.get(currentIndex);

        if (!aceptadaButton.isSelected() && !denegadaButton.isSelected()) {
            JOptionPane.showMessageDialog(this, "Debe aprobar o denegar la publicación antes de continuar.");
            return;
        }

        if (denegadaButton.isSelected() && justificacionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe proporcionar una justificación para denegar la publicación.");
            return;
        }

        if (currentIndex < publicaciones.size() - 1) {
            currentIndex++;
            mostrarPublicacion();
        } else {
            JOptionPane.showMessageDialog(this, "No hay más publicaciones para administrar.");
        }
    }
}