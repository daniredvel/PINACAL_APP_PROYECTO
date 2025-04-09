package VIEW.MENSAJES;

import MODEL.Mensaje;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class Mensajes_Vista extends JPanel{
    public Mensajes_Vista(Mensaje mensaje) {
        setLayout(new BorderLayout());
        setBackground(new Color(211, 205, 192));
        setBorder(BorderFactory.createLineBorder(new Color(174, 101, 7), 2));
        setPreferredSize(new Dimension(800, 150));

        JPanel leftPanel = getJPanel(mensaje, mensaje.isLeido());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(211, 205, 192));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        JLabel textViewFecha = new JLabel(dateFormat.format(mensaje.getFecha_envio()));
        textViewFecha.setFont(new Font("Arial", Font.BOLD, 20));
        textViewFecha.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.setBackground(new Color(211, 205, 192));
        datePanel.add(textViewFecha);

        contentPanel.add(datePanel, BorderLayout.EAST);
        contentPanel.add(leftPanel, BorderLayout.WEST);

        add(contentPanel, BorderLayout.CENTER);
    }
    private static JPanel getJPanel(Mensaje mensaje, boolean leido) {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(211, 205, 192));


        JLabel textViewAsunto = new JLabel(mensaje.getAsunto());
        textViewAsunto.setFont(new Font("Arial", Font.PLAIN, 30));
        textViewAsunto.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel textViewUsuario = new JLabel("Autor: " + mensaje.getDe_usuario().getUsuario());
        textViewUsuario.setFont(new Font("Arial", Font.PLAIN, 20));
        textViewUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel textViewContenido = new JLabel(mensaje.getContenido());
        textViewContenido.setFont(new Font("Arial", Font.PLAIN, 15));
        textViewContenido.setAlignmentX(Component.LEFT_ALIGNMENT);

        textViewContenido.setForeground(leido ? Color.GRAY : Color.BLACK);
        textViewAsunto.setForeground(leido ? Color.GRAY : Color.BLACK);
        textViewUsuario.setForeground(leido ? Color.GRAY : Color.BLACK);

        leftPanel.add(textViewAsunto);
        leftPanel.add(textViewUsuario);
        leftPanel.add(textViewContenido);


        return leftPanel;
    }



   }
