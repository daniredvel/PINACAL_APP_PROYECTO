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

public class Add_Empresa extends JFrame {
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

        // Icono
        setIconImage(Rutas.getImage(Rutas.ICONO));

        setTitle("Publicaciones del Usuario");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(211, 205, 192)); // Color de fondo de las publicaciones

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(new Color(211, 205, 192));

        Font buttonFont = new Font("Arial", Font.PLAIN, 18);

        JButton inicioButton = new JButton("Inicio");
        inicioButton.setFont(buttonFont);
        inicioButton.setBackground(new Color(174, 101, 7));
        inicioButton.setForeground(Color.WHITE);
        inicioButton.setPreferredSize(new Dimension(150, 50));
        inicioButton.setMargin(new Insets(10, 20, 10, 20));

        JButton personalButton = new JButton("Personal");
        personalButton.setFont(buttonFont);
        personalButton.setBackground(new Color(174, 101, 7));
        personalButton.setForeground(Color.WHITE);
        personalButton.setPreferredSize(new Dimension(150, 50));
        personalButton.setMargin(new Insets(10, 20, 10, 20));

        JButton anadirButton = new JButton("Mis Publicaciones");
        anadirButton.setFont(buttonFont);
        anadirButton.setBackground(new Color(174, 101, 7));
        anadirButton.setForeground(Color.WHITE);
        anadirButton.setMargin(new Insets(10, 20, 10, 20)); // Ajusta el margen para que se adapte al texto
        anadirButton.setPreferredSize(null); // Permite que el tamaño se ajuste automáticamente

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
        topPanel.add(inicioButton, gbc);

        gbc.gridx = 1;
        topPanel.add(personalButton, gbc);

        gbc.gridx = 2;
        topPanel.add(anadirButton, gbc);

        gbc.gridx = 3;
        topPanel.add(nuevaPublicacionButton, gbc);

        add(topPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        JScrollPane scrollPane = getJScrollPane(usuario_actual);
        add(scrollPane, BorderLayout.CENTER);

        cargarPublicaciones(usuario_actual);

        inicioButton.addActionListener(e -> {
            dispose();
            new Inicio_Vista(usuario_actual, conn).setVisible(true);
        });
        personalButton.addActionListener(e -> {
            dispose();
            new Personal_Empresa(usuario_actual, conn).setVisible(true);
        });
        anadirButton.addActionListener(e -> {
            dispose();
            new Add_Empresa(usuario_actual, conn).setVisible(true);
        });
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