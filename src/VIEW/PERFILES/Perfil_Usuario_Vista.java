package VIEW.PERFILES;

import CONTROLLER.ControladorDatos;
import MODEL.Publicacion;
import MODEL.Usuario;
import VIEW.PUBLICACIONES.Publicacion_Detalle_Vista;
import VIEW.PUBLICACIONES.Publicacion_Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Clase que representa la vista del perfil de un usuario a través de la aplicación.
 * Esta clase se encarga de mostrar la información del usuario.
 * <p>
 * La clase cuenta con componentes de interfaz de usuario para mostrar la información del mismo.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class Perfil_Usuario_Vista extends JPanel {

    private boolean cargando = false; // Evitar múltiples cargas simultáneas
    private boolean hayMasPublicaciones = true; // Controlar si hay más publicaciones por cargar

    public static final Logger LOGGER = Logger.getLogger(Perfil_Usuario_Vista.class.getName());
    private static int OFFSSET = 0;
    private final Usuario usuario; // Usuario cuyo perfil se está viendo
    private final DefaultListModel<Publicacion> listModel; // Modelo de la lista de publicaciones
    private final Connection conn; // Conexión a la base de datos
    private final Usuario usuario_actual; // Usuario que ha iniciado sesión

    public Perfil_Usuario_Vista(Connection conn, Usuario usuario, Usuario usuario_actual) {
        this.conn = conn;
        this.usuario = usuario;
        this.usuario_actual = usuario_actual;

        // Lista de publicaciones en la parte inferior
        this.listModel = new DefaultListModel<>();
        JScrollPane scrollPane = getJScrollPane();
        add(scrollPane, BorderLayout.CENTER);



        cargarPublicaciones();

        // Detectar el final de la lista
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            if (!e.getValueIsAdjusting() && ControladorDatos.esUltimoElementoVisible(scrollPane)) {
                cargarPublicaciones();
            }
        });

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(211, 205, 192));

        // Panel de información del usuario
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(211, 205, 192));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nombreLabel = new JLabel("Nombre: " + usuario.getUsuario());
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(nombreLabel, gbc);

        if (usuario.getDireccion() != null) {
            JLabel direccionLabel = new JLabel("Dirección: " + usuario.getDireccion());
            direccionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            gbc.gridy = 1;
            infoPanel.add(direccionLabel, gbc);
        }

        JLabel telefonoLabel = new JLabel("Teléfono: " + usuario.getTelefono());
        telefonoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 2;
        infoPanel.add(telefonoLabel, gbc);

        JLabel emailLabel = new JLabel("Correo Electrónico: " + usuario.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 3;
        infoPanel.add(emailLabel, gbc);

        add(infoPanel, BorderLayout.NORTH);
        add(getJScrollPane(), BorderLayout.CENTER);
    }

    private JScrollPane getJScrollPane() {
        JList<Publicacion> publicacionesList = getPublicacionJList();
        publicacionesList.setBackground(new Color(211, 205, 192));

        publicacionesList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Publicacion selectedPublicacion = publicacionesList.getSelectedValue();
                    if (selectedPublicacion != null) {
                        Publicacion_Detalle_Vista detalleVista = new Publicacion_Detalle_Vista(null, selectedPublicacion, usuario_actual, conn);
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
        System.out.println("Intentando cargar publicaciones...");
        System.out.println("Estado antes de cargar: cargando=" + cargando + ", hayMasPublicaciones=" + hayMasPublicaciones);

        if (cargando || !hayMasPublicaciones) {
            System.out.println("Carga bloqueada: cargando=" + cargando + ", hayMasPublicaciones=" + hayMasPublicaciones);
            return; // Evitar cargas innecesarias
        }

        cargando = true; // Marcar como cargando
        System.out.println("Cargando publicaciones con offset=" + OFFSSET + ", limit=" + ControladorDatos.LIMIT);

        List<Publicacion> publicaciones = ControladorDatos.obtenerPublicaciones(conn, false, OFFSSET);

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

}