package DB.UTIL;

import java.sql.Connection;

/**
 * Clase encargada de crear conexiones la base de datos.
 *
 * <p>Esta clase se encarga de obtener las listas de publicaciones y de usuarios desde la base de datos en función de lo solicitado por las vistas.</p>
 *
 * <strong>Dependencias:</strong>
 * <ul>
 *     <li>La clase depende de los paquetes {@link DB.UTIL.GestorConexion} para gestionar la conexión
 *     <li>Utiliza el paquete estándar de <code>java.sql</code> para conectarse y operar con la base de datos.</li>
 * </ul>
 *
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class CrearConn {
    private static final String DB_NAME = "";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";
    public static boolean crearConexion() {
        int estadoConexion = GestorConexion.crearConexion(DB_NAME, DB_USER, DB_PASSWORD);
        return estadoConexion == 0;
    }
    public static Connection conn() {
        int estadoConexion = GestorConexion.crearConexion(DB_NAME, DB_USER, DB_PASSWORD);
        if(estadoConexion == 0) return GestorConexion.getConexion();
        else return null;
    }
}
