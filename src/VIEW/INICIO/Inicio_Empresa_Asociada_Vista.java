package VIEW.INICIO;

import CONTROLLER.ControladorDatos;
import MODEL.Publicacion;
import MODEL.Usuario;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;

public class Inicio_Empresa_Asociada_Vista extends Inicio_Vista {
    private boolean cargando = false; // Evitar múltiples cargas simultáneas
    private boolean hayMasPublicaciones = true; // Controlar si hay más publicaciones por cargar

    private static int OFFSSET = 0;
    public Inicio_Empresa_Asociada_Vista(Usuario usuario_actual, Connection conexion) {
        super(usuario_actual, conexion);
        LOGGER.log(Level.INFO, "Iniciando vista de inicio de empresa asociada");


    }

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

}
