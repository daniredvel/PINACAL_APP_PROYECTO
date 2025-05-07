package VIEW.MAIN;

import MODEL.Usuario;
import VIEW.ADD.Add_Empresa;
import VIEW.ADMINISTRAR.Administar_Vista;
import VIEW.INICIO.Inicio_Empresa_Asociada_Vista;
import VIEW.INICIO.Inicio_Vista;
import VIEW.MENSAJES.Mensajes_Lista_Vista;
import VIEW.PERSONAL.Personal_Empresa;
import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

/**
 * Clase que representa la ventana principal de la aplicación.
 * Esta clase se encarga de mostrar la vista de inicio de la aplicación y de gestionar las diferentes vistas que se muestran.
 * <p>
 * La clase cuenta con varios componentes de interfaz de usuario, como un panel de navegación y un panel de contenido.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class MAIN_FRAME extends JFrame {

    boolean cargando = false; // Evitar múltiples cargas simultáneas
    boolean hayMasPublicaciones = true; // Controlar si hay más publicaciones por cargar
    int OFFSSET = 0; //Límite para cargar publicaciones


    private final JTabbedPane tabbedPane;
    private static Usuario usuario_actual;
    private static Connection conn;

    public MAIN_FRAME(Usuario usuario_actual, Connection conexion) {
        MAIN_FRAME.usuario_actual = usuario_actual;
        MAIN_FRAME.conn = conexion;

        setTitle("RED PINACAL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        // Icono
        setIconImage(Rutas.getImage(Rutas.ICONO));

        // Fondo
        setBackground(Rutas.getColor(Rutas.GRIS));

        // Panel principal con pestañas
        JPanel mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Agregar vistas al JTabbedPane
        agregarVistas();

        // Agregar ChangeListener para actualizar las vistas
        tabbedPane.addChangeListener(e -> actualizarVista());

        // Establecer el contenido inicial
        setContentPane(mainPanel);

        // Hacer visible la ventana
        setVisible(true);
    }

    private void agregarVistas() {

        // Agregar vistas específicas según el tipo de usuario
        if (usuario_actual.getPermisos().equals(Usuario.getTipos(Usuario.USUARIO))) {

            tabbedPane.addTab("Inicio", new Inicio_Vista(usuario_actual, conn));
            tabbedPane.addTab("Personal", new Personal_Empresa(usuario_actual, conn));

        } else if (usuario_actual.getPermisos().equals(Usuario.getTipos(Usuario.EMPRESA_ASOCIADA))) {

            tabbedPane.addTab("Inicio", new Inicio_Empresa_Asociada_Vista(usuario_actual, conn));
            tabbedPane.addTab("Personal", new Personal_Empresa(usuario_actual, conn));
            tabbedPane.addTab("Mis Publicaciones", new Add_Empresa(usuario_actual, conn));

        } else if (usuario_actual.getPermisos().equals(Usuario.getTipos(Usuario.EMPRESA_NO_ASOCIADA))) {

            tabbedPane.addTab("Inicio", new Inicio_Vista(usuario_actual, conn));
            tabbedPane.addTab("Personal", new Personal_Empresa(usuario_actual, conn));
            tabbedPane.addTab("Mis Publicaciones", new Add_Empresa(usuario_actual, conn));

        } else if (usuario_actual.getPermisos().equals(Usuario.getTipos(Usuario.ADMINISTRADOR))) {

            tabbedPane.addTab("Inicio", new Inicio_Empresa_Asociada_Vista(usuario_actual, conn));
            tabbedPane.addTab("Personal", new Personal_Empresa(usuario_actual, conn));
            tabbedPane.addTab("Mis Publicaciones", new Add_Empresa(usuario_actual, conn));
            tabbedPane.addTab("Administrar", new Administar_Vista(usuario_actual, conn));
        }

        // Agregar vistas comunes
        tabbedPane.addTab("Mensajes", new Mensajes_Lista_Vista(usuario_actual, conn));

        // Cambiar la fuente de las pestañas
        Font font = new Font("Arial", Font.ITALIC, 18);
        tabbedPane.setFont(font);

        // Agregar ChangeListener para actualizar las vistas
        tabbedPane.addChangeListener(e -> actualizarVista());
    }

    private void actualizarVista() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        Component selectedComponent = tabbedPane.getComponentAt(selectedIndex);


        if (selectedComponent instanceof Inicio_Empresa_Asociada_Vista) {

            //Actualizamos el estado de cargando para que vuelva a cargar las publicaciones desde 0
            ((Inicio_Empresa_Asociada_Vista) selectedComponent).setCargando(cargando);
            //Para evitar dublicados actualizamos el valor de cargando
            cargando = ((Inicio_Empresa_Asociada_Vista) selectedComponent).getCargando();

            //Actualizamos el estado de HayMasPublicaciones para que vuelva a cargar las publicaciones desde 0
            ((Inicio_Empresa_Asociada_Vista) selectedComponent).setHayMasPublicaciones(hayMasPublicaciones);
            //Para evitar dublicados actualizamos el valor de HayMasPublicaciones
            hayMasPublicaciones = ((Inicio_Empresa_Asociada_Vista) selectedComponent).getHayMasPublicaciones();

            //Actualizamos el estado de OFFSSET para que vuelva a cargar las publicaciones desde 0
            Inicio_Empresa_Asociada_Vista.setOFFSSET(OFFSSET);
            //Para evitar dublicados actualizamos el valor del OFFSSET
            OFFSSET = Inicio_Empresa_Asociada_Vista.getOFFSSET();

            //Limpiamos la vista para que no haya publicaciones duplicadas
            Inicio_Empresa_Asociada_Vista.limpiar();

            //Cargamos las publicaciones
            ((Inicio_Empresa_Asociada_Vista) selectedComponent).cargarPublicaciones();

        } else if (selectedComponent instanceof Mensajes_Lista_Vista) {
            ((Mensajes_Lista_Vista) selectedComponent).cargarMensajes();
        } else if (selectedComponent instanceof Inicio_Vista) {

            ((Inicio_Vista) selectedComponent).setCargando(cargando);
            cargando = ((Inicio_Vista) selectedComponent).getCargando();

            ((Inicio_Vista) selectedComponent).setHayMasPublicaciones(hayMasPublicaciones);
            hayMasPublicaciones = ((Inicio_Vista) selectedComponent).getHayMasPublicaciones();

            Inicio_Vista.setOFFSSET(OFFSSET);
            OFFSSET = Inicio_Vista.getOFFSSET();

            Inicio_Vista.limpiar();
            ((Inicio_Vista) selectedComponent).cargarPublicaciones();

        } else if (selectedComponent instanceof Personal_Empresa) {

            ((Personal_Empresa) selectedComponent).setCargando(cargando);
            cargando = ((Personal_Empresa) selectedComponent).getCargando();

            ((Personal_Empresa) selectedComponent).setHayMasPublicaciones(hayMasPublicaciones);
            hayMasPublicaciones = ((Personal_Empresa) selectedComponent).getHayMasPublicaciones();

            Personal_Empresa.setOFFSSET(OFFSSET);
            OFFSSET = Personal_Empresa.getOFFSSET();

            Personal_Empresa.limpiar();
            ((Personal_Empresa) selectedComponent).cargarPublicaciones();

        }else if (selectedComponent instanceof Add_Empresa) {

            ((Add_Empresa) selectedComponent).setCargando(cargando);
            cargando = ((Add_Empresa) selectedComponent).getCargando();

            ((Add_Empresa) selectedComponent).setHayMasPublicaciones(hayMasPublicaciones);
            hayMasPublicaciones = ((Add_Empresa) selectedComponent).getHayMasPublicaciones();

            Add_Empresa.setOFFSSET(OFFSSET);
            OFFSSET = Add_Empresa.getOFFSSET();

            Add_Empresa.limpiar();
            ((Add_Empresa) selectedComponent).cargarPublicaciones(usuario_actual);

        } else if (selectedComponent instanceof Administar_Vista) {
            ((Administar_Vista) selectedComponent).actualizarVista();
        }
    }
}