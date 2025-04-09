package CONTROLLER.VALIDATION;

import CONTROLLER.ENCRIPTACION.ControladorEncriptacion;
import DB.UTIL.GestorConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControladorInicioSesion {


    /**
     * @param usuario El nombre de usuario a comprobar
     * @param pass   La contraseña a comprobar
     * @return  1 si el usuario y la contraseña son correctos, -1 si la contraseña es incorrecta, 0 si el usuario no existe
     * @throws Exception Si ocurre un error al acceder a la base de datos
     */
    public static int comprobarPass(String usuario, String pass) throws Exception {

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

    private static ResultSet getResultSet(String usuario) throws SQLException {
        Connection conn = GestorConexion.getConexion();

        //Obtenemos la contraseña del usuario
        PreparedStatement consulta = conn.prepareStatement("SELECT * FROM usuarios WHERE nombre = ?");
        consulta.setString(1, usuario);

        return consulta.executeQuery();
    }
}
