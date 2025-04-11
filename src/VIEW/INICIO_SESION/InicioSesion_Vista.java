package VIEW.INICIO_SESION;

import MODEL.Usuario;
import VIEW.MAIN.MAIN_FRAME;
import VIEW.REGISTRO.Registro_Vista;
import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.sql.Connection;

import static CONTROLLER.CRUD.USER.LeerUsuario.leerUsuarioPorNombre;
import static CONTROLLER.VALIDATION.ControladorInicioSesion.comprobarPass;

public class InicioSesion_Vista extends JFrame {
    public static Usuario usuario_actual = null;
    private final JTextField userField;
    private final JPasswordField passField;
    private final JLabel messageLabel;

    public InicioSesion_Vista(Connection conn) {
        // Propiedades del frame (Marco)
        setTitle("Inicio de Sesión");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ajustar la ventana al tamaño de la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Icono
        setIconImage(Rutas.getImage(Rutas.ICONO));

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(255, 255, 255)); // Fondo

        // Constraints para el layout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(20, 20, 20, 20); // Padding
        constraints.anchor = GridBagConstraints.WEST;

        // Fuente de los elementos
        Font font = new Font("Arial", Font.PLAIN, 24);

        // Etiqueta de mensaje
        messageLabel = new JLabel("");
        messageLabel.setFont(font);
        messageLabel.setForeground(Color.RED);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(messageLabel, constraints);

        // Etiqueta y Campo de texto del usuario
        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(font);
        userLabel.setForeground(new Color(0, 0, 0)); // Color del texto
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(userLabel, constraints);

        userField = new JTextField(20);
        userField.setFont(font);
        constraints.gridx = 1;
        panel.add(userField, constraints);

        // Etiqueta y Campo de texto de la contraseña
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(font);
        passLabel.setForeground(new Color(0, 0, 0)); // Color del texto
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passLabel, constraints);
        panel.setBackground(new Color(211, 205, 192));

        passField = new JPasswordField(20);
        passField.setFont(font);
        passField.setEchoChar('•'); // Caracter para ocultar la contraseña
        constraints.gridx = 1;
        panel.add(passField, constraints);

        // Checkbox para mostrar la contraseña
        JCheckBox showPasswordCheckBox = new JCheckBox("Mostrar Contraseña");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 18));
        showPasswordCheckBox.setBackground(new Color(211, 205, 192));
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(showPasswordCheckBox, constraints);

        // Botón de inicio de sesión
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setFont(font);
        loginButton.setBackground(new Color(174, 101, 7)); // Color del fondo del botón
        loginButton.setForeground(new Color(255, 255, 255)); // Color del texto del botón
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, constraints);

        // Botón de registro
        JButton registerButton = new JButton("REGÍSTRATE");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        registerButton.setForeground(Color.BLUE);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        constraints.gridx = 1;
        constraints.gridy = 5;
        panel.add(registerButton, constraints);

        // «Escuchador» del botón de inicio de sesión
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            try {
                int result = comprobarPass(username, password);
                System.out.println("comprobarPass result: " + result); // Debug
                switch (result) {
                    case 1:
                        usuario_actual = leerUsuarioPorNombre(username);
                        assert usuario_actual != null;
                        System.out.println("Permisos de usuario: " + usuario_actual.getPermisos()); // Debug
                        System.out.println("Usuario encontrado: " + usuario_actual); // Debug
                        dispose();
                        System.out.println("Creando la vista principal..."); // Debug

                        SwingUtilities.invokeLater(() -> new MAIN_FRAME(usuario_actual, conn).setVisible(true));

                        System.out.println("Vista princiapl __ MAIN_FRAME __ creada y visible."); // Debug
                        break;
                        case -1: case 0:
                        messageLabel.setText("Contraseña o usuario incorrectos");
                        messageLabel.setForeground(new Color(233, 30, 99));
                        break;
                        }
            } catch (Exception ex) {
                messageLabel.setText("Error al iniciar sesión");
                messageLabel.setForeground(new Color(233, 30, 99));
            }
        });
        // «Escuchador» del botón de registro
        registerButton.addActionListener(e -> {
            // Abre la ventana de registro como un diálogo modal
            new Registro_Vista(this, conn).setVisible(true);
        });

        // «Escuchador» del checkbox para mostrar la contraseña
        showPasswordCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                passField.setEchoChar((char) 0); // Muestra la contraseña
            } else {
                passField.setEchoChar('•'); // Oculta la contraseña
            }
        });

        // Añade el panel al marco
        add(panel);
    }
}