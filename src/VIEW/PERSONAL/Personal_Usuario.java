package VIEW.PERSONAL;

import CONTROLLER.CRUD.USER.ActualizarUsuario;
import CONTROLLER.ControladorDatos;
import MODEL.Publicacion;
import MODEL.Usuario;
import VIEW.INICIO.Inicio_Vista;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Personal_Usuario extends JFrame {
    private final JTextField nombreField;
    private final JTextField emailField;
    private final JTextField telefonoField;
    private final JLabel messageLabel;
    private static Usuario usuario_actual;
    private static Connection conn;
    protected final DefaultListModel<Publicacion> listModel;
    public static final Logger LOGGER = Logger.getLogger(Inicio_Vista.class.getName());

    public Personal_Usuario(Usuario usuario_actual, Connection conn) {
        Personal_Usuario.conn = conn;
        Personal_Usuario.usuario_actual = usuario_actual;
        setTitle("Personal Usuario");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ajustar la ventana al tamaño de la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Icono
        setIconImage(Rutas.getImage(Rutas.ICONO));

        // Color de fondo
        Color backgroundColor = new Color(211, 205, 192);
        getContentPane().setBackground(backgroundColor);

        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new GridBagLayout());
        panelSuperior.setBackground(backgroundColor);

        Font fuenteButton = new Font("Arial", Font.PLAIN, 18);

        JButton inicioButton = new JButton("Inicio");
        inicioButton.setFont(fuenteButton);
        inicioButton.setBackground(new Color(174, 101, 7));
        inicioButton.setForeground(Color.WHITE);
        inicioButton.setMargin(new Insets(10, 20, 10, 20)); // Ajusta el margen para que se adapte al texto
        inicioButton.setPreferredSize(null); // Permite que el tamaño se ajuste automáticamente

        JButton personalButton = new JButton("Personal");
        personalButton.setFont(fuenteButton);
        personalButton.setBackground(new Color(174, 101, 7));
        personalButton.setForeground(Color.WHITE);
        personalButton.setMargin(new Insets(10, 20, 10, 20)); // Ajusta el margen para que se adapte al texto
        personalButton.setPreferredSize(null); // Permite que el tamaño se ajuste automáticamente

        JButton modificarButton = new JButton("Modificar");
        modificarButton.setFont(fuenteButton);
        modificarButton.setBackground(new Color(174, 101, 7));
        modificarButton.setForeground(Color.WHITE);
        modificarButton.setMargin(new Insets(10, 20, 10, 20)); // Ajusta el margen para que se adapte al texto
        modificarButton.setPreferredSize(null); // Permite que el tamaño se ajuste automáticamente

        JButton aceptarButton = new JButton("Aceptar");
        aceptarButton.setFont(fuenteButton);
        aceptarButton.setBackground(new Color(174, 101, 7));
        aceptarButton.setForeground(Color.WHITE);
        aceptarButton.setMargin(new Insets(10, 20, 10, 20)); // Ajusta el margen para que se adapte al texto
        aceptarButton.setPreferredSize(null); // Permite que el tamaño se ajuste automáticamente
        aceptarButton.setEnabled(false);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setFont(fuenteButton);
        cancelarButton.setBackground(new Color(174, 101, 7));
        cancelarButton.setForeground(Color.WHITE);
        cancelarButton.setMargin(new Insets(10, 20, 10, 20)); // Ajusta el margen para que se adapte al texto
        cancelarButton.setPreferredSize(null); // Permite que el tamaño se ajuste automáticamente
        cancelarButton.setEnabled(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelSuperior.add(inicioButton, gbc);

        gbc.gridx = 1;
        panelSuperior.add(personalButton, gbc);

        gbc.gridx = 2;
        panelSuperior.add(modificarButton, gbc);

        gbc.gridx = 3;
        panelSuperior.add(aceptarButton, gbc);

        gbc.gridx = 4;
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

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(fuenteLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        userPanel.add(emailLabel, gbc);

        emailField = new JTextField(usuario_actual.getEmail());
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setEnabled(false);
        emailField.setColumns(usuario_actual.getEmail().length());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(emailField, gbc);

        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoLabel.setFont(fuenteLabel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        userPanel.add(telefonoLabel, gbc);

        String telefono = Usuario.formatoTelefono(usuario_actual.getTelefono());

        telefonoField = new JTextField(telefono);
        telefonoField.setFont(new Font("Arial", Font.PLAIN, 14));
        telefonoField.setEnabled(false);
        telefonoField.setColumns(telefono.length());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(telefonoField, gbc);

        JLabel cambiarContrasenaLabel = new JLabel("<html><u>Cambiar contraseña</u></html>");
        cambiarContrasenaLabel.setForeground(Color.BLUE);
        cambiarContrasenaLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cambiarContrasenaLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Cambiar_Pass_Vista dialog = new Cambiar_Pass_Vista(Personal_Usuario.this, usuario_actual, conn);
                dialog.setVisible(true);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        userPanel.add(cambiarContrasenaLabel, gbc);

        // Añadir etiqueta para mostrar mensajes de error
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
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

        inicioButton.addActionListener(e -> {
            dispose();
            new Inicio_Vista(usuario_actual, conn).setVisible(true);
        });
        personalButton.addActionListener(e -> {
            dispose();
            new Personal_Usuario(usuario_actual, conn).setVisible(true);
        });
        modificarButton.setEnabled(true);
        modificarButton.addActionListener(e -> {
            nombreField.setEnabled(true);
            emailField.setEnabled(true);
            telefonoField.setEnabled(true);
            aceptarButton.setEnabled(true);
            cancelarButton.setEnabled(true);
            modificarButton.setEnabled(false);
        });

        aceptarButton.addActionListener(e -> {
            if (validateFields()) {
                usuario_actual.setUsuario(nombreField.getText());
                usuario_actual.setEmail(emailField.getText());
                usuario_actual.setTelefono(Usuario.formatoTelefonoBD(telefonoField.getText()));

                String resultado = ActualizarUsuario.actualizarUsuario(usuario_actual);
                JOptionPane.showMessageDialog(null, resultado);

                nombreField.setEnabled(false);
                emailField.setEnabled(false);
                telefonoField.setEnabled(false);
                aceptarButton.setEnabled(false);
                cancelarButton.setEnabled(false);
                modificarButton.setEnabled(true);
            }
        });

        cancelarButton.addActionListener(e -> {
            nombreField.setText(usuario_actual.getUsuario());
            emailField.setText(usuario_actual.getEmail());
            telefonoField.setText(Usuario.formatoTelefono(usuario_actual.getTelefono()));
            nombreField.setEnabled(false);
            emailField.setEnabled(false);
            telefonoField.setEnabled(false);
            aceptarButton.setEnabled(false);
            cancelarButton.setEnabled(false);
            modificarButton.setEnabled(true);
            messageLabel.setText("");
        });

        // Cargar publicaciones
        cargarPublicaciones();
    }

    // PUBLICACIONES GUARDADAS

    protected JScrollPane getJScrollPane() {
        JList<Publicacion> publicacionesList = getPublicacionJList();
        publicacionesList.setBackground(new Color(211, 205, 192)); // Establecer el color de fondo de la lista

        publicacionesList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Publicacion selectedPublicacion = publicacionesList.getSelectedValue();
                    if (selectedPublicacion != null) {
                        Publicacion_Detalle_Vista detalleVista = new Publicacion_Detalle_Vista(Personal_Usuario.this, selectedPublicacion, usuario_actual, conn);
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
                publicacionVista.setBackground(new Color(174, 101, 7));
                publicacionVista.setForeground(Color.WHITE);
            } else {
                publicacionVista.setBackground(new Color(211, 205, 192));
                publicacionVista.setForeground(Color.BLACK);
            }
            return publicacionVista;
        });
        return publicacionesList;
    }

    protected void cargarPublicaciones() {
        LOGGER.log(Level.INFO, "Cargando publicaciones");
        listModel.clear(); // Limpiar la lista antes de recargar

        List<Publicacion> publicaciones = ControladorDatos.obtenerPublicacionesGuardadas(conn, usuario_actual);

        for (Publicacion publicacion : publicaciones) {
            listModel.addElement(publicacion);
        }
        LOGGER.log(Level.INFO, "Publicaciones cargadas: {0}", listModel.size());
    }

    // METODO que valida los campos del formulario
    private boolean validateFields() {
        if (nombreField.getText().isEmpty()) {
            messageLabel.setText("El nombre es obligatorio");
            return false;
        }
        if (emailField.getText().isEmpty()) {
            messageLabel.setText("El email es obligatorio");
            return false;
        }
        if (!emailField.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            messageLabel.setText("Indica un email válido");
            return false;
        }
        if (telefonoField.getText().isEmpty()) {
            messageLabel.setText("El teléfono es obligatorio");
            return false;
        }
        if (!telefonoField.getText().matches("^\\d{3} \\d{3} \\d{3}$")) {
            messageLabel.setText("Indica un teléfono válido en el formato '000 000 000'");
            return false;
        }
        return true;
    }


}