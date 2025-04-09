package VIEW.PUBLICACIONES;

import CONTROLLER.CRUD.PUBLICACION.GuardarPublicacion;
import CONTROLLER.ControladorDatos;
import DB.UTIL.GestorConexion;
import MODEL.Publicacion;
import MODEL.UTIL.Mensajes;
import MODEL.Usuario;
import VIEW.PERFILES.Perfil_Usuario_Vista;
import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Publicacion_Detalle_Vista extends JDialog {
    private static boolean isOriginalIcon = true;
    private final JButton saveButton;
    private static final Logger LOGGER = Logger.getLogger(Publicacion_Detalle_Vista.class.getName());
    private static Window owner;
    private static Usuario usuario_actual;
    private static Connection conn;

    public Publicacion_Detalle_Vista(Window owner, Publicacion publicacion, Usuario usuario_actual, Connection conexion) {
        super(owner, "Detalle de Publicación", ModalityType.APPLICATION_MODAL);
        Publicacion_Detalle_Vista.owner = owner;
        Publicacion_Detalle_Vista.usuario_actual = usuario_actual;
        Publicacion_Detalle_Vista.conn = conexion;

        // Configuración inicial de la ventana
        setSize(800, 300);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Establecer el icono y el título de la ventana
        setIconImage(Rutas.getImage(Rutas.ICONO));
        setTitle(publicacion.getTitulo());

        // Panel principal
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(211, 205, 192));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel izquierdo con información de la publicación
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(211, 205, 192));

        // Tipo de publicación
        JLabel textViewTipo = new JLabel(publicacion.getTipo());
        textViewTipo.setFont(new Font("Arial", Font.PLAIN, 30));
        textViewTipo.setForeground(new Color(174, 101, 7));
        textViewTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(textViewTipo);

        // Título de la publicación
        JLabel textViewNombre = new JLabel(publicacion.getTitulo());
        textViewNombre.setFont(new Font("Arial", Font.PLAIN, 30));
        textViewNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(textViewNombre);

        // Autor de la publicación
        JLabel textViewUsuario = new JLabel("Autor: " + publicacion.getUsuario());
        textViewUsuario.setFont(new Font("Arial", Font.PLAIN, 20));
        textViewUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(textViewUsuario);

        // Hacer clic en el autor para abrir su perfil
        textViewUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        textViewUsuario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Connection conn = GestorConexion.getConexion();
                Usuario autor = ControladorDatos.obtenerUsuarioPorNombre(conn, publicacion.getUsuario());
                if (autor != null) {
                    abrirPerfil(autor);
                } else {
                    LOGGER.log(Level.SEVERE, "No se pudo encontrar el autor: " + publicacion.getUsuario());
                }
            }
        });

        // Descripción de la publicación
        JLabel textViewDescripcion = new JLabel("<html>" + publicacion.getDescripcion() + "</html>");
        textViewDescripcion.setFont(new Font("Arial", Font.PLAIN, 20));
        textViewDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(textViewDescripcion);

        contentPanel.add(leftPanel, BorderLayout.CENTER);

        // Fecha de publicación
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        JLabel textViewFecha = new JLabel(dateFormat.format(publicacion.getFecha_publicacion()));
        textViewFecha.setFont(new Font("Arial", Font.BOLD, 20));
        textViewFecha.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.setBackground(new Color(211, 205, 192));
        datePanel.add(textViewFecha);

        contentPanel.add(datePanel, BorderLayout.EAST);

        // Iconos para el botón de guardar
        ImageIcon originalIcon = new ImageIcon(Rutas.getImage(Rutas.GUARDAR));
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        ImageIcon newIcon = new ImageIcon(Rutas.getImage(Rutas.GUARDAR_RELLENO));
        Image newImage = newIcon.getImage();
        Image newScaledImage = newImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon newScaledIcon = new ImageIcon(newScaledImage);

        // Botón de guardar
        saveButton = new JButton();
        boolean isGuardada = GuardarPublicacion.estaGuardada(publicacion, usuario_actual, conexion);
        ImageIcon iconoInicial = isGuardada ? newScaledIcon : scaledIcon;
        saveButton.setIcon(iconoInicial);
        isOriginalIcon = isGuardada;

        saveButton.setBackground(null);
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(saveButton);

        // Acción del botón de guardar
        saveButton.addActionListener(e -> {
            if (isOriginalIcon) {
                saveButton.setIcon(scaledIcon);
                LOGGER.log(Level.INFO, Mensajes.getMensaje(Mensajes.PUBLICACION_RETIRADA));
                System.out.println(GuardarPublicacion.retirarGuardadoPublicacion(publicacion, usuario_actual, conexion));
            } else {
                saveButton.setIcon(newScaledIcon);
                LOGGER.log(Level.INFO, Mensajes.getMensaje(Mensajes.PUBLICACION_GUARDADA));
                System.out.println(GuardarPublicacion.guardarPublicacion(publicacion, usuario_actual, conexion));
            }
            isOriginalIcon = !isOriginalIcon;
        });

        add(contentPanel);
    }

    private void abrirPerfil(Usuario autor) {
        // Cerrar la ventana de inicio y la ventana actual
        owner.dispose();
        this.dispose();

        // Abrir la ventana del perfil
        SwingUtilities.invokeLater(() -> new Perfil_Usuario_Vista(conn, autor, usuario_actual).setVisible(true));
    }
}