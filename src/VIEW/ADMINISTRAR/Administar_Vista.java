package VIEW.ADMINISTRAR;

import CONTROLLER.CRUD.PUBLICACION.AceptarPublicacion;
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
import VIEW.RES.Rutas;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Level;

import static VIEW.INICIO.Inicio_Vista.LOGGER;

/**
 * Clase que representa la vista de la ventana de administrador de la app.
 * Esta clase solo se muestra si el usuario es ADMINISTRADOR.
 * <p>
 * Esta clase esta divida en dos paneles.
 * Panel superior: contiene la administración de publicaciones.
 * Panel inferior: contiene la administración de usuarios.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class Administar_Vista extends JPanel {
    private static int OFFSSET = 0;
    private List<Publicacion> publicaciones;
    private Publicacion publicacion;
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
        setBackground(Rutas.getColor(Rutas.GRIS));
        Administar_Vista.usuario_actual = usuario_actual;
        LOGGER.log(Level.INFO, "Iniciando vista de administrar");
        Administar_Vista.conn = conexion;

        if (conn == null) {
            LOGGER.log(Level.SEVERE, "Conexión nula");
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        assert conn != null;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel superior
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Rutas.getColor(Rutas.GRIS));



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
        radioPanel.setBackground(Rutas.getColor(Rutas.GRIS));

        aceptadaButton = new JRadioButton("Aceptada");
        aceptadaButton.setFont(new Font("Arial", Font.PLAIN, 18));
        aceptadaButton.setBackground(Rutas.getColor(Rutas.GRIS));

        denegadaButton = new JRadioButton("Denegada");
        denegadaButton.setFont(new Font("Arial", Font.PLAIN, 18));
        denegadaButton.setBackground(Rutas.getColor(Rutas.GRIS));

        group = new ButtonGroup();
        group.add(aceptadaButton);
        group.add(denegadaButton);

        justificacionField.setPreferredSize(new Dimension(justificacionField.getWidth()+50, justificacionField.getHeight()+30));
        aceptadaButton.addActionListener(e -> {
            justificacionField.setEnabled(false);
            assert publicacion != null;
            publicacion.setAceptada(true);
            publicacion.setAceptada(AceptarPublicacion.aceptarPublicacion(publicacion));
        });
        denegadaButton.addActionListener(e -> {
                justificacionField.setEnabled(true);
                System.out.println(Mensajes.getMensaje(Mensajes.PUBLICACION_DENEGADA));

        });

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
        JPanel siguientePanel = getSiguientePanel();

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
        modificarPermisosButton.setBackground(Rutas.getColor(Rutas.NARANJA));
        modificarPermisosButton.setForeground(Rutas.getColor(Rutas.BLANCO));

        JButton eliminarUsuarioButton = new JButton("Eliminar Usuario");
        eliminarUsuarioButton.setFont(new Font("Arial", Font.PLAIN, 18));
        eliminarUsuarioButton.setBackground(Rutas.getColor(Rutas.NARANJA));
        eliminarUsuarioButton.setForeground(Rutas.getColor(Rutas.BLANCO));

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

        cargarPublicaciones();
    }

    private JPanel getSiguientePanel() {
        JButton siguienteButton = new JButton("Siguiente");
        siguienteButton.setFont(new Font("Arial", Font.PLAIN, 18));
        siguienteButton.setBackground(Rutas.getColor(Rutas.NARANJA));
        siguienteButton.setForeground(Rutas.getColor(Rutas.BLANCO));

        siguienteButton.addActionListener(e -> {
            if (!aceptadaButton.isSelected() && !denegadaButton.isSelected()) {
                JOptionPane.showMessageDialog(this, "Debe aprobar o denegar la publicación antes de continuar.");
                return;
            }

            if (denegadaButton.isSelected() && justificacionField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe proporcionar una justificación para denegar la publicación.");
                return;
            }

            // Llamar al METODO gestionarPublicacion antes de avanzar
            gestionarPublicacion();

            if (currentIndex < publicaciones.size() - 1) {
                currentIndex++;
                System.out.println("1Mostrando publicación " + currentIndex);
                mostrarPublicacion();
            } else {
                JOptionPane.showMessageDialog(this, "No hay más publicaciones para administrar.");
            }
        });
        JPanel siguientePanel = new JPanel();
        siguientePanel.add(siguienteButton);
        siguientePanel.setBackground(Rutas.getColor(Rutas.GRIS));
        return siguientePanel;
    }

    private void mostrarPublicacion() {
        this.publicacion = publicaciones.get(currentIndex);

        //Las publicaciones son denegadas por defecto a la espera de que sean validadas.
        //Si las publicaciones son denegadas se eliminan de la base de datos, por lo que si existen publicaciones con el boolean aceptada en 'false', se entienden como que no han sido validadas.
        if ( publicacion.getAceptada()) {
            currentIndex++;
            System.out.println("2Mostrando publicación " + currentIndex);

            //En caso de que la publicación leida de la base de datos es aceptada, se muestra la siguiente publicación
            mostrarPublicacion();

        } else {
            publicacionArea.setText(
                    "Título:" + publicacion.getTitulo() +
                            "\nTipo:" + publicacion.getTipo() +
                            "\nUsuario:" + publicacion.getUsuario() +
                            "\n\nDescripción:" + publicacion.getDescripcion() +
                            "\n\n\nFecha de publicación:" + publicacion.getFecha_publicacion()
            );
            currentIndex++;
            System.out.println("3Mostrando publicación " + currentIndex);

        }
        justificacionField.setText("");
        group.clearSelection();
    }

    private void cargarPublicaciones() {
        publicaciones = ControladorDatos.obtenerPublicaciones(conn, true, OFFSSET);
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
            System.out.println("Mensaje: " + mensaje);
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
    public void actualizarVista() {
        System.out.println("Actualizando la vista de administrar...");

        // Limpiar los datos actuales
        publicaciones.clear();
        currentIndex = 0;

        // Recargar las publicaciones desde la base de datos
        List<Publicacion> nuevasPublicaciones = ControladorDatos.obtenerPublicaciones(conn, true, 0);

        if (nuevasPublicaciones.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay publicaciones para administrar.");
        } else {
            publicaciones.addAll(nuevasPublicaciones);
            mostrarPublicacion(); // Mostrar la primera publicación
        }

        // Actualizar la interfaz gráfica
        revalidate();
        repaint();

        System.out.println("Vista de administrar actualizada.");
    }
}