package VIEW.REGISTRO;

import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

/**
 * Clase que representa la vista de registro de usuario o empresa.
 * Esta vista muestra la opción de registrar un usuario o una empresa..
 * A través de dos botones, el usuario puede elegir entre registrarse como empresa o como usuario.
 * <p>
 * La clase se encarga de redirigir al usuario en función de la opción de registro deseada.
 *
 * @author DANIEL REDONDO VELASCO
 * @version 1.0
 * @since 2025
 */

public class Registro_Vista extends JDialog {
    public Registro_Vista(JFrame parent, Connection conn) {
        super(parent, "REGISTRO", true); // Hacer que el diálogo sea modal
        setSize(600, 400); // Establecer tamaño de la ventana
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        // Icono
        setIconImage(Rutas.getImage(Rutas.ICONO));

        // Crear Panel y Layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Rutas.getColor(Rutas.GRIS));

        // Constraints para el layout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(20, 20, 20, 20);
        constraints.anchor = GridBagConstraints.CENTER;

        // Fuente para los elementos
        Font font = new Font("Arial", Font.PLAIN, 24);

        // Añade una etiqueta
        JLabel label = new JLabel("Escoge una opción para registrarte");
        label.setFont(font);
        label.setForeground(Rutas.getColor(Rutas.NEGRO));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(label, constraints);

        // Añade el botón de la opción de EMPRESA
        JButton empresaButton = new JButton("EMPRESA");
        empresaButton.setFont(font);
        empresaButton.setBackground(Rutas.getColor(Rutas.NARANJA));
        empresaButton.setForeground(Rutas.getColor(Rutas.BLANCO));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(empresaButton, constraints);

        // Añade el botón de la opción de USUARIO
        JButton usuarioButton = new JButton("USUARIO");
        usuarioButton.setFont(font);
        usuarioButton.setBackground(Rutas.getColor(Rutas.NARANJA));
        usuarioButton.setForeground(Rutas.getColor(Rutas.BLANCO));
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(usuarioButton, constraints);

        // «Escuchadores» de los botones
        empresaButton.addActionListener(e -> {
            dispose();
            new Registro_Empresa(parent, conn).setVisible(true);
        });

        usuarioButton.addActionListener(e -> {
            dispose();
            new Registro_Usuario(parent, conn).setVisible(true);
        });

        // Añade el panel al marco
        add(panel);
    }
}