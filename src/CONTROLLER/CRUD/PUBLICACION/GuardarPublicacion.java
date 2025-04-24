package CONTROLLER.CRUD.PUBLICACION;

import MODEL.Publicacion;
import MODEL.UTIL.Mensajes;
import MODEL.Usuario;
import VIEW.ERROR.Error_INICIAR_BD;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static DB.UTIL.CrearConn.conn;

/**
 * Clase encargada de la persistencia de publicaciones en la base de datos.
 * Proporciona métodos para guardar publicaciones, ya sea creando nuevas entradas
 * o actualizando entradas existentes en la tabla de publicaciones.
 *
 * <p>Esta clase se conecta a la base de datos y realiza operaciones de inserción o actualización
 * (INSERT o UPDATE) en la tabla <code>PUBLICACIONES</code> dependiendo de las circunstancias.</p>
 *
 * <p><strong>Dependencias:</strong></p>
 * <ul>
 *     <li>La clase depende de los paquetes {@link DB.UTIL.GestorConexion} para gestionar la conexión
 *         a la base de datos y el paquete {@link MODEL.Publicacion} para los modelos de publicaciones.</li>
 *     <li>Utiliza el paquete estándar de <code>java.sql</code> para conectarse y operar con la base de datos.</li>
 * </ul>
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */



public class GuardarPublicacion {
    private static final Logger LOGGER = Logger.getLogger(GuardarPublicacion.class.getName());


    public static String guardarPublicacion(Publicacion publicacion, Usuario usuario, Connection conexion){


        Connection conn = conexion;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }



        //Indicamos la fecha de guardado, la publicacion guardad y el usuario que guarda dicha publicacion, el que ha iniciado sesion
            String sql = "INSERT INTO PUBLICACIONES_GUARDADAS (id_publicacion, id_usuario) VALUES (?, ?)";
        try {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, publicacion.getId_publicacion());
                ps.setInt(2, usuario.getId_usuario());
                ps.executeUpdate();
                return Mensajes.getMensaje(Mensajes.PUBLICACION_GUARDADA);
            }
        } catch (SQLException e) {
            return Mensajes.getMensaje(Mensajes.ERROR_GUARDAR_PUBLICACION) + "ERROR: " + e.getMessage();
        }
    }

    public static String retirarGuardadoPublicacion(Publicacion publicacion, Usuario usuario, Connection conexion){


        Connection conn = conexion;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        //Indicamos la publicacion guardada y el usuario que ha guardado dicha publicacion, el que ha iniciado sesion
        //Y eliminamos la publicacion guardada de la tabla de PUBLICACIONES_GUARDADAS
        String sql = "DELETE FROM PUBLICACIONES_GUARDADAS WHERE id_publicacion = ? AND id_usuario = ?";
        try {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, publicacion.getId_publicacion());
                ps.setInt(2, usuario.getId_usuario());
                ps.executeUpdate();
                return Mensajes.getMensaje(Mensajes.PUBLICACION_RETIRADA);
            }
        } catch (SQLException e) {
            return Mensajes.getMensaje(Mensajes.ERROR_RETIRAR_PUBLICACION);
        }
    }
    public static boolean estaGuardada(Publicacion publicacion, Usuario usuario, Connection conexion) {
        Connection conn = conexion;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Validar que la conexión no sea nula
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
            return false;
        }

        // Consulta SQL para verificar si la publicación está guardada
        String sql = "SELECT COUNT(*) FROM PUBLICACIONES_GUARDADAS WHERE id_publicacion = ? AND id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, publicacion.getId_publicacion());
            ps.setInt(2, usuario.getId_usuario());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Devuelve true si la publicación está guardada
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONSULTA) + " ERROR: " + e.getMessage());
        }
        return false;
    }

}
