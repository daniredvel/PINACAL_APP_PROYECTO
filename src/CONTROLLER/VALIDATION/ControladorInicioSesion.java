package CONTROLLER.VALIDATION;

import CONTROLLER.ENCRIPTACION.ControladorEncriptacion;
import DB.UTIL.GestorConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase encargada de validar el inicio de sesión de un usuario en la aplicación.
 *
 * <p>Esta clase se encarga de comparar la clave proporcionada por el usuario con la almacenada en la base de datos.</p>
 *
 * <p><strong>Dependencias:</strong></p>
 * <ul>
 *     <li>La clase depende de los paquetes {@link CONTROLLER.ENCRIPTACION.ControladorEncriptacion} para comparar la clave
 *         proporcionada por el usuario con la almacenada en la base de datos.</li>
 *     <li>Utiliza el paquete estándar de <code>java.sql</code> para conectarse y operar con la base de datos.</li>
 * </ul>
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */


public class ControladorInicioSesion {


    /**
     * @param usuario El nombre de usuario a comprobar
     * @param pass    La contraseña a comprobar
     * @return 1 si el usuario y la contraseña son correctos, -1 si la contraseña es incorrecta, 0 si el usuario no existe
     * @throws Exception Si ocurre un error al acceder a la base de datos
     */
    public static int comprobarPass(String usuario, String pass) throws Exception {
        System.out.println("Comprobando usuario: " + usuario);

        ResultSet resultSet = getResultSet(usuario);

        //Comprobamos si el usuario existe
        if (resultSet.next()) {
            String passDB = resultSet.getString("password");
            //Comprobamos si la contraseña es correcta
            if (ControladorEncriptacion.comprobar(pass, passDB)) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return 0;
        }

    }

    public static int comprobarCorreo(String correo, String pass) throws Exception {
        System.out.println("Comprobando correo: " + correo);

        ResultSet resultSet = getResultSetCorreo(correo);

        //Comprobamos si el usuario existe
        if (resultSet.next()) {
            String passDB = resultSet.getString("password");
            //Comprobamos si la contraseña es correcta
            if (ControladorEncriptacion.comprobar(pass, passDB)) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return 0;
        }

    }

    private static ResultSet getResultSet(String usuario) throws SQLException {
        Connection conn = GestorConexion.getConexion();

        //Obtenemos la contraseña del usuario
        PreparedStatement consulta = conn.prepareStatement("SELECT * FROM usuarios WHERE nombre = ?");
        consulta.setString(1, usuario);

        return consulta.executeQuery();
    }

    private static ResultSet getResultSetCorreo(String correo) throws SQLException {
        Connection conn = GestorConexion.getConexion();

        //Obtenemos la contraseña del usuario
        PreparedStatement consulta = conn.prepareStatement("SELECT * FROM usuarios WHERE email = ?");
        consulta.setString(1, correo);

        return consulta.executeQuery();

    }
}
