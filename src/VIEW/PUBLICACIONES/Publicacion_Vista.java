package VIEW.PUBLICACIONES;

import MODEL.Publicacion;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class Publicacion_Vista extends JPanel {
    public Publicacion_Vista(Publicacion publicacion) {
        setLayout(new BorderLayout());
        setBackground(new Color(211, 205, 192));
        setBorder(BorderFactory.createLineBorder(new Color(174, 101, 7), 2));
        setPreferredSize(new Dimension(800, 150));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(211, 205, 192));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = getJPanel(publicacion);

        contentPanel.add(leftPanel, BorderLayout.CENTER);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        JLabel textViewFecha = new JLabel(dateFormat.format(publicacion.getFecha_publicacion()));
        textViewFecha.setFont(new Font("Arial", Font.BOLD, 20));
        textViewFecha.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.setBackground(new Color(211, 205, 192));
        datePanel.add(textViewFecha);

        contentPanel.add(datePanel, BorderLayout.EAST);

        // Crear la etiqueta con el mensaje
        JPanel labelPanel = getJPanel();

        // Añadir el panel de la etiqueta al contentPanel en la parte inferior
        contentPanel.add(labelPanel, BorderLayout.SOUTH);



        add(contentPanel, BorderLayout.CENTER);
    }

    private static JPanel getJPanel() {
        JLabel doubleClickLabel = new JLabel("Doble click para ver la publicación completa");
        doubleClickLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        doubleClickLabel.setForeground(new Color(174, 101, 7));
        doubleClickLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Crear un panel para la etiqueta y añadirla
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(new Color(211, 205, 192));
        labelPanel.add(doubleClickLabel, BorderLayout.CENTER);
        return labelPanel;
    }

    private static JPanel getJPanel(Publicacion publicacion) {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(211, 205, 192));

        JLabel textViewTipo = new JLabel(publicacion.getTipo());
        textViewTipo.setFont(new Font("Arial", Font.PLAIN, 30));
        textViewTipo.setForeground(new Color(174, 101, 7));
        textViewTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(textViewTipo);

        JLabel textViewNombre = new JLabel(publicacion.getTitulo());
        textViewNombre.setFont(new Font("Arial", Font.PLAIN, 30));
        textViewNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(textViewNombre);

        JLabel textViewUsuario = new JLabel("Autor: " + publicacion.getUsuario());
        textViewUsuario.setFont(new Font("Arial", Font.PLAIN, 20));
        textViewUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(textViewUsuario);
        return leftPanel;
    }
}