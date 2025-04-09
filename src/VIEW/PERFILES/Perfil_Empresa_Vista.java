package VIEW.PERFILES;

import CONTROLLER.ControladorDatos;
import MODEL.Publicacion;
import MODEL.Usuario;
import VIEW.ERROR.Error_INICIAR_BD;
import VIEW.INICIO.Inicio_Vista;
import VIEW.RES.Rutas;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static DB.UTIL.CrearConn.conn;


public class Perfil_Empresa_Vista extends JFrame {
    public static final Logger LOGGER = Logger.getLogger(Perfil_Empresa_Vista.class.getName());
    private Usuario empresa;
    private List<Publicacion> publicaciones;
    private Connection conn;

    public Perfil_Empresa_Vista(Connection conexion, Usuario empresa) {
        this.empresa = empresa;

        LOGGER.log(Level.INFO, "Iniciando vista de perfil");

        // Si la conexión es nula, se crea una nueva
        if (conn == null) conn = conn();
        else Inicio_Vista.conn = conexion;

        // Nos aseguramos de que la conexión no sea nula
        // Si la conexión es nula, se muestra la ventana de error de la aplicación
        if (conn == null) {
            LOGGER.log(Level.SEVERE, "Conexión nula");
            SwingUtilities.invokeLater(() -> new Error_INICIAR_BD().setVisible(true));
        }

        // Icono
        setIconImage(Rutas.getImage(Rutas.ICONO));



        this.publicaciones = ControladorDatos.obtenerPublicaciones(conn, empresa);
        initUI();
    }

    private void initUI() {
        setTitle("Perfil de Empresa");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(211, 205, 192));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nombreLabel = new JLabel("Nombre: " + empresa.getUsuario());
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(nombreLabel, gbc);

        JLabel direccionLabel = new JLabel("Dirección: " + empresa.getDireccion());
        direccionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 1;
        infoPanel.add(direccionLabel, gbc);

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        JPanel publicacionesPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        for (Publicacion publicacion : publicaciones) {
            JPanel publicacionPanel = new JPanel(new BorderLayout());
            publicacionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JLabel tituloLabel = new JLabel(publicacion.getTitulo());
            tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));
            publicacionPanel.add(tituloLabel, BorderLayout.NORTH);

            JTextArea descripcionArea = new JTextArea(publicacion.getDescripcion());
            descripcionArea.setFont(new Font("Arial", Font.PLAIN, 14));
            descripcionArea.setLineWrap(true);
            descripcionArea.setWrapStyleWord(true);
            descripcionArea.setEditable(false);
            publicacionPanel.add(new JScrollPane(descripcionArea), BorderLayout.CENTER);

            publicacionesPanel.add(publicacionPanel);
        }

        mainPanel.add(new JScrollPane(publicacionesPanel), BorderLayout.CENTER);

        add(mainPanel);
    }
}