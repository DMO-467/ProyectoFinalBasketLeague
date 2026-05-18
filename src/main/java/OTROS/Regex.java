package OTROS;

import VISTAS.GUI;

import java.sql.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Regex {

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
    public boolean verificacionInicioSesion(String usuario, String contrasena){
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
    public boolean cambiarContrasena(String usuario, String antigua, String nueva){
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
