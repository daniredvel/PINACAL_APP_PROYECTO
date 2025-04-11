package MAIN;

import DB.UTIL.GestorConexion;
import MODEL.UTIL.Mensajes;
import VIEW.ERROR.Error_INICIAR_BD;
import VIEW.INICIO_SESION.InicioSesion_Vista;

import javax.swing.*;
import java.sql.Connection;

import static DB.UTIL.CrearConn.crearConexion;

/**
 * Clase principal de la aplicación.
 * Esta clase da inicio a la aplicación, establece la conexión a la base de datos y lanza la vista de inicio de sesión.
 * Esta clase gestiona el cierre de la base de datos al cerrar la aplicación.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */


public class Main {

        private static Connection conn;

        public static void main(String[] args) {

                //Creamos la conexion a la base de datos
                if (crearConexion()) {
                        //Obtenemos la conexion
                        conn = GestorConexion.getConexion();
                        //Llamamos a la vista de inicio de sesion para iniciar el programa
                        SwingUtilities.invokeLater(() -> new InicioSesion_Vista(conn).setVisible(true));
                } else{
                        //En caso de error al crear la conexion, mostramos un mensaje de error
                        SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
                }

                // Agregar un Shutdown Hook para cerrar la conexión al cerrar la aplicación
                //Crea un hilo que se ejecuta al cerrar la aplicación para cerrar la conexión a la base de datos
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        System.out.println("Cerrando la conexión a la base de datos...");
                        System.out.println(Mensajes.getMensaje(GestorConexion.cerrarConexion()));
                }));    }

}