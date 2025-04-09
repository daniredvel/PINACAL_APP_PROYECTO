package DB.UTIL;

import MODEL.UTIL.Mensajes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que se encarga de gestionar la conexión con la base de datos.
 */
public class GestorConexion {
    /**
     * Objeto estático de conexión con la base de datos.
     */
    private static Connection conexion = null;
    private GestorConexion() {}


    /**
     * METODO que crea una conexión con la base de datos.
     * @param bd Nombre de la base de datos.
     * @param usr Usuario de la base de datos.
     * @param pass Contraseña del usuario de la base de datos.
     * @return Código de error.
     */

    public static int crearConexion(String bd, String usr, String pass) {
        //URL para conexiones remotas
        //String url = "jdbc:mysql://85.251.134.6:3306/";
        String url = "jdbc:mysql://localhost:3306/";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(
                    url + bd, usr, pass
            );

            System.out.println("Conexión a la base de datos establecida correctamente."); // Debug
            return Mensajes.OK;
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Error: Driver no encontrado."); // Debug
            return Mensajes.FALLO_DRIVER;
        } catch (SQLException sqle) {
            System.err.println("Error: No se pudo establecer la conexión a la base de datos."); // Debug
            return Mensajes.FALLO_CONEXION;
        }
    }

    /**
     * METODO que devuelve la conexión con la base de datos.
     * @return Conexión con la base de datos.
     */
    public static Connection getConexion() {
        return conexion;
    }

    /**
     * METODO que cierra la conexión con la base de datos.
     * @return Código de error.
     */
    public static int cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión a la base de datos cerrada correctamente."); // Debug
                return Mensajes.OK;
            } else {
                return Mensajes.FALLO_CERRAR_CONEXION;
            }
        } catch (SQLException sqle) {
            System.err.println("Error: No se pudo cerrar la conexión a la base de datos."); // Debug
            return Mensajes.FALLO_CERRAR_CONEXION;
        }
    }
}