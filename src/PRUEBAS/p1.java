package PRUEBAS;


import MODEL.Mensaje;
import MODEL.Usuario;
import VIEW.MENSAJES.Mensajes_Vista;

import javax.swing.*;

public class p1 {
    public static void main(String[] args) {

        // Crear un mensaje de ejemplo
        Mensaje mensajeEjemplo = new Mensaje("", "", new Usuario("user1", "password1", "admin@mail.com", "000 000 000", 0, "ADMIN"), new Usuario("user2", "password2", "admin@mail.com", "000 000 000", 0, "ADMIN"), true);
        mensajeEjemplo.setAsunto("Asunto de prueba");
        mensajeEjemplo.setContenido("Este es el contenido del mensaje de prueba.");

        // Crear la vista de mensajes
        Mensajes_Vista mensajesVista = new Mensajes_Vista(mensajeEjemplo);

        // Crear un JFrame para mostrar la vista
        JFrame frame = new JFrame("Vista de Mensajes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 200);
        frame.setLocationRelativeTo(null);
        frame.add(mensajesVista);
        frame.setVisible(true);
    }
}

