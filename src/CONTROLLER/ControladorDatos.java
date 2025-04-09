package CONTROLLER;

import CONTROLLER.ENCRIPTACION.ControladorEncriptacion;
import MODEL.Mensaje;
import MODEL.Publicacion;
import MODEL.UTIL.Mensajes;
import MODEL.Usuario;
import VIEW.ERROR.Error_INICIAR_BD;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static DB.UTIL.CrearConn.conn;

public class ControladorDatos {
    private static final Logger LOGGER = Logger.getLogger(ControladorDatos.class.getName());

    // Obtener todas las publicaciones
    /**
     * @param empresaAsociada Indica si el usuario es una empresa asociada o Administrador
     *  True -> Obtiene todas las publicaciones. False -> Solo obtiene las publicaciones de las empresas asocidas
     * */

    // Metodo para obtener todas las publicaciones
    public static List<Publicacion> obtenerPublicaciones(Connection conexion, boolean empresaAsociada) {
        Connection conn = conexion;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = getSql(empresaAsociada);

        try {
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement(sql);
            try (stmt; ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Publicacion publicacion = new Publicacion(
                            rs.getInt("id_publicacion"),
                            rs.getString("titulo"),
                            rs.getString("descripcion"),
                            rs.getTimestamp("fecha"),
                            rs.getString("tipo"),
                            rs.getInt("id_usuario"),
                            rs.getString("usuario")
                    );
                    publicaciones.add(publicacion);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, Mensajes.getMensaje(Mensajes.ERROR_CARGAR_PUBLICACIONES) + "{0}, {1}", new Object[]{e.getMessage(), e});
            // Reabrir la conexión si se cierra debido a un error
        }

        return publicaciones;
    }

    //SQL en funcion de si es una empresa asociada o no
    private static String getSql(boolean empresaAsociada) {
        String sql;
        if (empresaAsociada) {
            sql = "SELECT p.*, u.nombre AS usuario FROM PUBLICACIONES p " +
                    "JOIN USUARIOS u ON p.id_usuario = u.id_usuario " +
                    "ORDER BY p.fecha DESC";
        } else {
            sql = "SELECT p.*, u.nombre AS usuario FROM PUBLICACIONES p " +
                    "JOIN USUARIOS u ON p.id_usuario = u.id_usuario " +
                    "WHERE u.id_tipo_usuario IN (0, 2) " + // 0: Administrador, 2: Empresa asociada
                    "ORDER BY p.fecha DESC";
        }
        return sql;
    }

    // Obtener publicaciones por usuario
    public static List<Publicacion> obtenerPublicaciones(Connection conexion, Usuario usuario) {
        Connection conn = conexion;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT p.*, u.nombre AS usuario FROM publicaciones p JOIN usuarios u ON p.id_usuario = u.id_usuario WHERE p.id_usuario = ? ORDER BY p.fecha DESC";

        try {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, usuario.getId_usuario());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Publicacion publicacion = new Publicacion(
                                rs.getInt("id_publicacion"),
                                rs.getString("titulo"),
                                rs.getString("descripcion"),
                                rs.getTimestamp("fecha"),
                                rs.getString("tipo"),
                                rs.getInt("id_usuario"),
                                rs.getString("usuario")
                        );
                        publicaciones.add(publicacion);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, Mensajes.getMensaje(Mensajes.ERROR_CARGAR_PUBLICACIONES) + "{0}, {1}", new Object[]{e.getMessage(), e});
            // Reabrir la conexión si se cierra debido a un error
        }

        return publicaciones;
    }

    // METODO para obtener todos los usuarios de la base de datos
    public static List<Usuario> obtenerUsuarios(Connection conexion) {
        Connection conn = conexion;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, t.nombre_tipo FROM USUARIOS u JOIN TIPOS_USUARIOS t ON u.id_tipo_usuario = t.id_tipo_usuario;";
        try {
            assert conn != null;
            PreparedStatement ps = conn.prepareStatement(sql);
            try (ps; ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Consultamos la dirección
                    String direccion = rs.getString("direccion");

                    // Si la direccion no es nula usamos el constructor de la empresa, con direccion
                    Usuario usuario;
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
                    usuarios.add(usuario);
                }
            }
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.ERROR_CARGAR_USUARIOS) + " {0}", e.getMessage());
        }

        return usuarios;
    }

    public static List<Publicacion> obtenerPublicacionesGuardadas(Connection conexion, Usuario usuario) {
        Connection conn = conexion;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        List<Publicacion> publicaciones = new ArrayList<>();
        String sql = "SELECT p.*, u.nombre AS usuario FROM PUBLICACIONES_GUARDADAS pg " +
                "JOIN PUBLICACIONES p ON pg.id_publicacion = p.id_publicacion " +
                "JOIN USUARIOS u ON p.id_usuario = u.id_usuario " +
                "WHERE pg.id_usuario = ? ORDER BY pg.fecha_guardado DESC";

        try {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, usuario.getId_usuario());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Publicacion publicacion = new Publicacion(
                                rs.getInt("id_publicacion"),
                                rs.getString("titulo"),
                                rs.getString("descripcion"),
                                rs.getTimestamp("fecha"),
                                rs.getString("tipo"),
                                rs.getInt("id_usuario"),
                                rs.getString("usuario")
                        );
                        publicaciones.add(publicacion);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, Mensajes.getMensaje(Mensajes.ERROR_CARGAR_PUBLICACIONES) + "{0}, {1}", new Object[]{e.getMessage(), e});
            // Reabrir la conexión si se cierra debido a un error
        }

        return publicaciones;
    }
    public static Usuario obtenerUsuarioPorNombre(Connection conexion, String nombreUsuario) {
        Connection conn = conexion;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        Usuario usuario = null;
        String sql = "SELECT u.*, t.nombre_tipo FROM USUARIOS u JOIN TIPOS_USUARIOS t ON u.id_tipo_usuario = t.id_tipo_usuario WHERE u.nombre = ?";

        try {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nombreUsuario);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Consultamos la dirección
                        String direccion = rs.getString("direccion");

                        // Si la direccion no es nula usamos el constructor de la empresa, con direccion
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
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.ERROR_CARGAR_USUARIO) + " {0}", e.getMessage());
        }

        return usuario;
    }
    public static boolean cambiarPass(Usuario usuario, String nuevaContrasena, Connection conn) {
        String sql = "UPDATE usuarios SET password = ? WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Encriptar la nueva contraseña
            String contrasenaEncriptada = ControladorEncriptacion.encriptar(nuevaContrasena);
            ps.setString(1, contrasenaEncriptada);
            ps.setString(2, usuario.getUsuario());

            // Ejecutar la actualización
            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0; // Devuelve true si se actualizó al menos una fila
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false; // Devuelve false si ocurre un error
        }
    }
    public static List<Mensaje> obtenerMensajes(Connection conexion, Usuario usuario) {
        Connection conn = conexion;

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();

        // Nos aseguramos de que la conexión no sea nula
        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        List<Mensaje> mensajes = new ArrayList<>();
        String sql = "SELECT m.id_mensajes, m.id_usuario_de, m.id_usuario_para, m.asunto, m.contenido, m.fecha, m.leido, " +
                "u_de.nombre AS nombre_emisor, u_para.nombre AS nombre_receptor " +
                "FROM MENSAJES m " +
                "JOIN USUARIOS u_de ON m.id_usuario_de = u_de.id_usuario " +
                "JOIN USUARIOS u_para ON m.id_usuario_para = u_para.id_usuario " +
                "WHERE m.id_usuario_para = ? " +
                "ORDER BY m.fecha DESC";

        try {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, usuario.getId_usuario());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Obtener los nombres de los usuarios
                        String nombreEmisor = rs.getString("nombre_emisor");
                        String nombreReceptor = rs.getString("nombre_receptor");

                        // Obtener los objetos Usuario a partir de los nombres
                        Usuario usuarioEmisor = obtenerUsuarioPorNombre(conn, nombreEmisor);
                        Usuario usuarioReceptor = obtenerUsuarioPorNombre(conn, nombreReceptor);

                        // Crear el objeto Mensaje
                        Mensaje mensaje = new Mensaje(
                                rs.getInt("id_mensajes"),
                                rs.getString("asunto"),
                                rs.getString("contenido"),
                                usuarioEmisor,
                                usuarioReceptor,
                                rs.getTimestamp("fecha"),
                                rs.getBoolean("leido")
                        );
                        mensajes.add(mensaje);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.ERROR_CARGAR_MENSAJES) + " {0}", e.getMessage());
        }

        return mensajes;
    }
    public static void marcarComoLeido(Mensaje mensaje, Connection conn) {
        String sql = "UPDATE MENSAJES SET leido = ? WHERE id_mensajes = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, true); // Marcar como leído
            ps.setInt(2, mensaje.getId_mensaje()); // ID del mensaje

            int filasActualizadas = ps.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("El mensaje ha sido marcado como leído correctamente.");
            } else {
                System.out.println("No se encontró el mensaje para marcar como leído.");
            }
        } catch (SQLException e) {
            System.out.println("Error al marcar el mensaje como leído: " + e.getMessage());
        }
    }
}