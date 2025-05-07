package VIEW.INICIO;

import CONTROLLER.ControladorDatos;
import MODEL.Publicacion;
import MODEL.Usuario;
import VIEW.ERROR.Error_INICIAR_BD;
import VIEW.PERSONAL.Personal_Empresa;
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

/**
 * Clase que representa la vista de la ventana de inicio de la aplicación.
 * Esta clase se encarga de mostrar al usuario las publicaciones del administrador y de la empresa asociada.
 * <p>
 * La clase cuenta con varias variables de instancia para controlar el estado de la vista, como {@link #cargando}, {@link #hayMasPublicaciones} y {@link #OFFSSET}.
 * <p>
 * La clase también cuenta con varios métodos para cargar y gestionar las publicaciones, como {@link #cargarPublicaciones()} y {@link #getJScrollPane()}.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */


public class Inicio_Vista extends JPanel {
    private boolean cargando = false; // Evitar múltiples cargas simultáneas
    private boolean hayMasPublicaciones = true; // Controlar si hay más publicaciones por cargar
    private static int OFFSSET = 0;
    public static final Logger LOGGER = Logger.getLogger(Inicio_Vista.class.getName());
    protected static DefaultListModel<Publicacion> listModel;
    private static Usuario usuario_actual = null;
    public static Connection conn = null;

    public void setListModel(DefaultListModel<Publicacion> listModel) {
        Inicio_Vista.listModel = listModel;
    }


    public DefaultListModel<Publicacion> getListModel() {
        return listModel;
    }

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
        topPanel.setBackground(Rutas.getColor(Rutas.GRIS));




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

    /**
     * Metodo que se encarga de cargar las publicaciones de la aplicación.
     * <p>
     * Este metodo verifica si se están cargando publicaciones actualmente o si no hay más publicaciones por cargar.
     * Si se cumplen estas condiciones, no se carga nada.
     * <p>
     * De lo contrario, se procede a cargar las publicaciones y se actualiza el offset para la próxima carga.
     *
     * @see #cargando
     * @see #hayMasPublicaciones
     * @see #OFFSSET
     */

    public void cargarPublicaciones() {
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

    public boolean getCargando() {
        return cargando;
    }

    public void setCargando(boolean cargando) {
        this.cargando = cargando;
    }

    public boolean getHayMasPublicaciones() {
        return hayMasPublicaciones;
    }

    public void setHayMasPublicaciones(boolean hayMasPublicaciones) {
        this.hayMasPublicaciones = hayMasPublicaciones;
    }

    public static int getOFFSSET() {
        return OFFSSET;
    }

    public static void setOFFSSET(int OFFSSET) {
        Inicio_Vista.OFFSSET = OFFSSET;
    }
    public static void limpiar() {
        listModel.clear();
    }
}