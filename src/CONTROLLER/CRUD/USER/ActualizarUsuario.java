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
import java.util.logging.Logger;

import static DB.UTIL.CrearConn.conn;

public class ActualizarUsuario {
    private static final Logger LOGGER = Logger.getLogger(ActualizarUsuario.class.getName());

    public static String actualizarUsuario(Usuario usuario) {

        Connection conn = GestorConexion.getConexion();

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        //int id, String nombre, String email, String telefono, String password, String calle, String numero, String localidad, String municipio, String provincia, String codigoPostal, String pais

        if (actualizarUsuarioBD(usuario,conn)) return Mensajes.getMensaje(Mensajes.USUARIO_ACTUALIZADO);
        else return Mensajes.getMensaje(Mensajes.ERROR_ACTUALIZAR_USUARIO);
    }

    private static boolean actualizarUsuarioBD(Usuario usuario, Connection conn) {
        String sql = "UPDATE USUARIOS SET nombre = ?, password = ?, email = ?, direccion = ?, telefono = ?, id_tipo_usuario = ? WHERE id_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getDireccion());
            ps.setString(5, Usuario.formatoTelefonoBD(usuario.getTelefono()));
            ps.setInt(6, usuario.getIndice_tipo_usuario());
            ps.setInt(7, usuario.getId_usuario());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el usuario: " + e.getMessage());
            return false;
        }
    }
}