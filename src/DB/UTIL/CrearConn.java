package DB.UTIL;

import java.sql.Connection;

public class CrearConn {
    private static final String DB_NAME = "Pinacal";
    private static final String DB_USER = "Pinacal";
    private static final String DB_PASSWORD = "pinacal";
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
