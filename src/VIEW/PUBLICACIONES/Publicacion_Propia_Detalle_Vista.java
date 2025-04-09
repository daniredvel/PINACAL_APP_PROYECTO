package VIEW.PUBLICACIONES;

import CONTROLLER.CRUD.PUBLICACION.EliminarPublicacion;
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

public class Publicacion_Propia_Detalle_Vista extends JDialog {
    private boolean isOriginalIcon; // Indica si el icono actual es el original
    private final JButton saveButton; // Botón para guardar o retirar la publicación
    private static final Logger LOGGER = Logger.getLogger(Publicacion_Propia_Detalle_Vista.class.getName());

    public Publicacion_Propia_Detalle_Vista(Window owner, Publicacion publicacion, Usuario usuario_actual, Connection conexion) {
        super(owner, "Detalle de Publicación Propia", ModalityType.APPLICATION_MODAL);

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
        // Panel izquierdo con la información de la publicación
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
                    Publicacion_Propia_Detalle_Vista.this.dispose();

                    SwingUtilities.invokeLater(() -> new Perfil_Usuario_Vista(conn, autor, usuario_actual).setVisible(true));
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
        boolean isGuardada = GuardarPublicacion.estaGuardada(publicacion, usuario_actual, conexion); // Verificar si está guardada
        ImageIcon iconoInicial = isGuardada ? newScaledIcon : scaledIcon;
        saveButton.setIcon(iconoInicial);
        isOriginalIcon = isGuardada; // Inicializar correctamente el estado del icono

        saveButton.setBackground(null);
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(saveButton);

        // Acción del botón de guardar
        saveButton.addActionListener(e -> {
            if (isOriginalIcon) {
                saveButton.setIcon(scaledIcon);
                System.out.printf("Publicación retirada: %s, autor: %s%n", publicacion.getTitulo(), publicacion.getUsuario());
                LOGGER.log(Level.INFO, Mensajes.getMensaje(Mensajes.PUBLICACION_RETIRADA));
                System.out.println(GuardarPublicacion.retirarGuardadoPublicacion(publicacion, usuario_actual, conexion));
            } else {
                saveButton.setIcon(newScaledIcon);
                LOGGER.log(Level.INFO, Mensajes.getMensaje(Mensajes.PUBLICACION_GUARDADA));
                System.out.printf("Publicación guardada: %s, autor: %s%n", publicacion.getTitulo(), publicacion.getUsuario());
                System.out.println(GuardarPublicacion.guardarPublicacion(publicacion, usuario_actual, conexion));
            }
            isOriginalIcon = !isOriginalIcon;
        });
        // Botón de eliminar
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 18));
        deleteButton.setBackground(new Color(220, 70, 90));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(deleteButton);

// Acción del botón de eliminar
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que deseas eliminar esta publicación?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean eliminado = EliminarPublicacion.eliminarPublicacion(publicacion, usuario_actual); // Método para eliminar
                if (eliminado) {
                    LOGGER.log(Level.INFO, "Publicación eliminada: " + publicacion.getTitulo());
                    JOptionPane.showMessageDialog(this, "Publicación eliminada correctamente.", "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Cerrar la ventana actual
                } else {
                    LOGGER.log(Level.SEVERE, "Error al eliminar la publicación: " + publicacion.getTitulo());
                    JOptionPane.showMessageDialog(this, "Error al eliminar la publicación.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(contentPanel);
    }
}