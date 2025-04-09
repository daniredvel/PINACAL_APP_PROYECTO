package VIEW.INICIO;

import CONTROLLER.ControladorDatos;
import MODEL.Publicacion;
import MODEL.Usuario;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;

public class Inicio_Empresa_Asociada_Vista extends Inicio_Vista {
    public Inicio_Empresa_Asociada_Vista(Usuario usuario_actual, Connection conexion) {
        super(usuario_actual, conexion);
        LOGGER.log(Level.INFO, "Iniciando vista de inicio de empresa asociada");


    }

    @Override
    protected void cargarPublicaciones() {
        LOGGER.log(Level.INFO, "Cargando publicaciones");
        listModel.clear(); // Limpia el modelo de la lista antes de cargar las publicaciones


        List<Publicacion> publicaciones = ControladorDatos.obtenerPublicaciones(conn, true);
        for (Publicacion publicacion : publicaciones) {
            listModel.addElement(publicacion);
        }
        LOGGER.log(Level.INFO, "Publications loaded: {0}", listModel.size());
    }}
