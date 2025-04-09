package MODEL.ELIMINADO;

import MODEL.Publicacion;

import java.sql.Timestamp;

public class Publicacion_eliminada extends Eliminado {
    final Publicacion publicacion;
    //Indicamos la publicación que se ha eliminado y el mensaje que justifica su eliminacion
    public Publicacion_eliminada(String mensaje, Publicacion publicacion) {
        //La fecha de la eliminación, el momento en el que se crea el objeto
        this.setFecha(new Timestamp(System.currentTimeMillis()));
        this.setTipo(Eliminado.getTipos()[1]);
        this.setMensaje(mensaje);
        this.publicacion=publicacion;
    }
}
