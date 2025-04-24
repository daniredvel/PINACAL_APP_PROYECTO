package VIEW.ERROR;

import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa la vista en caso de error al no poder conectarse con la base de datos
 * o en caso de que se cierre la conexión de la misma durante la ejecución del programa.
 * <p>
 * La clase muestra un mensaje de error y una disculpa al usuario.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class Error_INICIAR_BD extends JFrame {

    public Error_INICIAR_BD() {
        // Propiedades de la ventana
        setTitle("Error de Conexión");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Icono
        setIconImage(Rutas.getImage(Rutas.ICONO));

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Rutas.getColor(Rutas.BLANCO));

        // Etiqueta de error
        JLabel errorMessage = new JLabel("No se puede acceder a la aplicación.", SwingConstants.CENTER);
        errorMessage.setFont(new Font("Arial", Font.BOLD, 16));
        errorMessage.setForeground(Rutas.getColor(Rutas.ROJO));

        // Etiqueta de disculpas
        JLabel apologyMessage = new JLabel("Lo sentimos por los inconvenientes.", SwingConstants.CENTER);
        apologyMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        apologyMessage.setForeground(Rutas.getColor(Rutas.NEGRO));

        // Añaddor etiquetas al panel
        panel.add(errorMessage, BorderLayout.CENTER);
        panel.add(apologyMessage, BorderLayout.SOUTH);

        // Añadir panel a la ventana
        add(panel);
    }
}