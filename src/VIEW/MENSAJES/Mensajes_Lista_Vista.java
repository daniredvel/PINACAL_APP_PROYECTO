package VIEW.MENSAJES;

import CONTROLLER.ControladorDatos;
import MODEL.Mensaje;
import MODEL.Usuario;
import VIEW.INICIO.Inicio_Vista;
import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mensajes_Lista_Vista extends JFrame {
    public static final Logger LOGGER = Logger.getLogger(Mensajes_Lista_Vista.class.getName());
    private final DefaultListModel<Mensaje> listModel;
    private static Usuario usuario_actual = null;
    private static Connection conn = null;

    public Mensajes_Lista_Vista(Usuario usuario_actual, Connection conexion) {
        LOGGER.log(Level.INFO, "Iniciando vista de mensajes");
        Mensajes_Lista_Vista.usuario_actual = usuario_actual;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conexion;

        // Icono
        setIconImage(Rutas.getImage(Rutas.ICONO));

        setTitle("Mensajes");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ajustar la ventana a la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(new Color(211, 205, 192));

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(211, 205, 192)); // Color de fondo

        // Panel superior con botones centrados
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(new Color(211, 205, 192));

        Font buttonFont = new Font("Arial", Font.PLAIN, 18);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Botón Volver
        JButton volverButton = new JButton("Inicio");
        volverButton.setFont(buttonFont);
        volverButton.setBackground(new Color(174, 101, 7));
        volverButton.setForeground(Color.WHITE);
        volverButton.setPreferredSize(new Dimension(150, 50));
        topPanel.add(volverButton, gbc);

        add(topPanel, BorderLayout.NORTH);

        // Lista de mensajes en la parte inferior
        listModel = new DefaultListModel<>();
        setBackground(new Color(211, 205, 192));
        JScrollPane scrollPane = getJScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        // Cargar mensajes
        cargarMensajes();

        // Acción del botón Volver
        volverButton.addActionListener(e -> {
            LOGGER.log(Level.INFO, "Volver button clicked");
            dispose();
            new Inicio_Vista(usuario_actual, conn).setVisible(true);
        });
    }

    protected JScrollPane getJScrollPane() {
        setBackground(new Color(211, 205, 192));
        JList<Mensaje> mensajesList = getMensajesJList();

        mensajesList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Mensaje selectedMensaje = mensajesList.getSelectedValue();
                    if (selectedMensaje != null) {
                        Mensajes_Vista detalleVista = new Mensajes_Vista(selectedMensaje);
                        selectedMensaje.setLeido(true);
                        ControladorDatos.marcarComoLeido(selectedMensaje, conn);
                        detalleVista.setVisible(true);
                    }
                }
            }
        });

        return new JScrollPane(mensajesList);
    }

    private JList<Mensaje> getMensajesJList() {
        setBackground(new Color(211, 205, 192));

        JList<Mensaje> mensajesList = new JList<>(listModel);
        mensajesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mensajesList.setCellRenderer((list, mensaje, index, isSelected, cellHasFocus) -> {
            Mensajes_Vista mensajeVista = new Mensajes_Vista(mensaje);
            if (isSelected) {
                mensajeVista.setBackground(new Color(174, 101, 7));
                mensajeVista.setForeground(Color.WHITE);
            } else {
                mensajeVista.setBackground(new Color(211, 205, 192));
                mensajeVista.setForeground(Color.BLACK);
            }
            return mensajeVista;
        });
        return mensajesList;
    }

    protected void cargarMensajes() {
        setBackground(new Color(211, 205, 192));

        LOGGER.log(Level.INFO, "Cargando mensajes");
        listModel.clear(); // Limpiar la lista antes de recargar

        // Obtener mensajes del usuario actual
        List<Mensaje> mensajes = ControladorDatos.obtenerMensajes(conn, usuario_actual);

        for (Mensaje mensaje : mensajes) {
            listModel.addElement(mensaje);
        }
        LOGGER.log(Level.INFO, "Mensajes cargados: {0}", listModel.size());
    }
}