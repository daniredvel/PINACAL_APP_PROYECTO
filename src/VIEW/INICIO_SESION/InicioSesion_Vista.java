package VIEW.INICIO_SESION;

import CONTROLLER.CRUD.USER.LeerUsuario;
import CONTROLLER.ENCRIPTACION.ControladorEncriptacion;
import MODEL.Usuario;
import VIEW.MAIN.MAIN_FRAME;
import VIEW.REGISTRO.Registro_Vista;
import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.sql.Connection;

import static CONTROLLER.CRUD.USER.LeerUsuario.leerUsuario;
import static CONTROLLER.VALIDATION.ControladorInicioSesion.*;

import java.io.*;
import java.util.Properties;

public class InicioSesion_Vista extends JFrame {
    public static Usuario usuario_actual = null;
    private  JTextField userField;
    private  JPasswordField passField;
    private  JLabel messageLabel;
    private  JCheckBox saveCredentialsCheckBox;

    private static final String CRED_FILE = "datos.properties";

    public InicioSesion_Vista(Connection conn) {

        // Revisa si hay credenciales guardadas
        File credFile = new File(CRED_FILE);
        setBackground(Rutas.getColor(Rutas.GRIS));
        if (credFile.exists()) {
            try {
                Properties props = new Properties();
                FileInputStream fis = new FileInputStream(CRED_FILE);
                props.load(fis);
                fis.close();

                String usuario = props.getProperty("usuario");
                String contrasena = props.getProperty("contrasena");

                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "¿Quieres iniciar sesión como: " + usuario + "?",
                        "Inicio Rápido",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    int result;
                    if (usuario.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                        result = comprobarCorreo(usuario, contrasena);
                        usuario_actual = leerUsuario(usuario, LeerUsuario.EMAIL);
                    } else {
                        result = comprobarPassCifrada(usuario, contrasena);
                        usuario_actual = leerUsuario(usuario, LeerUsuario.NOMBRE);
                    }

                    if (result == 1 && usuario_actual != null) {
                        dispose();
                        SwingUtilities.invokeLater(() -> new MAIN_FRAME(usuario_actual, conn).setVisible(true));
                        return;
                    } else {
                        JOptionPane.showMessageDialog(this, "Credenciales guardadas inválidas", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error leyendo las credenciales guardadas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Configuración del JFrame
        setTitle("Inicio de Sesión");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(Rutas.getImage(Rutas.ICONO));
        setBackground(Rutas.getColor(Rutas.GRIS));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Rutas.getColor(Rutas.GRIS));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(20, 20, 20, 20);
        constraints.anchor = GridBagConstraints.WEST;

        Font font = new Font("Arial", Font.PLAIN, 24);

        messageLabel = new JLabel("");
        messageLabel.setFont(font);
        messageLabel.setForeground(Color.RED);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(messageLabel, constraints);

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(font);
        userLabel.setForeground(Rutas.getColor(Rutas.NEGRO));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(userLabel, constraints);

        userField = new JTextField(20);
        userField.setFont(font);
        constraints.gridx = 1;
        panel.add(userField, constraints);

        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(font);
        passLabel.setForeground(Rutas.getColor(Rutas.NEGRO));
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passLabel, constraints);

        passField = new JPasswordField(20);
        passField.setFont(font);
        passField.setEchoChar('•');
        constraints.gridx = 1;
        panel.add(passField, constraints);

        JCheckBox showPasswordCheckBox = new JCheckBox("Mostrar Contraseña");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 18));
        showPasswordCheckBox.setBackground(Rutas.getColor(Rutas.GRIS));
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(showPasswordCheckBox, constraints);

        // Nuevo checkbox para guardar credenciales
        saveCredentialsCheckBox = new JCheckBox("Guardar credenciales");
        saveCredentialsCheckBox.setFont(new Font("Arial", Font.PLAIN, 18));
        saveCredentialsCheckBox.setBackground(Rutas.getColor(Rutas.GRIS));
        constraints.gridy = 4;
        panel.add(saveCredentialsCheckBox, constraints);

        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setFont(font);
        loginButton.setBackground(Rutas.getColor(Rutas.NARANJA));
        loginButton.setForeground(Rutas.getColor(Rutas.BLANCO));
        constraints.gridy = 5;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, constraints);

        JButton registerButton = new JButton("REGÍSTRATE");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        registerButton.setForeground(Color.BLUE);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        constraints.gridy = 6;
        panel.add(registerButton, constraints);

        // Evento iniciar sesión
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            try {
                int result;
                if(username.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) result = comprobarCorreo(username, password);
                else result = comprobarPass(username, password);

                switch (result) {
                    case 1:
                        if(username.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) usuario_actual = leerUsuario(username, LeerUsuario.EMAIL);
                        else usuario_actual = leerUsuario(username, LeerUsuario.NOMBRE);

                        if (saveCredentialsCheckBox.isSelected()) {
                            saveCredentials(username, ControladorEncriptacion.encriptar(password));
                        }

                        dispose();
                        SwingUtilities.invokeLater(() -> new MAIN_FRAME(usuario_actual, conn).setVisible(true));
                        break;
                    case -1: case 0:
                        messageLabel.setText("Contraseña o usuario incorrectos");
                        messageLabel.setForeground(Rutas.getColor(Rutas.ROJO));
                        break;
                }
            } catch (Exception ex) {
                messageLabel.setText("Error al iniciar sesión");
                messageLabel.setForeground(Rutas.getColor(Rutas.ROJO));
            }
        });

        registerButton.addActionListener(e -> new Registro_Vista(this, conn).setVisible(true));

        showPasswordCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                passField.setEchoChar((char) 0);
            } else {
                passField.setEchoChar('•');
            }
        });

        add(panel);
    }

    private void saveCredentials(String usuario, String contrasena) {
        try {
            Properties props = new Properties();
            props.setProperty("usuario", usuario);
            props.setProperty("contrasena", contrasena);
            FileOutputStream fos = new FileOutputStream(CRED_FILE);
            props.store(fos, "Credenciales guardadas");
            fos.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar credenciales", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
