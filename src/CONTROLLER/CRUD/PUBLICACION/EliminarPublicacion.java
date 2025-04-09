


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
                    String insertSQL = "INSERT INTO mensajes (id_usuario_de, id_usuario_para, asunto, contenido) VALUES (?,?,?,?)";
                assert conn != null;
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setInt(1, de_usuario.getId_usuario());
                    pstmt.setInt(2, publicacion.getId_usuario());
                    pstmt.setString(3, mensaje.getAsunto());
                    pstmt.setString(4, mensaje.getContenido());
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

      public static boolean eliminarPublicacion(Publicacion publicacion, Usuario de_usuario) {

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