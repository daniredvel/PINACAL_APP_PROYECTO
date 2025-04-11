package CONTROLLER.CRUD.USER;

import DB.UTIL.GestorConexion;
import MODEL.UTIL.Mensajes;
import MODEL.Usuario;
import VIEW.ERROR.Error_INICIAR_BD;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import static DB.UTIL.CrearConn.conn;
import static VIEW.INICIO.Inicio_Vista.LOGGER;

/**
 * Clase encargada de eliminar usuarios a la base de datos.
 *
 * <p>Esta clase se conecta a la base de datos y realiza operaciones de eliminación
 * (DELETE) en la tabla `USUARIOS`.</p>
 *
 * <strong>Dependencias:</strong>
 * <ul>
 *     <li>La clase depende de los paquetes {@link DB.UTIL.GestorConexion} para gestionar la conexión
 *         a la base de datos y el paquete {@link MODEL.Usuario} para los modelos de los usuarios.
 *     </li>
 *     <li>Utiliza el paquete estándar de <code>java.sql</code> para conectarse y operar con la base de datos.</li>
 * </ul>
 *
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class EliminarUsuario {

    public static void eliminarUsuario(Connection conexion, Usuario usuario) {

        Connection conn = GestorConexion.getConexion();

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }




        LOGGER.log(Level.INFO, "Eliminando usuario: {0}", usuario.getUsuario() + "...");
        conn = conexion; // Asignar la conexión correctamente

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, "Conexión nula");
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
            return; // Salir del metodo si la conexión es nula
        }

        String sql = "DELETE FROM USUARIOS WHERE id_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuario.getId_usuario());
            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected); // Debug
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "No se ha podido eliminar el usuario: {0}, {1}", new Object[]{e.getMessage(), e});
        }
    }
}