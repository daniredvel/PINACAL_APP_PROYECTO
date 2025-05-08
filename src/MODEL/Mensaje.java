package MODEL;

import java.sql.Timestamp;

/**
 * Clase Mensaje
 * Esta clase representa un mensaje enviado por un usuario a otro usuario.
 * Contiene información sobre el asunto, contenido, remitente, destinatario, fecha de envío y estado de lectura del mensaje.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class Mensaje {
    private String asunto;
    private String contenido;
    private Usuario de_usuario;
    private Usuario para_usuario;
    private String  titulo_publicacion;

    public int getId_mensaje() {
        return id_mensaje;
    }

    public void setId_mensaje(int id_mensaje) {
        this.id_mensaje = id_mensaje;
    }

    private int id_mensaje;
    private boolean leido;
    private Timestamp fecha_envio;

    public Mensaje(String asunto, String contenido, String titulo_publicacion, Usuario de_usuario, Usuario para_usuario, boolean leido) {
        this.asunto = asunto;
        this.contenido = contenido;
        this.titulo_publicacion = titulo_publicacion;
        this.de_usuario = de_usuario;
        this.para_usuario = para_usuario;
        this.fecha_envio = new Timestamp(System.currentTimeMillis());
        this.leido = leido;
    }
    public Mensaje(int id_mensaje, String asunto, String contenido, String titulo_publicacion, Usuario de_usuario, Usuario para_usuario, boolean leido) {
        this.id_mensaje = id_mensaje;
        this.asunto = asunto;
        this.titulo_publicacion = titulo_publicacion;
        this.contenido = contenido;
        this.de_usuario = de_usuario;
        this.para_usuario = para_usuario;
        this.fecha_envio = new Timestamp(System.currentTimeMillis());
        this.leido = leido;
    }
    public Mensaje(int id_mensaje, String asunto, String contenido, String titulo_publicacion, Usuario de_usuario, Usuario para_usuario,Timestamp fecha_envio ,boolean leido) {
        this.id_mensaje = id_mensaje;
        this.asunto = asunto;
        this.contenido = contenido;
        this.de_usuario = de_usuario;
        this.para_usuario = para_usuario;
        this.fecha_envio = fecha_envio;
        this.leido = leido;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Usuario getDe_usuario() {
        return de_usuario;
    }

    public void setDe_usuario(Usuario de_usuario) {
        this.de_usuario = de_usuario;
    }

    public Usuario getPara_usuario() {
        return para_usuario;
    }

    public void setPara_usuario(Usuario para_usuario) {
        this.para_usuario = para_usuario;
    }

    public Timestamp getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(Timestamp fecha_envio) {
        this.fecha_envio = fecha_envio;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public String getTitulo_Publicacion() {
        return titulo_publicacion;
    }

    public void setTitulo_Publicacion(String titulo_publicacion) {
        this.titulo_publicacion = titulo_publicacion;
    }
    @Override
    public String toString() {
        return "Mensaje{" +
                "asunto='" + asunto + '\'' +
                ", contenido='" + contenido + '\'' +
                ", publicacion='" + titulo_publicacion + '\'' +
                ", de_usuario=" + de_usuario.getUsuario() +
                ", para_usuario=" + para_usuario.getUsuario() +
                ", id_mensaje=" + id_mensaje +
                ", leido=" + leido +
                ", fecha_envio=" + fecha_envio +
                '}';
    }
}
