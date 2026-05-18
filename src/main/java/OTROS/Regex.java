package OTROS;

import VISTAS.GUI;

import java.sql.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Regex {
    public boolean verificacionInicioSesion(String usuario){
        String sql = "SELECT contraseña FROM usuarios WHERE usuario = 'administrador'";
        Conexion c = new Conexion();
        String contrasena = "";
        try {
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                contrasena = resultSet.getString("contraseña");
            }
            return contrasena.equals(usuario);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean cambiarContrasena(String antigua, String nueva){
        String sql = "SELECT contraseña FROM usuarios WHERE usuario = 'administrador'";
        Conexion c = new Conexion();
        String contrasena = "";
        try {
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                contrasena = resultSet.getString("contraseña");
            }
            if (antigua.equals(contrasena)) {
                String cambiar = "UPDATE usuarios SET contraseña = ? WHERE usuario = 'administrador'";
                PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(cambiar);
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

    public boolean comprobarTelefono(String telefono){
        String filtro = "^[679]\\d{8}|[679]\\d{11}$";
        return Pattern.matches(filtro, String.valueOf(telefono));

    }
    public boolean comprobarEmail(String email){
        String filtro = "^\\w+@\\w+\\.[a-z]{2,3}$";
        return Pattern.matches(filtro, email);
    }
    public boolean comprobarFecha(LocalDate fecha){
        try {
            int edad = Period.between(fecha, LocalDate.now()).getYears();
            return edad >= 16;
        }catch (Exception e){
            return false;
        }
    }
    public boolean comprobarFechaPartido(String fecha){
        try {
            DateTimeFormatter filtro = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaConvertida = LocalDate.parse(fecha, filtro);
            if (fechaConvertida.isBefore(LocalDate.now()) || fechaConvertida.equals(LocalDate.now())) {
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    public LocalDate textoFecha(String texto){
        DateTimeFormatter filtro = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(texto, filtro);
    }

    public boolean comprobarNumero(String numero, int max){
        String filtro = "^\\d+$";
        int num = Integer.parseInt(numero);
        if (Pattern.matches(filtro, numero)) {
            if (num >= 0 && num <= max) {
                return true;
            }
            return false;
        }else {
            return false;
        }

    }
    public boolean comprobarTexto(String texto){
        String filtro = "^[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+(\\s[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+)*$";
        return Pattern.matches(filtro,texto);
    }

    public boolean comprobarHora(String hora){
        String filtro = "^(2[0123]:[012345][0-9])|(1[0-9]:[012345][0-9])|(0[0-9]:[012345][0-9])|([0-9]:[012345][0-9])$";
        return Pattern.matches(filtro, hora);
    }
    public String mayusculasNombres(String texto) {

        return Arrays.stream(texto.trim().split("\\s+"))
                .map(p -> p.substring(0, 1).toUpperCase()
                        + p.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

}
