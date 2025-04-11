package VIEW.MAIN;

import MODEL.Usuario;
import VIEW.ADD.Add_Empresa;
import VIEW.ADMINISTRAR.Administar_Vista;
import VIEW.INICIO.Inicio_Empresa_Asociada_Vista;
import VIEW.INICIO.Inicio_Vista;
import VIEW.MENSAJES.Mensajes_Lista_Vista;
import VIEW.PERSONAL.Personal_Empresa;
import VIEW.PERSONAL.Personal_Usuario;
import VIEW.RES.Rutas;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.sql.Connection;

public class MAIN_FRAME extends JFrame {

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
        setBackground(new Color(211, 205, 192));

        // Panel principal con pestañas
        JPanel mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Agregar vistas al JTabbedPane
        agregarVistas();

        // Agregar ChangeListener para actualizar las vistas
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            Component selectedComponent = tabbedPane.getComponentAt(selectedIndex);

            if (selectedComponent instanceof Inicio_Empresa_Asociada_Vista) {
                ((Inicio_Empresa_Asociada_Vista) selectedComponent).cargarPublicaciones();
            } else if (selectedComponent instanceof Mensajes_Lista_Vista) {
                ((Mensajes_Lista_Vista) selectedComponent).cargarMensajes();
            } else if (selectedComponent instanceof Inicio_Vista) {
                ((Inicio_Vista) selectedComponent).cargarPublicaciones();
            } else if (selectedComponent instanceof Personal_Usuario) {
                ((Personal_Usuario) selectedComponent).cargarPublicaciones();
            } else if (selectedComponent instanceof Personal_Empresa) {
                ((Personal_Empresa) selectedComponent).cargarPublicaciones();
            } else if (selectedComponent instanceof Administar_Vista) {
                ((Administar_Vista) selectedComponent).actualizarVista();
            }


        });

        // Establecer el contenido inicial
        setContentPane(mainPanel);

        // Hacer visible la ventana
        setVisible(true);
    }

    private void agregarVistas() {

        // Agregar vistas específicas según el tipo de usuario
        if (usuario_actual.getPermisos().equals(Usuario.getTipos(Usuario.USUARIO))) {

            tabbedPane.addTab("Inicio", new Inicio_Vista(usuario_actual, conn));
            tabbedPane.addTab("Personal", new Personal_Usuario(usuario_actual, conn));

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

    }
}