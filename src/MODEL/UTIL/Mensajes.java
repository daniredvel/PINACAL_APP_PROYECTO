package MODEL.UTIL;

public class Mensajes {
    //Constantes de la Base de Datos
    public static final int OK = 0;
    public static final int FALLO_DRIVER = 1;
    public static final int FALLO_CONEXION = 2;
    public static final int FALLO_CERRAR_CONEXION = 3;
    public static final int FALLO_CONSULTA = 4;

    //Constantes de los usuarios
    public static final int USUARIO_ANADIDO = 5;
    public static final int ERROR_ANADIR_USUARIO = 6;
    public static final int USUARIO_ELIMINADO = 7;
    public static final int ERROR_ELIMINAR_USUARIO = 8;
    public static final int USUARIO_ACTUALIZADO = 9;
    public static final int ERROR_ACTUALIZAR_USUARIO = 10;
    public static final int USUARIO_NO_EXISTE = 11;

    //Constantes de las publicaciones
    public static final int PUBLICACION_ANADIDO = 12;
    public static final int ERROR_ANADIR_PUBLICACION = 13;
    public static final int PUBLICACION_ELIMINADA = 14;
    public static final int ERROR_ELIMINAR_PUBLICACION = 15;
    public static final int PUBLICACION_GUARDADA = 16;
    public static final int PUBLICACION_RETIRADA = 17;
    public static final int ERROR_GUARDAR_PUBLICACION = 18;
    public static final int ERROR_RETIRAR_PUBLICACION = 19;

    //Constantes de otros mensajes
    public static final int ERROR_ELIMINAR = 20;
    public static final int ERROR_CARGAR_USUARIOS = 21;
    public static final int ERROR_CARGAR_PUBLICACIONES = 22;
    public static final int ERROR_AL_CIFRAR = 23;
    public static final int ERROR_CARGAR_USUARIO = 24;
    public static final int ERROR_CARGAR_MENSAJES = 25;




    /**
     * Array de mensajes de error, cada mensaje se relaciona
     * por posición con el código de error correspondiente.
     */
    private static final String[] MENSAJE = {
            "Correcto",
            "Error al conectar con la base de datos. Fallo en el conector",
            "Error al conectar con la base de datos",
            "No se pudo cerrar la conexión",
            "Error al recoger los datos",
            "Usuario añadido correctamente",
            "Error al añadir usuario",
            "Usuario eliminado correctamente",
            "Error al eliminar usuario",
            "Usuario actualizado correctamente",
            "Error al actualizar usuario",
            "Usuario no encontrado",
            "Publicación añadida correctamente",
            "Error al añadir la publicación",
            "Publicación eliminada correctamente",
            "Error al eliminar la publicación",
            "Publicación guardada correctamente",
            "Publicación retirada de guardados",
            "No se pudo guardar la publicación",
            "No se pudo eliminar de guardados",
            "Error al eliminar",
            "Error al cargar los usuarios: ",
            "Error al cargar las publicaciones: ",
            "Error al cifrar la contraseña",
            "Error al cargar el usuario: ",
            "Error al cargar los mensajes del usuario"

    };

    /**
     * METODO que devuelve el mensaje de error correspondiente al código
     * @param codigo Código de error
     * @return Mensaje de error
     */
    public static String getMensaje(int codigo){
        return MENSAJE[codigo];
    }
}
