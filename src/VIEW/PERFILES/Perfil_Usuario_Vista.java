package VIEW.PERFILES;

import CONTROLLER.ControladorDatos;
import MODEL.Publicacion;
import MODEL.Usuario;
import VIEW.PUBLICACIONES.Publicacion_Detalle_Vista;
import VIEW.PUBLICACIONES.Publicacion_Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;

public class Perfil_Usuario_Vista extends JPanel {
    private final Usuario usuario; // Usuario cuyo perfil se está viendo
    private final DefaultListModel<Publicacion> listModel; // Modelo de la lista de publicaciones
    private final Connection conn; // Conexión a la base de datos
    private final Usuario usuario_actual; // Usuario que ha iniciado sesión

    public Perfil_Usuario_Vista(Connection conn, Usuario usuario, Usuario usuario_actual) {
        this.conn = conn;
        this.usuario = usuario;
        this.usuario_actual = usuario_actual;
        this.listModel = new DefaultListModel<>();

        List<Publicacion> publicaciones = ControladorDatos.obtenerPublicaciones(conn, usuario);
        for (Publicacion publicacion : publicaciones) {
            listModel.addElement(publicacion);
        }
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(211, 205, 192));

        // Panel de información del usuario
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(211, 205, 192));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nombreLabel = new JLabel("Nombre: " + usuario.getUsuario());
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(nombreLabel, gbc);

        if (usuario.getDireccion() != null) {
            JLabel direccionLabel = new JLabel("Dirección: " + usuario.getDireccion());
            direccionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            gbc.gridy = 1;
            infoPanel.add(direccionLabel, gbc);
        }

        JLabel telefonoLabel = new JLabel("Teléfono: " + usuario.getTelefono());
        telefonoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 2;
        infoPanel.add(telefonoLabel, gbc);

        JLabel emailLabel = new JLabel("Correo Electrónico: " + usuario.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 3;
        infoPanel.add(emailLabel, gbc);

        add(infoPanel, BorderLayout.NORTH);
        add(getJScrollPane(), BorderLayout.CENTER);
    }

    private JScrollPane getJScrollPane() {
        JList<Publicacion> publicacionesList = getPublicacionJList();
        publicacionesList.setBackground(new Color(211, 205, 192));

        publicacionesList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Publicacion selectedPublicacion = publicacionesList.getSelectedValue();
                    if (selectedPublicacion != null) {
                        Publicacion_Detalle_Vista detalleVista = new Publicacion_Detalle_Vista(null, selectedPublicacion, usuario_actual, conn);
                        detalleVista.setVisible(true);
                    }
                }
            }
        });

        return new JScrollPane(publicacionesList);
    }

    private JList<Publicacion> getPublicacionJList() {
        JList<Publicacion> publicacionesList = new JList<>(listModel);
        publicacionesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        publicacionesList.setCellRenderer((list, publicacion, index, isSelected, cellHasFocus) -> {
            Publicacion_Vista publicacionVista = new Publicacion_Vista(publicacion);
            if (isSelected) {
                publicacionVista.setBackground(new Color(174, 101, 7));
                publicacionVista.setForeground(Color.WHITE);
            } else {
                publicacionVista.setBackground(new Color(211, 205, 192));
                publicacionVista.setForeground(Color.BLACK);
            }
            return publicacionVista;
        });
        return publicacionesList;
    }

    // Método estático para abrir el diálogo modal
    public static void mostrarDialogo(Window parent, Connection conn, Usuario autor, Usuario usuario_actual) {
        JDialog dialog = new JDialog(parent, "Perfil de Usuario", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(parent);

        Perfil_Usuario_Vista perfilVista = new Perfil_Usuario_Vista(conn, autor, usuario_actual);
        dialog.setContentPane(perfilVista);

        dialog.setVisible(true);
    }
}