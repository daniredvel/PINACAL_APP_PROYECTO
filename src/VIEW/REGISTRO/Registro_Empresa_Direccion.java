package VIEW.REGISTRO;

import MODEL.Usuario;
import VIEW.INICIO_SESION.InicioSesion_Vista;
import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

import static CONTROLLER.CRUD.USER.AddUsuario.addEmpresa;

public class Registro_Empresa_Direccion extends JDialog {
    private final JTextField streetField;
    private final JTextField numberField;
    private final JTextField localityField;
    private final JTextField municipalityField;
    private final JTextField provinceField;
    private final JTextField postalCodeField;
    private final JTextField countryField;
    private final JLabel messageLabel;

    public Registro_Empresa_Direccion(JFrame parent, Usuario usuario_sin_direccion, Connection conn) {
        super(parent, "Registro Dirección Empresa", true); // Hacer que el diálogo sea modal
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

        // Etiqueta y campo de texto para la calle
        JLabel streetLabel = new JLabel("Calle:");
        streetLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(streetLabel, constraints);

        streetField = new JTextField(20);
        streetField.setFont(font);
        constraints.gridx = 1;
        panel.add(streetField, constraints);

        // Etiqueta y campo de texto para el número
        JLabel numberLabel = new JLabel("Número:");
        numberLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(numberLabel, constraints);

        numberField = new JTextField(20);
        numberField.setFont(font);
        constraints.gridx = 1;
        panel.add(numberField, constraints);

        // Etiqueta y campo de texto para la Localidad
        JLabel localityLabel = new JLabel("Localidad:");
        localityLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(localityLabel, constraints);

        localityField = new JTextField(20);
        localityField.setFont(font);
        constraints.gridx = 1;
        panel.add(localityField, constraints);

        // Etiqueta y campo de texto para el municipio
        JLabel municipalityLabel = new JLabel("Municipio:");
        municipalityLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(municipalityLabel, constraints);

        municipalityField = new JTextField(20);
        municipalityField.setFont(font);
        constraints.gridx = 1;
        panel.add(municipalityField, constraints);

        // Etiqueta y campo de texto para la provincia
        JLabel provinceLabel = new JLabel("Provincia:");
        provinceLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(provinceLabel, constraints);

        provinceField = new JTextField(20);
        provinceField.setFont(font);
        constraints.gridx = 1;
        panel.add(provinceField, constraints);

        // Etiqueta y campo de texto para el código postal
        JLabel postalCodeLabel = new JLabel("Código Postal:");
        postalCodeLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(postalCodeLabel, constraints);

        postalCodeField = new JTextField(20);
        postalCodeField.setFont(font);
        constraints.gridx = 1;
        panel.add(postalCodeField, constraints);

        // Etiqueta y campo de texto para el país
        JLabel countryLabel = new JLabel("País:");
        countryLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 6;
        panel.add(countryLabel, constraints);

        countryField = new JTextField(20);
        countryField.setFont(font);
        constraints.gridx = 1;
        panel.add(countryField, constraints);

        // Botón de registro
        JButton registerButton = new JButton("Registrar");
        registerButton.setFont(font);
        registerButton.setBackground(new Color(174, 101, 7));
        registerButton.setForeground(new Color(255, 255, 255));
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, constraints);

        // Añade etiqueta para mostrar mensajes
        messageLabel = new JLabel("");
        messageLabel.setFont(font);
        messageLabel.setForeground(Color.RED);
        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(messageLabel, constraints);

        // «Escuchador» del botón de registro
        registerButton.addActionListener(e -> {
            if (validateFields()) {
                // Se crea un usuario con los datos introducidos indicando la dirección vacía

                // Se le asigna la direccion con el formato correcto
                usuario_sin_direccion.setDireccion(Usuario.formatoDireccion(streetField.getText(), numberField.getText(), localityField.getText(), municipalityField.getText(), provinceField.getText(), postalCodeField.getText(), countryField.getText()));
                //Se muestra un mensaje con el resultado del registro
                String mensaje = addEmpresa(usuario_sin_direccion);
                JOptionPane.showMessageDialog(null, mensaje, "Registro Empresa", JOptionPane.INFORMATION_MESSAGE);
                //Se cierra la ventana y se abre la de inicio de sesión
                dispose();
                new InicioSesion_Vista(conn).setVisible(true);
            }
        });
        add(panel);
    }

    // METODO para validar los campos
    private boolean validateFields() {
        if (streetField.getText().isEmpty()) {
            messageLabel.setText("La calle es obligatoria");
            return false;
        }
        if (numberField.getText().isEmpty()) {
            messageLabel.setText("El número es obligatorio");
            return false;
        }
        if (localityField.getText().isEmpty()) {
            messageLabel.setText("La localidad es obligatoria");
            return false;
        }
        if (municipalityField.getText().isEmpty()) {
            messageLabel.setText("El municipio es obligatorio");
            return false;
        }
        if (provinceField.getText().isEmpty()) {
            messageLabel.setText("La provincia es obligatoria");
            return false;
        }
        if (postalCodeField.getText().isEmpty()) {
            messageLabel.setText("El código postal es obligatorio");
            return false;
        }
        if (countryField.getText().isEmpty()) {
            messageLabel.setText("El país es obligatorio");
            return false;
        }
        return true;
    }
}