package VIEW.PUBLICACIONES;

import CONTROLLER.CRUD.PUBLICACION.EliminarPublicacion;
import MODEL.Publicacion;
import MODEL.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que representa la vista detallada de una publicación propia del usuario en la aplicación.
 * Muestra la información completa de la publicación, incluyendo su tipo, título, autor y fecha de publicación.
 * Permite al usuario eliminar su propia publicación.
 * <p>
 * La clase cuenta con componentes de interfaz de usuario para mostrar la información de la publicación y métodos para manejar los eventos de la publicación.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class Publicacion_Propia_Detalle_Vista extends Publicacion_Detalle_Vista {
    private static final Logger LOGGER = Logger.getLogger(Publicacion_Propia_Detalle_Vista.class.getName());


    public Publicacion_Propia_Detalle_Vista(JPanel owner, Publicacion publicacion, Usuario usuario_actual, Connection conn) {
        super(owner, publicacion, usuario_actual, conn); // Llama al constructor de la clase base

        // Botón de eliminar
        // Botón para eliminar la publicación
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 18));
        deleteButton.setBackground(new Color(220, 70, 90));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);

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
                boolean eliminado = EliminarPublicacion.eliminarPublicacion(publicacion); // METODO para eliminar
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

        // Agregar el botón al diseño existente
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(211, 205, 192));
        buttonPanel.add(deleteButton);

        // Añadir el panel del botón al final del contenido
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
}