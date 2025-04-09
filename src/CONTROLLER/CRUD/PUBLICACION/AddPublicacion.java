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