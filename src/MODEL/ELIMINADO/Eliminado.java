package MODEL.ELIMINADO;

import java.sql.Timestamp;


//Modelo base para los usuarios y publicaciones eliminadas
public class Eliminado {
    Timestamp fecha;
    String tipo;
    static final String  []tipos={"USUARIO","PUBLICACION"};
    String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public static String[] getTipos() {
        return tipos;
    }
}
