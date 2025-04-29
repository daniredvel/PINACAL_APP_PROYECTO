package CONTROLLER.CRUD.PUBLICACION;

import DB.UTIL.GestorConexion;
import MODEL.Publicacion;
import MODEL.UTIL.Mensajes;
import VIEW.ERROR.Error_INICIAR_BD;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static DB.UTIL.CrearConn.conn;

/**
 * Clase encargada de actualizar el estado de validez de una publicación en la base de datos.
 * Proporciona métodos para actualizar el estado de validez de la publicación.
 *
 * <p>Esta clase se conecta a la base de datos y realiza una operación de actualización (UPDATE)
 * en la tabla <code>PUBLICACIONES</code> para validar una publicación.</p>
 *
 * <p><strong>Dependencias:</strong></p>
 * <ul>
 *     <li>La clase depende de los paquetes {@link DB.UTIL.GestorConexion}, {@link MODEL.Publicacion}
 *         y {@link VIEW.ERROR.Error_INICIAR_BD}.</li>
 *     <li>Utiliza el paquete estándar de <code>java.sql</code> para conectarse y operar con la base de datos.</li>
 * </ul>
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */



public class AceptarPublicacion {
    private static final Logger LOGGER = Logger.getLogger(AddPublicacion.class.getName());

    private static Publicacion publicacion;
    public static boolean aceptarPublicacion(Publicacion publicacion) {
    AceptarPublicacion.publicacion  = publicacion;
        Connection conn = GestorConexion.getConexion();

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        String sql = "UPDATE PUBLICACIONES SET aceptada = ? WHERE id_publicacion = ?";

        try {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBoolean(1, true);
                ps.setInt(2, publicacion.getId_publicacion());
                ps.executeUpdate();
                System.out.println(Mensajes.getMensaje(Mensajes.PUBLICACION_ACEPTADA));

                publicacion.setAceptada(true);
                return true;
            }
        } catch (SQLException e) {
            //En caso de error se deniega la publicacion
            publicacion.setAceptada(false);
            System.out.println(Mensajes.getMensaje(Mensajes.ERROR_ACEPTAR_PUBLICACION));
            return false;
        }

    }
}
