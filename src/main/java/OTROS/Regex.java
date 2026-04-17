package OTROS;

import VISTAS.GUI;

import java.util.regex.Pattern;

public class Regex {
    String contrasena = "1234";
    public boolean verificacionInicioSesion(String usuario){
        boolean esValido = Pattern.matches(contrasena, usuario);
        return esValido;
    }
    public boolean cambiarContrasena(String antigua, String nueva){
        if (antigua.equals(contrasena)) {
            contrasena = nueva;
            return true;
        }
        return false;
    }

    public boolean comprobarTelefono(int telefono){
        String filtro = "\\d{9}";
        boolean esValido = Pattern.matches(filtro, String.valueOf(telefono));
        return esValido;
    }
}
