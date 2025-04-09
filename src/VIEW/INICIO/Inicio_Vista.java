package VIEW.INICIO;

import CONTROLLER.ControladorDatos;
import MODEL.Publicacion;
import MODEL.Usuario;
import VIEW.ADD.Add_Empresa;
import VIEW.ADMINISTRAR.Administar_Vista;
import VIEW.ERROR.Error_INICIAR_BD;
import VIEW.MENSAJES.Mensajes_Lista_Vista;
import VIEW.PERSONAL.Personal_Empresa;
import VIEW.PERSONAL.Personal_Usuario;
import VIEW.PUBLICACIONES.Publicacion_Detalle_Vista;
import VIEW.PUBLICACIONES.Publicacion_Vista;
import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static DB.UTIL.CrearConn.conn;

public class Inicio_Vista extends JFrame {
    public static final Logger LOGGER = Logger.getLogger(Inicio_Vista.class.getName());
    private JButton adminButton;
    protected final DefaultListModel<Publicacion> listModel;
    private static Usuario usuario_actual = null;
    public static Connection conn = null;

    public Inicio_Vista(Usuario usuario_actual, Connection conexion) {
        LOGGER.log(Level.INFO, "Iniciando vista de inicio");
        Inicio_Vista.usuario_actual = usuario_actual;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();
        else Inicio_Vista.conn = conexion;

        // Nos aseguramos de que la conexión no sea nula
        if (conn == null) {
            LOGGER.log(Level.SEVERE, "Conexión nula");
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        // Icono
        setIconImage(Rutas.getImage(Rutas.ICONO));

        setTitle("Inicio Vista");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ajustar la ventana a la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(211, 205, 192)); // Color de fondo

        // Panel superior con botones centrados
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout()); // Usar GridBagLayout para centrar botones
        topPanel.setBackground(new Color(211, 205, 192));

        Font buttonFont = new Font("Arial", Font.PLAIN, 18);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margen entre botones
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Centrar los botones

        // Botón Inicio
        JButton inicioButton = new JButton("Inicio");
        inicioButton.setFont(buttonFont);
        inicioButton.setBackground(new Color(174, 101, 7));
        inicioButton.setForeground(Color.WHITE);
        inicioButton.setPreferredSize(new Dimension(150, 50));
        topPanel.add(inicioButton, gbc);

        // Botón Personal
        gbc.gridx++;
        JButton personalButton = new JButton("Personal");
        personalButton.setFont(buttonFont);
        personalButton.setBackground(new Color(174, 101, 7));
        personalButton.setForeground(Color.WHITE);
        personalButton.setPreferredSize(new Dimension(150, 50));
        topPanel.add(personalButton, gbc);

        // Botón Mis Publicaciones
        gbc.gridx++;
        JButton anadirButton = new JButton("Mis Publicaciones");
        anadirButton.setFont(buttonFont);
        anadirButton.setBackground(new Color(174, 101, 7));
        anadirButton.setForeground(Color.WHITE);
        anadirButton.setPreferredSize(new Dimension(150, 50));
        if (!usuario_actual.getTipo().equalsIgnoreCase(Usuario.getTipos(Usuario.USUARIO))) {
            topPanel.add(anadirButton, gbc);
        }

        // Botón Administrador (solo para administradores)
        if (usuario_actual.getTipo().equalsIgnoreCase(Usuario.getTipos(Usuario.ADMINISTRADOR))) {
            gbc.gridx++;
            adminButton = new JButton("Administrador");
            adminButton.setFont(buttonFont);
            adminButton.setBackground(new Color(174, 101, 7));
            adminButton.setForeground(Color.WHITE);
            adminButton.setPreferredSize(new Dimension(150, 50));
            topPanel.add(adminButton, gbc);
        }

        // Escalar el icono para hacerlo más pequeño
        ImageIcon originalIcon = new ImageIcon(Rutas.getImage(Rutas.MENSAJE)); // Ruta del icono
        Image scaledImage = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Escalar a 30x30 píxeles
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Botón Mensaje
        gbc.gridx++;

        //gbc.weightx = 1.0; // Empujar el botón hacia la derecha
        //gbc.anchor = GridBagConstraints.EAST; // Alinear al extremo derecho
        JButton mensajeButton = new JButton();
        mensajeButton.setIcon(scaledIcon); // Asignar el icono escalado
        mensajeButton.setBackground(new Color(174, 101, 7));
        mensajeButton.setPreferredSize(new Dimension(50, 50));
        mensajeButton.setToolTipText("Mensajes");
        topPanel.add(mensajeButton, gbc);

        add(topPanel, BorderLayout.NORTH);

        // Lista de publicaciones en la parte inferior
        listModel = new DefaultListModel<>();
        JScrollPane scrollPane = getJScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        // Cargar publicaciones
        cargarPublicaciones();

        // «Escuchadores» de los botones
        inicioButton.addActionListener(e -> {
            LOGGER.log(Level.INFO, "Inicio button clicked");
            dispose();
            new Inicio_Vista(usuario_actual, conn).setVisible(true);
        });
        personalButton.addActionListener(e -> {
            LOGGER.log(Level.INFO, "Personal button clicked");
            dispose();
            if (usuario_actual.getTipo().equalsIgnoreCase(Usuario.getTipos(Usuario.EMPRESA_ASOCIADA)) || usuario_actual.getTipo().equalsIgnoreCase(Usuario.getTipos(Usuario.EMPRESA_NO_ASOCIADA)) || usuario_actual.getTipo().equalsIgnoreCase(Usuario.getTipos(Usuario.ADMINISTRADOR))) {
                new Personal_Empresa(usuario_actual, conn).setVisible(true);
            } else if (usuario_actual.getTipo().equalsIgnoreCase(Usuario.getTipos(Usuario.USUARIO))) {
                new Personal_Usuario(usuario_actual, conn).setVisible(true);
            } else {
                new Inicio_Vista(usuario_actual, conn).setVisible(true);
            }
        });

        mensajeButton.addActionListener(e -> {
            LOGGER.log(Level.INFO, "Mensaje button clicked");
            dispose();
            new Mensajes_Lista_Vista(usuario_actual, conn).setVisible(true);
        });

        if (usuario_actual.getTipo().equalsIgnoreCase(Usuario.getTipos(Usuario.EMPRESA_ASOCIADA)) || usuario_actual.getTipo().equalsIgnoreCase(Usuario.getTipos(Usuario.ADMINISTRADOR))) {
            anadirButton.addActionListener(e -> {
                LOGGER.log(Level.INFO, "Mis Publicaciones button clicked");
                dispose();
                new Add_Empresa(usuario_actual, conn).setVisible(true);
            });
        }
        if (usuario_actual.getTipo().equalsIgnoreCase(Usuario.getTipos(Usuario.ADMINISTRADOR))) {
            adminButton.addActionListener(e -> {
                LOGGER.log(Level.INFO, "Administrador button clicked");
                dispose();
                new Administar_Vista(usuario_actual, conn).setVisible(true);
            });
        }
    }

    protected JScrollPane getJScrollPane() {
        JList<Publicacion> publicacionesList = getPublicacionJList();

        publicacionesList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Publicacion selectedPublicacion = publicacionesList.getSelectedValue();
                    if (selectedPublicacion != null) {
                        Publicacion_Detalle_Vista detalleVista = new Publicacion_Detalle_Vista(Inicio_Vista.this, selectedPublicacion, usuario_actual, conn);
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

        List<Publicacion> publicaciones = ControladorDatos.obtenerPublicaciones(conn, false);

        for (Publicacion publicacion : publicaciones) {
            listModel.addElement(publicacion);
        }
        LOGGER.log(Level.INFO, "Publicaciones cargadas: {0}", listModel.size());
    }
}