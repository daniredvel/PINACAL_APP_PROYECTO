package VIEW.ADD;

import CONTROLLER.ControladorDatos;
import MODEL.Publicacion;
import MODEL.Usuario;
import VIEW.ERROR.Error_INICIAR_BD;
import VIEW.INICIO.Inicio_Vista;
import VIEW.PERSONAL.Personal_Empresa;
import VIEW.PUBLICACIONES.Publicacion_Propia_Detalle_Vista;
import VIEW.PUBLICACIONES.Publicacion_Vista;
import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;

import static VIEW.INICIO.Inicio_Vista.LOGGER;

public class Add_Empresa extends JPanel {
    private final DefaultListModel<Publicacion> listModel;
    private static Connection conn = null;

    public Add_Empresa(Usuario usuario_actual, Connection conexion) {

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conexion;
        else Inicio_Vista.conn = conexion;

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, "Conexión nula");
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }


        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(new Color(211, 205, 192));

        Font buttonFont = new Font("Arial", Font.PLAIN, 18);


        JButton nuevaPublicacionButton = new JButton("Nueva Publicación");
        nuevaPublicacionButton.setFont(buttonFont);
        nuevaPublicacionButton.setBackground(new Color(174, 101, 7));
        nuevaPublicacionButton.setForeground(Color.WHITE);
        nuevaPublicacionButton.setPreferredSize(new Dimension(200, 50));
        nuevaPublicacionButton.setMargin(new Insets(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(nuevaPublicacionButton, gbc);

        add(topPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        JScrollPane scrollPane = getJScrollPane(usuario_actual);
        add(scrollPane, BorderLayout.CENTER);

        cargarPublicaciones(usuario_actual);


        nuevaPublicacionButton.addActionListener(e -> {
            Add_Publicacion_Vista dialog = new Add_Publicacion_Vista(this, usuario_actual, conn);
            dialog.setVisible(true);
        });
    }

    private JScrollPane getJScrollPane(Usuario usuario_actual) {
        JList<Publicacion> publicacionesList = getPublicacionJList();

        publicacionesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Publicacion selectedPublicacion = publicacionesList.getSelectedValue();
                    if (selectedPublicacion != null) {
                        Publicacion_Propia_Detalle_Vista detalleVista = new Publicacion_Propia_Detalle_Vista(Add_Empresa.this, selectedPublicacion, usuario_actual, conn);
                        detalleVista.setVisible(true);
                    }
                }
            }
        });

        return new JScrollPane(publicacionesList);
    }

    private JList<Publicacion> getPublicacionJList() {
        JList<Publicacion> publicacionesList = new JList<>(listModel);
        publicacionesList.setBackground(new Color(211, 205, 192));

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

    private void cargarPublicaciones(Usuario usuario_actual) {
        List<Publicacion> publicaciones = ControladorDatos.obtenerPublicaciones(conn, usuario_actual);
        for (Publicacion publicacion : publicaciones) {
            listModel.addElement(publicacion);
        }
    }
}