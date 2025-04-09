package CONTROLLER.ENCRIPTACION;

import MODEL.UTIL.Mensajes;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

public class ControladorEncriptacion {

    /**
     * Encripta una clave utilizando el algoritmo SHA-256.
     * @param clave La clave a encriptar.
     * @return La clave encriptada.
     */

    public static String encriptar(String clave) {
        try {
            // Se utiliza el algoritmo SHA-256 para encriptar la clave
            MessageDigest algoritmo = MessageDigest.getInstance("SHA-256");
            // Se convierte la clave a bytes y se actualiza el algoritmo
            algoritmo.reset();
            algoritmo.update(clave.getBytes(StandardCharsets.UTF_8));
            // Se calcula la clave
            byte[] resumen = algoritmo.digest();
            // Covertimos la clave a un número entero y se formatea a hexadecimal
            return String.format("%064x", new BigInteger(1, resumen));
        } catch (Exception ex) {
            System.out.println(Mensajes.getMensaje(Mensajes.ERROR_AL_CIFRAR));
            return null;
        }
    }

    /**
     * Compara una clave con su versión encriptada.
     * @param clave La clave a comparar.
     * @param claveEncriptada La clave encriptada.
     * @return true si la clave coincide con la encriptada, false en caso contrario.
     */

    public static boolean comprobar(String clave, String claveEncriptada) {
        return Objects.equals(encriptar(clave), claveEncriptada);
    }

}
