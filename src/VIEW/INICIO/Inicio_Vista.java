package VIEW.INICIO;

import CONTROLLER.ControladorDatos;
import MODEL.Publicacion;
import MODEL.Usuario;
import VIEW.ERROR.Error_INICIAR_BD;
import VIEW.PUBLICACIONES.Publicacion_Detalle_Vista;
import VIEW.PUBLICACIONES.Publicacion_Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static DB.UTIL.CrearConn.conn;

public class Inicio_Vista extends JPanel {
    private boolean cargando = false; // Evitar múltiples cargas simultáneas
    private boolean hayMasPublicaciones = true; // Controlar si hay más publicaciones por cargar
    private static int OFFSSET = 0;
    public static final Logger LOGGER = Logger.getLogger(Inicio_Vista.class.getName());
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

        setLayout(new BorderLayout());

        // Panel superior con botones centrados
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout()); // Usar GridBagLayout para centrar botones
        topPanel.setBackground(new Color(211, 205, 192));




        // Lista de publicaciones en la parte inferior
        listModel = new DefaultListModel<>();
        JScrollPane scrollPane = getJScrollPane();
        add(scrollPane, BorderLayout.CENTER);

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