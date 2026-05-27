package OTROS;



import java.sql.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Regex {
// Devuelve todos los nombre de usuarios de la tabla usuarios
    public String[] mostrarUsuarios(){
        String sql = "SELECT usuario FROM usuarios";
        Conexion c = new Conexion();
        ArrayList<String> usuarios = new ArrayList<>();
        try {
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                usuarios.add(resultSet.getString("usuario"));
            }
            String[] usuariosCompletos = new String[usuarios.size()];
            for (int i = 0; i < usuariosCompletos.length; i++) {
                usuariosCompletos[i] = usuarios.get(i);
            }
            return usuariosCompletos;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    // Comprueba que la contraseña es correcta dado el usuario y el valor introducido como contraseña
    public boolean verificacionInicioSesion(String usuario, String contrasena){
        //if (contrasena == null) {
            //return false;
            //}
        String sql = "SELECT contraseña FROM usuarios WHERE usuario = ?";
        Conexion c = new Conexion();
        String verificacion = "";
        try {
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, usuario);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                verificacion = resultSet.getString("contraseña");
            }
            return verificacion.equals(contrasena);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Cambia en la base de datos en la tabla usuarios el valor de la contraseña del usuario indicado
    public boolean cambiarContrasena(String usuario, String antigua, String nueva){
        //if (antigua == null || nueva == null) {
            //    return false;
            //}
        String sql = "SELECT contraseña FROM usuarios WHERE usuario = ?";
        Conexion c = new Conexion();
        String contrasena = "";
        try {
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, usuario);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                contrasena = resultSet.getString("contraseña");
            }
            if (antigua.equals(contrasena)) {
                String cambiar = "UPDATE usuarios SET contraseña = ? WHERE usuario = 'administrador'";
                preparedStatement = c.realizarConexion().prepareStatement(cambiar);
                preparedStatement.setString(1, nueva);
                int rowsaffected = preparedStatement.executeUpdate();
                if (rowsaffected > 0) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
// Regex de un telefono de españa
    public boolean comprobarTelefono(String telefono){
        //if (telefono.trim().isEmpty()) {
        //    return false;
        //}
        String filtro = "^[679]\\d{8}|[679]\\d{11}$";
        return Pattern.matches(filtro, String.valueOf(telefono));

    }
    public boolean comprobarEmail(String email){
        //if (email.trim().isEmpty()) {
        //    return false;
        //}
        String filtro = "^\\w+@\\w+\\.[a-z]{2,3}$";
        return Pattern.matches(filtro, email);
    }
    // Comprueba que la fecha de nacimiento del árbitro que se va a introducir sea cierta y que tenga minimo 16 años
    public boolean comprobarFecha(LocalDate fecha){
        //if (fecha == null) {
        //    return false;
        //}
        try {
            int edad = Period.between(fecha, LocalDate.now()).getYears();
            if (edad >= 16 && edad <= 60) {
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
    // Comprueba que la fecha de partido sea anterior a mañana
    public boolean comprobarFechaPartido(LocalDate fecha){
        try {
            if (fecha.getYear() >= 1892) {
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
// Comprueba que el texto introducido tenga el formato de fecha
    public LocalDate textoFecha(String texto){
        DateTimeFormatter filtro = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(texto, filtro);
    }
// Comprueba que la cadena de caracteres introducida es un numero positivo inferior a la cantidad dada
    public boolean comprobarNumero(String numero, int max){
        String filtro = "^\\d+$";
        try {

            if (Pattern.matches(filtro, numero)) {
                int num = Integer.parseInt(numero);
                if (num >= 0 && num <= max) {
                    return true;
                }
                return false;
            }else {
                return false;
            }
        }catch (NumberFormatException e){
            return false;
        }
    }
    // Comprueba que el texto tenga el formato que debe
    public boolean comprobarTexto(String texto){
        //if (texto.trim().isEmpty()) {
         //   return false;
        //}
        String filtro = "^[A-ZÁÉÍÓÚÑÇ][a-záéíóúñç]+(\\s[A-ZÁÉÍÓÚÑÇ][a-záéíóúñç]+)*$";
        return Pattern.matches(filtro,texto);
    }
// Comprueba que se introduce una hora existente
    public boolean comprobarHora(String hora){
        //if (hora.trim().isEmpty()) {
        //    return false;
        //}
        String filtro = "^(2[0123]:[012345][0-9])|(1[0-9]:[012345][0-9])|(0[0-9]:[012345][0-9])|([0-9]:[012345][0-9])$";
        return Pattern.matches(filtro, hora);
    }
    // Cambia el texto dado a un texto con las iniciales en mayusculas
    public String mayusculasNombres(String texto) {
        texto = texto.toLowerCase();
        if(texto == null || texto.trim().isEmpty()){
            return "";
        }
        return Arrays.stream(texto.trim().split("\\s+"))
                .map(p -> p.substring(0, 1).toUpperCase()
                        + p.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

}
