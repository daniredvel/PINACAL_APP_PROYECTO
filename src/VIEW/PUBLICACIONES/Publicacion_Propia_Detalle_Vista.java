package VIEW.PUBLICACIONES;

import MODEL.Publicacion;
import MODEL.Usuario;
import VIEW.PERFILES.Perfil_Usuario_Vista;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class Publicacion_Propia_Detalle_Vista extends JDialog {
    private final Usuario usuario_actual;
    private final Connection conn;
    private final JPanel owner;

    public Publicacion_Propia_Detalle_Vista(JPanel owner, Publicacion publicacion, Usuario usuario_actual, Connection conn) {
        this.usuario_actual = usuario_actual;
        this.conn = conn;
        this.owner = owner;

        // Configuración inicial
        setSize(800, 300);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle(publicacion.getTitulo());
    }

    private void abrirPerfil(Usuario autor) {
        // Cerrar el JDialog actual
        this.dispose();

        // Crear un nuevo JDialog modal para mostrar el perfil
        JDialog perfilDialog = new JDialog(SwingUtilities.getWindowAncestor(owner), "Perfil de Usuario", Dialog.ModalityType.APPLICATION_MODAL);
        perfilDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        perfilDialog.setSize(800, 600); // Tamaño de la ventana
        perfilDialog.setLocationRelativeTo(owner); // Centrar la ventana

        // Crear la vista del perfil y agregarla al JDialog
        Perfil_Usuario_Vista perfilVista = new Perfil_Usuario_Vista(conn, autor, usuario_actual);
        perfilDialog.setContentPane(perfilVista);

        // Hacer visible el JDialog
        perfilDialog.setVisible(true);
    }
}