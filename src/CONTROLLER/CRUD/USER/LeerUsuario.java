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

/**
 * Clase encargada de obtener los datos de los usuarios desde la base de datos.
 *
 * <p>Esta clase se conecta a la base de datos y realiza operaciones de consulta
 * (SELECT) en la tabla <code>USUARIOS</code>.</p>
 *
 * <p><strong>Dependencias:</strong></p>
 * <ul>
 *     <li>La clase depende de los paquetes {@link DB.UTIL.GestorConexion} para gestionar la conexión
 *         a la base de datos y el paquete {@link MODEL.Usuario} para los modelos de los usuarios.</li>
 *     <li>Utiliza el paquete estándar de <code>java.sql</code> para conectarse y operar con la base de datos.</li>
 * </ul>
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */


public class LeerUsuario {
    static String mensaje;
    private static final Logger LOGGER = Logger.getLogger(LeerUsuario.class.getName());

    public static short NOMBRE = 0;
    public static short EMAIL = 1;

    public static Usuario leerUsuario(String username, int opcion) {

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
        String sql="";

        if (opcion == NOMBRE) {
        sql = "SELECT u.*, t.nombre_tipo FROM USUARIOS u " +
                "JOIN TIPOS_USUARIOS t ON u.id_tipo_usuario = t.id_tipo_usuario " +
                "WHERE u.nombre = ?";
        }
        else if (opcion == EMAIL) {
            sql = "SELECT u.*, t.nombre_tipo FROM USUARIOS u JOIN TIPOS_USUARIOS t ON u.id_tipo_usuario = t.id_tipo_usuario WHERE u.email = ?";
        }


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
                        System.out.println(mensaje);

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
        System.out.println(mensaje);
        return null;
    }
}