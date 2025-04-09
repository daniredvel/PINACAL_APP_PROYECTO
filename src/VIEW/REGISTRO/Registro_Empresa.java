package VIEW.REGISTRO;

import CONTROLLER.ENCRIPTACION.ControladorEncriptacion;
import MODEL.Usuario;
import VIEW.RES.Rutas;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.Connection;

public class Registro_Empresa extends JDialog {
    private final JTextField companyNameField;
    private final JTextField companyEmailField;
    private final JTextField companyPhoneField;
    private final JPasswordField companyPasswordField;
    private final JLabel messageLabel;
    private final JProgressBar passwordStrengthBar;
    private final JCheckBox showPasswordCheckBox;

    public Registro_Empresa(JFrame parent, Connection conn) {
        super(parent, "Registro Empresa", true); // Hacer que el diálogo sea modal
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        // Icono
        setIconImage(Rutas.getImage(Rutas.ICONO));

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(211, 205, 192));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;

        Font font = new Font("Arial", Font.PLAIN, 18);

        // Etiqueta y campo de texto para el nombre de la empresa
        JLabel companyNameLabel = new JLabel("Nombre de la Empresa:");
        companyNameLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(companyNameLabel, constraints);

        companyNameField = new JTextField(20);
        companyNameField.setFont(font);
        constraints.gridx = 1;
        panel.add(companyNameField, constraints);

        // Etiqueta y campo de texto para el correo electrónico de la empresa
        JLabel companyEmailLabel = new JLabel("Correo Electrónico:");
        companyEmailLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(companyEmailLabel, constraints);

        companyEmailField = new JTextField(20);
        companyEmailField.setFont(font);
        constraints.gridx = 1;
        panel.add(companyEmailField, constraints);

        // Etiqueta y campo de texto para el teléfono de la empresa
        JLabel companyPhoneLabel = new JLabel("Teléfono:");
        companyPhoneLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(companyPhoneLabel, constraints);

        companyPhoneField = new JTextField(20);
        companyPhoneField.setFont(font);
        constraints.gridx = 1;
        panel.add(companyPhoneField, constraints);

        // Etiqueta y campo de texto para la contraseña de la empresa
        JLabel companyPasswordLabel = new JLabel("Contraseña:");
        companyPasswordLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(companyPasswordLabel, constraints);

        companyPasswordField = new JPasswordField(20);
        companyPasswordField.setFont(font);
        constraints.gridx = 1;
        panel.add(companyPasswordField, constraints);

        // Barra de progreso para la fuerza de la contraseña
        passwordStrengthBar = new JProgressBar(0, 100);
        passwordStrengthBar.setStringPainted(true);
        constraints.gridx = 1;
        constraints.gridy = 4;
        panel.add(passwordStrengthBar, constraints);

        // Añadir un document listener al campo de contraseña para actualizar la barra de fuerza
        companyPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePasswordStrength();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePasswordStrength();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePasswordStrength();
            }
        });

        // Añadir un checkbox para mostrar/ocultar la contraseña
        showPasswordCheckBox = new JCheckBox("Mostrar Contraseña");
        showPasswordCheckBox.setFont(font);
        showPasswordCheckBox.setBackground(new Color(211, 205, 192));
        showPasswordCheckBox.setFocusPainted(false);
        constraints.gridx = 1;
        constraints.gridy = 5;
        panel.add(showPasswordCheckBox, constraints);

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                companyPasswordField.setEchoChar((char) 0);
            } else {
                companyPasswordField.setEchoChar('•');
            }
        });

        // Botón de registro
        JButton registerButton = new JButton("Registrar");
        registerButton.setFont(font);
        registerButton.setBackground(new Color(174, 101, 7)); // Button background color
        registerButton.setForeground(new Color(255, 255, 255)); // Button text color
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, constraints);

        // Etiqueta para mostrar mensajes
        messageLabel = new JLabel("");
        messageLabel.setFont(font);
        messageLabel.setForeground(Color.RED);
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(messageLabel, constraints);

        // «Escuchador» del botón de registro
        registerButton.addActionListener(e -> {
            // Comprueba si los campos son válidos
            if (validateFields()) {
                // Crea un nuevo usuario con los datos introducidos
                Usuario usuario = new Usuario(companyNameField.getText(), ControladorEncriptacion.encriptar(new String(companyPasswordField.getPassword())), companyEmailField.getText(), Usuario.formatoTelefonoBD(companyPhoneField.getText()), Usuario.TEMPORAL, "");
                // Cierra la ventana actual y abre la de inicio de sesión
                dispose();
                new Registro_Empresa_Direccion(parent, usuario, conn).setVisible(true);
            }
        });

        // Añade el panel al JDialog
        add(panel);
    }

    // METODO que valida los campos del formulario
    private boolean validateFields() {
        if (companyNameField.getText().isEmpty()) {
            messageLabel.setText("El nombre de la empresa es obligatorio");
            return false;
        }
        if (companyEmailField.getText().isEmpty()) {
            messageLabel.setText("El correo electrónico es obligatorio");
            return false;
        }
        if (!companyEmailField.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            messageLabel.setText("Indica un correo electrónico válido");
            return false;
        }
        if (companyPhoneField.getText().isEmpty()) {
            messageLabel.setText("El teléfono es obligatorio");
            return false;
        }
        if (! Usuario.formatoTelefonoBD(companyPhoneField.getText()).matches("\\d{9}")) {
            messageLabel.setText("El teléfono debe contener exactamente 9 números");
            return false;
        }
        if (companyPasswordField.getPassword().length == 0) {
            messageLabel.setText("La contraseña es obligatoria");
            return false;
        }
        String password = new String(companyPasswordField.getPassword());
        if (password.length() < 8) {
            messageLabel.setText("La contraseña debe tener al menos 8 caracteres");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            messageLabel.setText("La contraseña debe contener al menos una letra mayúscula");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            messageLabel.setText("La contraseña debe contener al menos una letra minúscula");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            messageLabel.setText("La contraseña debe contener al menos un número");
            return false;
        }
        if (!password.matches(".*\\W.*")) {
            messageLabel.setText("La contraseña debe contener al menos un símbolo");
            return false;
        }
        return true;
    }

    // METODO para actualizar la barra de fuerza de la contraseña
    private void updatePasswordStrength() {
        String password = new String(companyPasswordField.getPassword());
        int strength = calculatePasswordStrength(password);
        passwordStrengthBar.setValue(strength);
        if (strength < 50) {
            passwordStrengthBar.setString("Débil");
            passwordStrengthBar.setForeground(new Color(255, 102, 102)); // Soft red
        } else if (strength < 75) {
            passwordStrengthBar.setString("Media");
            passwordStrengthBar.setForeground(new Color(255, 178, 102)); // Soft orange
        } else {
            passwordStrengthBar.setString("Fuerte");
            passwordStrengthBar.setForeground(new Color(153, 255, 153)); // Soft green
        }
    }

    // METODO para calcular la fuerza de la contraseña
    private int calculatePasswordStrength(String password) {
        int strength = 0;
        if (password.length() >= 8) strength += 25;
        if (password.matches(".*[A-Z].*")) strength += 25;
        if (password.matches(".*[a-z].*")) strength += 25;
        if (password.matches(".*\\d.*")) strength += 15;
        if (password.matches(".*\\W.*")) strength += 10;
        return strength;
    }
}