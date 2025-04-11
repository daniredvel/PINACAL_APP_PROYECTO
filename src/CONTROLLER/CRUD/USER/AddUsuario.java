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

/**
 * Clase encargada de añadir usuarios a la base de datos.
 *
 * <p>Esta clase se conecta a la base de datos y realiza operaciones de inserción
 * (INSERT) en la tabla `USUARIOS`.</p>
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

public class AddUsuario {
    private static final Logger LOGGER = Logger.getLogger(AddUsuario.class.getName());

    public static String addEmpresa(Usuario empresa) {
        LOGGER.log(Level.INFO, "Añadiendo empresa: {0}", empresa.getUsuario());

        empresa.setTipo(Usuario.getTipos(Usuario.EMPRESA_NO_ASOCIADA));
        empresa.setPermisos("1");

        return responder(insertUsuario(empresa));
    }

    public static String addUsuario(Usuario usuario) {
        LOGGER.log(Level.INFO, "Añadiendo usuario: {0}", usuario.getUsuario());

        usuario.setTipo(Usuario.getTipos(Usuario.USUARIO));
        usuario.setPermisos("1");

        return responder(insertUsuario(usuario));
    }

    private static String responder(boolean usuario) {
        if (usuario) {
            LOGGER.log(Level.INFO, "Usuario añadido correctamente");
            return Mensajes.getMensaje(Mensajes.USUARIO_ANADIDO);
        } else {
            LOGGER.log(Level.SEVERE, "Error al añadir usuario");
            return Mensajes.getMensaje(Mensajes.ERROR_ANADIR_USUARIO);
        }
    }

    private static boolean insertUsuario(Usuario usuario) {
        LOGGER.log(Level.INFO, "Insertando usuario en la base de datos: {0}", usuario.getUsuario());

        Connection conn = GestorConexion.getConexion();

        if (conn == null) {
            conn = conn();
        }

        if (conn == null) {
            LOGGER.log(Level.SEVERE, Mensajes.getMensaje(Mensajes.FALLO_CONEXION));
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
            return false;
        }

        String sqlDireccion = "INSERT INTO USUARIOS (nombre, password, email, direccion, telefono, id_tipo_usuario) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlSinDireccion = "INSERT INTO USUARIOS (nombre, password, email, telefono, id_tipo_usuario) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps;
            if (usuario.getDireccion() != null) {
                ps = conn.prepareStatement(sqlDireccion);
                ps.setString(1, usuario.getUsuario());
                ps.setString(2, usuario.getPassword());
                ps.setString(3, usuario.getEmail());
                ps.setString(4, usuario.getDireccion());
                ps.setString(5, Usuario.formatoTelefonoBD(usuario.getTelefono()));
                ps.setString(6, usuario.getPermisos());
            } else {
                ps = conn.prepareStatement(sqlSinDireccion);
                ps.setString(1, usuario.getUsuario());
                ps.setString(2, usuario.getPassword());
                ps.setString(3, usuario.getEmail());
                ps.setString(4, Usuario.formatoTelefonoBD(usuario.getTelefono()));
                ps.setString(5, usuario.getPermisos());
            }
            ps.executeUpdate();
            LOGGER.log(Level.INFO, "Usuario insertado correctamente en la base de datos");
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar usuario en la base de datos", e);
            return false;
        }
    }
}