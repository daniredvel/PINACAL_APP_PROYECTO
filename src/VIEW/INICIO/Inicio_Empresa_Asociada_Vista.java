package VIEW.INICIO;

import CONTROLLER.ControladorDatos;
import MODEL.Publicacion;
import MODEL.Usuario;
import VIEW.PERSONAL.Personal_Empresa;

import javax.swing.*;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;

/**
 * Clase que representa la vista de la ventana de inicio de empresa asociada y administrador en la aplicación.
 * Esta clase extiende la clase {@link Inicio_Vista} y se encarga de mostrar todas las publicaciones de la app.
 * <p>
 * La clase cuenta con dos variables de instancia para controlar el estado de carga de publicaciones: {@link #cargando} y {@link #hayMasPublicaciones}.
 * <p>
 * La clase también cuenta con un metodo {@link #cargarPublicaciones()} que se encarga de cargar las publicaciones de la empresa asociada.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class Inicio_Empresa_Asociada_Vista extends Inicio_Vista {
    private boolean cargando = false; // Evitar múltiples cargas simultáneas
    private boolean hayMasPublicaciones = true; // Controlar si hay más publicaciones por cargar

    private static int OFFSSET = 0;
    public Inicio_Empresa_Asociada_Vista(Usuario usuario_actual, Connection conexion) {
        super(usuario_actual, conexion);
        LOGGER.log(Level.INFO, "Iniciando vista de inicio de empresa asociada");


    }

    /**
     * Metodo que se encarga de cargar las publicaciones de la empresa asociada.
     * <p>
     * Este metodo verifica si se están cargando publicaciones actualmente o si no hay más publicaciones por cargar.
     * Si se cumplen estas condiciones, no se carga nada.
     * <p>
     * De lo contrario, se procede a cargar las publicaciones.
     *
     * @see #cargando
     * @see #hayMasPublicaciones
     */

    @Override
    public void cargarPublicaciones() {
        System.out.println("Intentando cargar publicaciones...");
        System.out.println("Estado antes de cargar: cargando=" + cargando + ", hayMasPublicaciones=" + hayMasPublicaciones);

        if (cargando || !hayMasPublicaciones) {
            System.out.println("Carga bloqueada: cargando=" + cargando + ", hayMasPublicaciones=" + hayMasPublicaciones);
            return; // Evitar cargas innecesarias
        }

        cargando = true; // Marcar como cargando
        System.out.println("Cargando publicaciones con offset=" + OFFSSET + ", limit=" + ControladorDatos.LIMIT);

        List<Publicacion> publicaciones = ControladorDatos.obtenerPublicaciones(conn, true, OFFSSET);

        if (publicaciones.isEmpty()) {
            System.out.println("No se encontraron más publicaciones.");
            hayMasPublicaciones = false; // No hay más publicaciones por cargar
        } else {
            for (Publicacion publicacion : publicaciones) {
                listModel.addElement(publicacion);
            }
            System.out.println("OFFSET=" + OFFSSET);

            if(!publicaciones.isEmpty()) OFFSSET += publicaciones.size(); //Solo se recarga el offset si hay publicaciones


            System.out.println("OFFSET=" + OFFSSET);
            System.out.println("Publicaciones cargadas: " + publicaciones.size() + ", nuevo offset=" + OFFSSET);
        }

        cargando = false; // Finalizar la carga
        repaint();
        System.out.println("Carga finalizada.");
    }

    public boolean getCargando() {
        return cargando;
    }

    public void setCargando(boolean cargando) {
        this.cargando = cargando;
    }

    public boolean getHayMasPublicaciones() {
        return hayMasPublicaciones;
    }

    public void setHayMasPublicaciones(boolean hayMasPublicaciones) {
        this.hayMasPublicaciones = hayMasPublicaciones;
    }

    public static int getOFFSSET() {
        return OFFSSET;
    }


    public static void setOFFSSET(int OFFSSET) {
        Inicio_Empresa_Asociada_Vista.OFFSSET = OFFSSET;
    }
    public void setListModel(DefaultListModel<Publicacion> listModel) {
        Inicio_Empresa_Asociada_Vista.listModel = listModel;
    }


    public DefaultListModel<Publicacion> getListModel() {
        return listModel;
    }

    public static void limpiar() {
        listModel.clear();
    }
}
