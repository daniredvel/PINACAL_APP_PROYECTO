package VIEW.ADD;

import CONTROLLER.CRUD.PUBLICACION.AddPublicacion;
import MODEL.Publicacion;
import MODEL.UTIL.Mensajes;
import MODEL.Usuario;
import VIEW.INICIO.Inicio_Vista;
import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Add_Publicacion_Vista extends JDialog {
    private static final Logger LOGGER = Logger.getLogger(Add_Publicacion_Vista.class.getName());
    private final JTextField tituloField;
    private final JTextArea descripcionArea;
    private final JComboBox<String> tipoComboBox;
    private final Usuario usuario_actual;
    private final Connection conn;

    public Add_Publicacion_Vista(Frame owner, Usuario usuario_actual, Connection conexion) {
        super(owner, "Añadir Publicación", true);
        this.conn = conexion;
        this.usuario_actual = usuario_actual;

        // Icono
        setIconImage(Rutas.getImage(Rutas.ICONO));

        setSize(600, 400);
        setLocationRelativeTo(owner);

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(211, 205, 192));

        // Panel central con los campos de entrada
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(new Color(211, 205, 192));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel tituloLabel = new JLabel("Título:");
        tituloLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(tituloLabel, gbc);

        gbc.gridx = 1;
        tituloField = new JTextField(20);
        tituloField.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(tituloField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descripcionLabel = new JLabel("Descripción:");
        descripcionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(descripcionLabel, gbc);

        gbc.gridx = 1;
        descripcionArea = new JTextArea(5, 20);
        descripcionArea.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(descripcionArea);
        centerPanel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel tipoLabel = new JLabel("Tipo:");
        tipoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(tipoLabel, gbc);

        gbc.gridx = 1;
        tipoComboBox = new JComboBox<>(new String[]{"OFERTA", "DEMANDA"});
        tipoComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(tipoComboBox, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con el botón de aceptar
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(211, 205, 192));
        JButton aceptarButton = new JButton("Aceptar");
        aceptarButton.setFont(new Font("Arial", Font.PLAIN, 18));
        aceptarButton.setBackground(new Color(174, 101, 7));
        aceptarButton.setForeground(Color.WHITE);
        aceptarButton.addActionListener(e -> crearPublicacion());
        bottomPanel.add(aceptarButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void crearPublicacion() {
        String titulo = tituloField.getText();
        String descripcion = descripcionArea.getText();
        String tipo = (String) tipoComboBox.getSelectedItem();
        Date fecha_publicacion = new Date();

        // Validación de los campos
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        assert tipo != null;
        Publicacion publicacion = new Publicacion(0, titulo, descripcion, new java.sql.Timestamp(fecha_publicacion.getTime()), tipo, usuario_actual.getId_usuario(), usuario_actual.getUsuario());

        if (AddPublicacion.crearPublicacion(publicacion)) {
            JOptionPane.showMessageDialog(null, Mensajes.getMensaje(Mensajes.PUBLICACION_ANADIDO));
        } else {
            JOptionPane.showMessageDialog(null, Mensajes.getMensaje(Mensajes.ERROR_ANADIR_PUBLICACION));
            LOGGER.log(Level.SEVERE, "Error al crear la publicación: {0}");
        }
        dispose();
        new Inicio_Vista(usuario_actual, conn).setVisible(true);

        LOGGER.log(Level.INFO, "Publicación creada: {0}", publicacion);
        dispose();
        new Inicio_Vista(usuario_actual, conn).setVisible(true);
    }

}