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
    // Iconos
    public static final int NEGRO = 0;
    public static final int BLANCO = 1;
    public static final int NARANJA = 2;
    public static final int CREMA = 3;
    public static final int GRIS = 4;
    public static final int GRIS_OSCURO = 5;
    public static final int ROJO = 6;
    public static final int AZUL = 7;
    public static final int ROJO_SUAVE = 8;
    public static final int NARANAJA_SUAVE = 9;
    public static final int VERDE_SUAVE = 10;

    private static final Color[] COLORES = {
            new Color(0,   0,   0),
            new Color(255, 255, 255),
            new Color(174, 101, 7),
            new Color(224, 215, 183),
            new Color(211, 205, 192),
            new Color(149, 138, 122),
            new Color(233, 30,  99),
            new Color(30,  145, 233),
            new Color(255, 102, 102),
            new Color(255, 178, 102),
            new Color(153, 255, 153),

    };

    public static Color getColor(int codigo) {
        return COLORES[codigo];
    }
}