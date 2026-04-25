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
            setContrasena(nueva);
            return true;
        }
        return false;
    }

    public boolean comprobarTelefono(int telefono){
        String filtro = "(679)\\d{8}|(679)\\d{11}";
        boolean esValido = Pattern.matches(filtro, String.valueOf(telefono));
        return esValido;
    }
    public boolean comprobarEmail(String email){
        String filtro = "\\w+@\\w+\\.[a-z]{2,3}";
        boolean esValido = Pattern.matches(filtro, email);
        return esValido;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
