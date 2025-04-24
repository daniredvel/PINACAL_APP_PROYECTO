package CONTROLLER.CRUD.PUBLICACION;

import DB.UTIL.GestorConexion;
import MODEL.Publicacion;
import MODEL.UTIL.Mensajes;
import VIEW.ERROR.Error_INICIAR_BD;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import static DB.UTIL.CrearConn.conn;


/**
 * Clase encargada de gestionar la creación de publicaciones en la base de datos.
 * Proporciona métodos para insertar nuevos registros en la tabla de publicaciones.
 *
 * <p>Esta clase se conecta a la base de datos y realiza una operación de inserción (INSERT)
 * en la tabla <code>PUBLICACIONES</code> para crear una nueva entrada.</p>
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




public class AddPublicacion {
    private static final Logger LOGGER = Logger.getLogger(AddPublicacion.class.getName());

    public static boolean crearPublicacion(Publicacion publicacion) {

        Connection conn = GestorConexion.getConexion();

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        String sql = "INSERT INTO PUBLICACIONES (titulo, descripcion, fecha, tipo, id_usuario) VALUES (?, ?, ?, ?, ?)";

        try {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, publicacion.getTitulo());
                ps.setString(2, publicacion.getDescripcion());
                ps.setTimestamp(3, new Timestamp(publicacion.getFecha_publicacion().getTime()));
                ps.setString(4, publicacion.getTipo());
                ps.setInt(5, publicacion.getId_usuario());
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}

