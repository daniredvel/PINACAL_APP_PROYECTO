package MODEL;

public class Usuario {
    //Tipos de usario
    //El usuario administrador se encarga de la validación y tramitación de las publicaciones
    //Los usuarios de tipo 'Asociado' son los usuarios de las empresas asociadas a PINACAL
    //Los usuarios de tipo 'Empresa_no_asociada' son las empresas no asociadas
    //Los usuarios de tipo 'Usuario' son los personas particulares
    private static final String [] tipos = {"ADMINISTRADOR", "USUARIO", "EMPRESA_ASOCIADA", "EMPRESA_NO_ASOCIADA","TEMPORAL"};

    public static final int ADMINISTRADOR = 0;
    public static final int USUARIO = 1;
    public static final int EMPRESA_ASOCIADA = 2;
    public static final int EMPRESA_NO_ASOCIADA = 3;
    public static final int TEMPORAL = 4;


    private int id_usuario;
    private String usuario;
    private String password;
    private String email;
    private String direccion;
    private String telefono;
    private String tipo;
    private String permisos;
    private int indice_tipo_usuario;
    //"1" Permiso de usuario; Publicar y eliminar publicaciones propias
    //"2" Permiso de administrador; Eliminar usuarios y publicaciones de cualquier usuario

    //CONSTRUCTORES

    //Constructor por defecto
    public Usuario(){}

    //Constructor para empresas con id
    //Las empresas tienen direccion
    public Usuario(
            int     id_usuario,
            String  usuario,
            String  password,
            String  email,
            String  direccion,
            String  telefono,
            int     indice_tipo_usuario,
            String  permisos
    ){
        this.id_usuario =   id_usuario;
        this.usuario    =   usuario;
        this.password   =   password;
        this.email      =   email;
        this.direccion  =   direccion;
        this.indice_tipo_usuario = indice_tipo_usuario;
        this.telefono   =   formatoTelefonoBD(telefono);
        this.tipo       =   indicarTipo(indice_tipo_usuario);
        this.permisos   =   permisos.toUpperCase();
    }
    //Constructor para usuarios con id
    //Los usuarios no tienen direccion
    public Usuario(
            int     id_usuario,
            String  usuario,
            String  password,
            String  email,
            String  telefono,
            int     indice_tipo_usuario,
            String  permisos

    ){
        this.id_usuario =   id_usuario;
        this.usuario    =   usuario;
        this.password   =   password;
        this.email      =   email;
        this.indice_tipo_usuario = indice_tipo_usuario;
        this.telefono   =   formatoTelefonoBD(telefono);
        this.tipo       =   indicarTipo(indice_tipo_usuario);
        this.permisos   =   permisos.toUpperCase();

    }

    //Constructor para empresas sin id
    //Las empresas tienen direccion
    public Usuario(
            String  usuario,
            String  password,
            String  email,
            String  direccion,
            String  telefono,
            int     indice_tipo_usuario,
            String  permisos

    ){
        this.usuario    =   usuario;
        this.password   =   password;
        this.email      =   email;
        this.direccion  =   direccion;
        this.telefono   =   formatoTelefonoBD(telefono);
        this.indice_tipo_usuario = indice_tipo_usuario;
        this.tipo       =   indicarTipo(indice_tipo_usuario);
        this.permisos   =   permisos.toUpperCase();

    }
    //Constructor para usuarios sin id
    //Los usuarios no tienen direccion
    public Usuario(
            String  usuario,
            String  password,
            String  email,
            String  telefono,
            int     indice_tipo_usuario,
            String  permisos

    ){
        this.usuario    =   usuario;
        this.password   =   password;
        this.email      =   email;
        this.telefono   =   formatoTelefonoBD(telefono);
        this.indice_tipo_usuario = indice_tipo_usuario;
        this.tipo       =   indicarTipo(indice_tipo_usuario);
        this.permisos   =   permisos.toUpperCase();

    }

    //GETTER

    public int getId_usuario() {
        return id_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    //Codificación
    public String getPassword() {
        return password;
    }

    public String getEmail(){
        return email;
    }

    public String getDireccion(){
        return direccion;
    }

    public String getTelefono(){
        return telefono;
    }

    public String getTipo(){
        return tipo;
    }

    public String getPermisos() {
        return permisos;
    }

    public int getIndice_tipo_usuario() {
        return indice_tipo_usuario;
    }



    //SETTER

    public void setId_usuario(int id_usuario){
        this.id_usuario = id_usuario;
    }

    public void setUsuario(String usuario){
        this.usuario = usuario;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public void setTelefono(String telefono){
        this.telefono = formatoTelefono(telefono);
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public void setPermisos(String permisos) {
        this.permisos = permisos;
    }

    public void setIndice_tipo_usuario(int indice_tipo_usuario) {
        this.indice_tipo_usuario = indice_tipo_usuario;
    }
    //METODO QUE INDICA EL TIPO DE USUARIO A PARTIR DE UN indice_tipo_usuario

    private String indicarTipo(int indice_tipo_usuario){
        return tipos[indice_tipo_usuario];
    }

    public static String formatoDireccion(String calle, String numero, String localidad, String municipio, String provincia, String codigoPostal, String pais){
        return "C/"+ formato(calle) + ", Nº " + numero + ", " + formato(localidad) + ", " + formato(municipio) + ", " + formato(provincia) + ", C.P.: " + codigoPostal + ", " + formato(pais);

    }
    private static String formato(String palabra){
        return palabra.substring(0,1).toUpperCase()+palabra.substring(1).toLowerCase();
    }
    public static String getTipos(int codigo){
        return tipos[codigo];
    }
    public static String formatoTelefono(String telefono) {
        // Eliminar todos los caracteres no numéricos
        String soloNumeros = telefono.replaceAll("\\D+", "");
        // Formatear el número en el formato '000 000 000'
        return soloNumeros.replaceAll("(\\d{3})(\\d{3})(\\d{3})", "$1 $2 $3");
    }

    public static String formatoTelefonoBD(String telefono) {
        // Eliminar todos los caracteres no numéricos
        return telefono.replaceAll("\\D+", "");
    }
    
}
