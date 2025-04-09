package CONTROLLER.CRUD.USER;

import DB.UTIL.GestorConexion;
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

public class LeerUsuario {
    static String mensaje;
    private static final Logger LOGGER = Logger.getLogger(LeerUsuario.class.getName());

    public static Usuario leerUsuarioPorNombre(String username) {

        Connection conn = GestorConexion.getConexion();

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }


        // Consulta SQL para unir las tablas USUARIOS y TIPOS_USUARIOS
        String sql = "SELECT u.*, t.nombre_tipo FROM USUARIOS u " +
                "JOIN TIPOS_USUARIOS t ON u.id_tipo_usuario = t.id_tipo_usuario " +
                "WHERE u.nombre = ?";
        try {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Consultamos la dirección
                        String direccion = rs.getString("direccion");

                        // Si la direccion no es nula usamos el constructor de la empresa, con direccion
                        Usuario usuario;// Debug
                        if (direccion != null) {
                            usuario = new Usuario(
                                    rs.getInt("id_usuario"),
                                    rs.getString("nombre"),
                                    rs.getString("password"),
                                    rs.getString("email"),
                                    direccion,
                                    rs.getString("telefono"),
                                    rs.getInt("id_tipo_usuario"),
                                    rs.getString("nombre_tipo")
                            );
                        } else {
                            // Si la direccion es nula usamos el constructor del usuario, sin direccion
                            usuario = new Usuario(
                                    rs.getInt("id_usuario"),
                                    rs.getString("nombre"),
                                    rs.getString("password"),
                                    rs.getString("email"),
                                    rs.getString("telefono"),
                                    rs.getInt("id_tipo_usuario"),
                                    rs.getString("nombre_tipo")
                            );
                        }
                        System.out.println("Usuario encontrado: " + usuario.getUsuario()); // Debug
                        return usuario;
                    } else {
                        mensaje = Mensajes.getMensaje(Mensajes.USUARIO_NO_EXISTE);
                        System.out.println("Usuario no encontrado: " + username); // Debug
                    }
                }
            }
        } catch (SQLException e) {
            mensaje = Mensajes.getMensaje(Mensajes.FALLO_CONEXION);
        }
        return null;
    }
}