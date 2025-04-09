package MODEL.ELIMINADO;

import MODEL.Usuario;

import java.sql.Timestamp;

public class Usuario_eliminado extends Eliminado {
    final Usuario usuario;
    public Usuario_eliminado(String mensaje, Usuario usuario) {
        //Indicamos el Usuario que se ha eliminado y el mensaje que justifica su eliminacion
        this.setFecha(new Timestamp(System.currentTimeMillis()));
        //La fecha de la eliminación, el momento en el que se crea el objeto
        this.setTipo(Eliminado.getTipos()[1]);
        this.setMensaje(mensaje);
        this.usuario=usuario;
    }
}
