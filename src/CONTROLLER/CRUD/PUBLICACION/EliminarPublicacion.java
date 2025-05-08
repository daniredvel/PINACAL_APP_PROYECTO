


package CONTROLLER.CRUD.PUBLICACION;

import DB.UTIL.GestorConexion;
import MODEL.Mensaje;
import MODEL.Publicacion;
import MODEL.UTIL.Mensajes;
import MODEL.Usuario;
import VIEW.ERROR.Error_INICIAR_BD;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static DB.UTIL.CrearConn.conn;
/**
 * Clase encargada de gestionar la eliminación de publicaciones en la base de datos.
 * Proporciona métodos para eliminar registros en la tabla de publicaciones.
 *
 * <p>Esta clase se conecta a la base de datos y realiza una operación de eliminación (DELETE)
 * en la tabla <code>PUBLICACIONES</code> para eliminar una fila.</p>
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



public class EliminarPublicacion {
    private static final Logger LOGGER = Logger.getLogger(EliminarPublicacion.class.getName());

      public static boolean eliminarPublicacion(Publicacion publicacion, Mensaje mensaje, Usuario de_usuario) {

          Connection conn = GestorConexion.getConexion();

          // Si la conexión es nula, se crea una nueva
          if (conn == null) conn = conn();

          // Nos aseguramos de que la conexión no sea nula
          // Si la conexión es nula, se muestra la ventana de error de la aplicación
          if (conn == null) {
              LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
              SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
          }



        try {
            try {
                // Registrar la justificación en la tabla de justificaciones
                    String insertSQL = "INSERT INTO mensajes (id_usuario_de, id_usuario_para, asunto, contenido, titulo_publicacion) VALUES (?,?,?,?,?)";
                assert conn != null;
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setInt(1, de_usuario.getId_usuario());
                    pstmt.setInt(2, publicacion.getId_usuario());
                    pstmt.setString(3, mensaje.getAsunto());
                    pstmt.setString(4, mensaje.getContenido());
                    pstmt.setString(5, mensaje.getTitulo_Publicacion());
                    pstmt.executeUpdate();
                }

        String sql = "DELETE FROM PUBLICACIONES WHERE id_publicacion = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, publicacion.getId_publicacion());
                int rowsAffected = ps.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
                System.out.println("Publicación eliminada: " + publicacion.getTitulo());
                return rowsAffected > 0;
            }
            } catch (SQLException sqle) {
                System.out.println("Error al eliminar la publicación: " + sqle.getMessage());
                return false;
            }} catch (Exception e) {
            System.out.println("Error al eliminar la publicación: " + e.getMessage());
            return false;
    }}

      public static boolean eliminarPublicacion(Publicacion publicacion) {

          Connection conn = GestorConexion.getConexion();

          // Si la conexión es nula, se crea una nueva
          if (conn == null) conn = conn();

          // Nos aseguramos de que la conexión no sea nula
          // Si la conexión es nula, se muestra la ventana de error de la aplicación
          if (conn == null) {
              LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
              SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
          }


        String sql = "DELETE FROM PUBLICACIONES WHERE id_publicacion = ?";

        try {
            try {

                assert conn != null;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, publicacion.getId_publicacion());
                int rowsAffected = ps.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
                System.out.println("Publicación eliminada: " + publicacion.getTitulo());
                return rowsAffected > 0;
            }
            } catch (SQLException sqle) {
                System.out.println("Error al eliminar la publicación: " + sqle.getMessage());
                return false;
            }} catch (Exception e) {
            System.out.println("Error al eliminar la publicación: " + e.getMessage());
            return false;
    }}

}