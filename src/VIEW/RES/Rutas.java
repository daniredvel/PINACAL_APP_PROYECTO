package VIEW.RES;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Rutas {
    // Iconos
    public static final int ICONO = 0;
    public static final int GUARDAR = 1;
    public static final int GUARDAR_RELLENO = 2;
    public static final int MENSAJE = 3;

    private static final String[] RUTAS = {
            "/VIEW/RES/icon.png",
            "/VIEW/RES/guardar.png",
            "/VIEW/RES/guardarrrelleno.png",
            "/VIEW/RES/mensaje.png"
    };

    public static Image getImage(int codigo) {
        return new ImageIcon(Objects.requireNonNull(Rutas.class.getResource(RUTAS[codigo]))).getImage();
    }
}