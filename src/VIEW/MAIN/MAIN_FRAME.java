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
import java.awt.*;
import java.sql.Connection;
import java.util.logging.Logger;

public class MAIN_FRAME extends JFrame {
    public static final Logger LOGGER = Logger.getLogger(MAIN_FRAME.class.getName());

    private JTabbedPane tabbedPane;
    private JPanel mainPanel;
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

        //Fondo
        setBackground(new Color(211, 205, 192));

        // Panel principal con pestañas
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Agregar vistas al JTabbedPane
        agregarVistas();

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