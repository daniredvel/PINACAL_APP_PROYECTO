package VIEW.PERSONAL;

import CONTROLLER.CRUD.USER.ActualizarUsuario;
import CONTROLLER.ControladorDatos;
import CONTROLLER.VALIDATION.ControladorInicioSesion;
import MODEL.Publicacion;
import MODEL.Usuario;
import VIEW.PUBLICACIONES.Publicacion_Detalle_Vista;
import VIEW.PUBLICACIONES.Publicacion_Vista;
import VIEW.RES.Rutas;
import VIEW.UTIL.Cambiar_Pass_Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;

/**
 * Clase que representa el perfil de un usuario en la aplicación.
 * Esta clase se encarga de almacenar y gestionar la información del usuario.
 * La interfaz muestra la información personal y permite al usuario modificarla.
 * <p>
 * La clase cuenta con atributos para almacenar la información del usuario y métodos para acceder y modificar esa información.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class Personal_Empresa extends JPanel {
    private boolean cargando = false; // Evitar múltiples cargas simultáneas
    private boolean hayMasPublicaciones = true; // Controlar si hay más publicaciones por cargar
    private static int OFFSSET = 0;
    private final JTextField nombreField;
    private final JTextField direccionField;
    private final JLabel messageLabel;
    private static Usuario usuario_actual;
    private static Connection conn;
    protected final DefaultListModel<Publicacion> listModel;

    public Personal_Empresa(Usuario usuario_actual, Connection conn) {
        Personal_Empresa.conn = conn;
        Personal_Empresa.usuario_actual = usuario_actual;


        Color backgroundColor = Rutas.getColor(Rutas.GRIS);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new GridBagLayout());
        panelSuperior.setBackground(backgroundColor);

        Font fuenteButton = new Font("Arial", Font.PLAIN, 18);

        JButton modificarButton = new JButton("Modificar");
        modificarButton.setFont(fuenteButton);
        modificarButton.setBackground(Rutas.getColor(Rutas.NARANJA));
        modificarButton.setForeground(Rutas.getColor(Rutas.BLANCO));
        modificarButton.setMargin(new Insets(10, 20, 10, 20)); // Ajusta el margen para que se adapte al texto
        modificarButton.setPreferredSize(null); // Permite que el tamaño se ajuste automáticamente

        JButton aceptarButton = new JButton("Aceptar");
        aceptarButton.setFont(fuenteButton);
        aceptarButton.setBackground(Rutas.getColor(Rutas.NARANJA));
        aceptarButton.setForeground(Rutas.getColor(Rutas.BLANCO));
        aceptarButton.setMargin(new Insets(10, 20, 10, 20)); // Ajusta el margen para que se adapte al texto
        aceptarButton.setPreferredSize(null); // Permite que el tamaño se ajuste automáticamente
        aceptarButton.setEnabled(false);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setFont(fuenteButton);
        cancelarButton.setBackground(Rutas.getColor(Rutas.NARANJA));
        cancelarButton.setForeground(Rutas.getColor(Rutas.BLANCO));
        cancelarButton.setMargin(new Insets(10, 20, 10, 20)); // Ajusta el margen para que se adapte al texto
        cancelarButton.setPreferredSize(null); // Permite que el tamaño se ajuste automáticamente
        cancelarButton.setEnabled(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 0;
        gbc.gridx = 0;

        panelSuperior.add(modificarButton, gbc);

        gbc.gridx = 1;
        panelSuperior.add(aceptarButton, gbc);

        gbc.gridx = 2;
        panelSuperior.add(cancelarButton, gbc);

        add(panelSuperior, BorderLayout.NORTH);

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridBagLayout());
        userPanel.setBackground(backgroundColor);
        userPanel.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));

        Font fuenteLabel = new Font("Arial", Font.PLAIN, 14);

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(fuenteLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        userPanel.add(nombreLabel, gbc);

        nombreField = new JTextField(usuario_actual.getUsuario());
        nombreField.setFont(new Font("Arial", Font.PLAIN, 14));
        nombreField.setEnabled(false);
        nombreField.setColumns(usuario_actual.getUsuario().length());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(nombreField, gbc);

        JLabel direccionLabel = new JLabel("Dirección:");
        direccionLabel.setFont(fuenteLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        userPanel.add(direccionLabel, gbc);

        direccionField = new JTextField(usuario_actual.getDireccion());
        direccionField.setFont(new Font("Arial", Font.PLAIN, 14));
        direccionField.setEnabled(false);
        try {
            direccionField.setColumns(usuario_actual.getDireccion().length());
        } catch (Exception e) {
            direccionField.setColumns(0);
        }
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(direccionField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(fuenteLabel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        userPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(usuario_actual.getEmail());
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setEnabled(false);
        emailField.setColumns(usuario_actual.getEmail().length());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(emailField, gbc);

        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoLabel.setFont(fuenteLabel);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        userPanel.add(telefonoLabel, gbc);

        String telefono = Usuario.formatoTelefono(usuario_actual.getTelefono());

        JTextField telefonoField = new JTextField(telefono);
        telefonoField.setFont(new Font("Arial", Font.PLAIN, 14));
        telefonoField.setEnabled(false);
        telefonoField.setColumns(telefono.length());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(telefonoField, gbc);

        JLabel cambiarContrasenaLabel = new JLabel("<html><u>Cambiar contraseña</u></html>");
        cambiarContrasenaLabel.setForeground(Rutas.getColor(Rutas.AZUL));
        cambiarContrasenaLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cambiarContrasenaLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Cambiar_Pass_Vista dialog = new Cambiar_Pass_Vista(Personal_Empresa.this, usuario_actual, conn);
                dialog.setVisible(true);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        userPanel.add(cambiarContrasenaLabel, gbc);

        // Añadir etiqueta para mostrar mensajes de error
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Rutas.getColor(Rutas.ROJO));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        userPanel.add(messageLabel, gbc);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.add(userPanel, BorderLayout.NORTH);

        // Lista de publicaciones en la parte inferior
        listModel = new DefaultListModel<>();
        JScrollPane scrollPane = getJScrollPane();
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);


        modificarButton.setEnabled(true);
        modificarButton.addActionListener(e -> {
            boolean admin = usuario_actual.getTipo().equalsIgnoreCase(Usuario.getTipos(Usuario.ADMINISTRADOR));
            if (admin && showPasswordDialog()) {
                nombreField.setEnabled(true);
                direccionField.setEnabled(true);
                aceptarButton.setEnabled(true);
                cancelarButton.setEnabled(true);
                modificarButton.setEnabled(false);
            } else if (!admin) {
                nombreField.setEnabled(true);
                direccionField.setEnabled(true);
                aceptarButton.setEnabled(true);
                cancelarButton.setEnabled(true);
                modificarButton.setEnabled(false);
            }
        });

        aceptarButton.addActionListener(e -> {
            if (validateFields()) {
                usuario_actual.setUsuario(nombreField.getText());
                usuario_actual.setDireccion(direccionField.getText());

                String resultado = ActualizarUsuario.actualizarUsuario(usuario_actual);
                JOptionPane.showMessageDialog(null, resultado);

                nombreField.setEnabled(false);
                direccionField.setEnabled(false);
                aceptarButton.setEnabled(false);
                cancelarButton.setEnabled(false);
                modificarButton.setEnabled(true);
            }
        });

        cancelarButton.addActionListener(e -> {
            nombreField.setText(usuario_actual.getUsuario());
            direccionField.setText(usuario_actual.getDireccion());
            nombreField.setEnabled(false);
            direccionField.setEnabled(false);
            aceptarButton.setEnabled(false);
            cancelarButton.setEnabled(false);
            modificarButton.setEnabled(true);
            messageLabel.setText("");
        });

        // Cargar publicaciones
        cargarPublicaciones();

        // Detectar el final de la lista
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            if (!e.getValueIsAdjusting() && ControladorDatos.esUltimoElementoVisible(scrollPane)) {
                cargarPublicaciones();
                repaint();
            }
        });

    }

    // PUBLICACIONES GUARDADAS

    protected JScrollPane getJScrollPane() {
        JList<Publicacion> publicacionesList = getPublicacionJList();
        publicacionesList.setBackground(Rutas.getColor(Rutas.GRIS)); // Establecer el color de fondo de la lista

        publicacionesList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Publicacion selectedPublicacion = publicacionesList.getSelectedValue();
                    if (selectedPublicacion != null) {
                        Publicacion_Detalle_Vista detalleVista = new Publicacion_Detalle_Vista(Personal_Empresa.this, selectedPublicacion, usuario_actual, conn);
                        detalleVista.setVisible(true);
                    }
                }
            }
        });

        return new JScrollPane(publicacionesList);
    }

    private JList<Publicacion> getPublicacionJList() {
        JList<Publicacion> publicacionesList = new JList<>(listModel);
        publicacionesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        publicacionesList.setCellRenderer((list, publicacion, index, isSelected, cellHasFocus) -> {
            Publicacion_Vista publicacionVista = new Publicacion_Vista(publicacion);
            if (isSelected) {
                publicacionVista.setBackground(Rutas.getColor(Rutas.NARANJA));
                publicacionVista.setForeground(Rutas.getColor(Rutas.BLANCO));
            } else {
                publicacionVista.setBackground(Rutas.getColor(Rutas.GRIS));
                publicacionVista.setForeground(Rutas.getColor(Rutas.NEGRO));
            }
            return publicacionVista;
        });
        return publicacionesList;
    }

    public void cargarPublicaciones() {
        System.out.println("Intentando cargar publicaciones...");
        System.out.println("Estado antes de cargar: cargando=" + cargando + ", hayMasPublicaciones=" + hayMasPublicaciones);

        if (cargando || !hayMasPublicaciones) {
            System.out.println("Carga bloqueada: cargando=" + cargando + ", hayMasPublicaciones=" + hayMasPublicaciones);
            return; // Evitar cargas innecesarias
        }

        cargando = true; // Marcar como cargando
        System.out.println("Cargando publicaciones con offset=" + OFFSSET + ", limit=" + ControladorDatos.LIMIT);

        List<Publicacion> publicaciones = ControladorDatos.obtenerPublicacionesGuardadas(conn, usuario_actual, OFFSSET);

        if (publicaciones.isEmpty()) {
            System.out.println("No se encontraron más publicaciones.");
            hayMasPublicaciones = false; // No hay más publicaciones por cargar
        } else {
            for (Publicacion publicacion : publicaciones) {
                listModel.addElement(publicacion);
            }
            System.out.println("OFFSET=" + OFFSSET);

            if(!publicaciones.isEmpty()) OFFSSET += publicaciones.size(); //Solo se recarga el offset si hay publicaciones


            System.out.println("OFFSET=" + OFFSSET);
            System.out.println("Publicaciones cargadas: " + publicaciones.size() + ", nuevo offset=" + OFFSSET);
        }

        cargando = false; // Finalizar la carga
        repaint();
        System.out.println("Carga finalizada.");
    }


    // METODO que valida los campos del formulario
    private boolean validateFields() {
        if (nombreField.getText().isEmpty()) {
            messageLabel.setText("El nombre es obligatorio");
            return false;
        }
        if (direccionField.getText().isEmpty()) {
            messageLabel.setText("La dirección es obligatoria");
            return false;
        }
        return true;
    }
    // METODO que muestra el diálogo de contraseña
    private boolean showPasswordDialog() {
        JPasswordField passwordField = new JPasswordField();
        JCheckBox showPasswordCheckBox = new JCheckBox("Mostrar contraseña");

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(passwordField, BorderLayout.CENTER);
        panel.add(showPasswordCheckBox, BorderLayout.SOUTH);

        int option = JOptionPane.showConfirmDialog(this, panel, "Introduce la contraseña de administrador", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            try {
                int result = ControladorInicioSesion.comprobarPass(usuario_actual.getUsuario(), password);
                return result == 1;
            } catch (Exception e) {
                System.out.println("Error al comprobar la contraseña: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
}