package VIEW.MENSAJES;

import CONTROLLER.ControladorDatos;
import MODEL.Mensaje;
import MODEL.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que representa la vista de la lista de mensajes de la aplicación.
 * Esta clase se encarga de mostrar una lista de mensajes y de permitir al usuario interactuar con ellos.
 * <p>
 * La clase cuenta con un componente de interfaz de usuario para mostrar la lista de mensajes y métodos para manejar los eventos de la lista.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class Mensajes_Lista_Vista extends JPanel {
    public static final Logger LOGGER = Logger.getLogger(Mensajes_Lista_Vista.class.getName());
    private final DefaultListModel<Mensaje> listModel;
    private static Usuario usuario_actual = null;
    private static Connection conn = null;

    public Mensajes_Lista_Vista(Usuario usuario_actual, Connection conexion) {
        LOGGER.log(Level.INFO, "Iniciando vista de mensajes");
        Mensajes_Lista_Vista.usuario_actual = usuario_actual;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conexion;

        setBackground(new Color(211, 205, 192));

        setLayout(new BorderLayout());

        // Panel superior con botones centrados
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(new Color(211, 205, 192));

        // Lista de mensajes en la parte inferior
        listModel = new DefaultListModel<>();
        JScrollPane scrollPane = getJScrollPane();
        scrollPane.setBackground(new Color(211, 205, 192));
        add(scrollPane, BorderLayout.CENTER);

        // Cargar mensajes
        cargarMensajes();

    }

    protected JScrollPane getJScrollPane() {
        setBackground(new Color(211, 205, 192));
        JList<Mensaje> mensajesList = getMensajesJList();
        mensajesList.setBackground(new Color(211, 205, 192));

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

    public void cargarMensajes() {
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