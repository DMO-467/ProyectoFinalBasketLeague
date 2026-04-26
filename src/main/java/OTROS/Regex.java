package OTROS;

import VISTAS.GUI;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

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

    public boolean comprobarTelefono(int telefono){
        String filtro = "(679)\\d{8}|(679)\\d{11}";
        return Pattern.matches(filtro, String.valueOf(telefono));

    }
    public boolean comprobarEmail(String email){
        String filtro = "\\w+@\\w+\\.[a-z]{2,3}";
        return Pattern.matches(filtro, email);
    }
    public boolean comprobarFecha(String fecha){
        try {
            DateTimeFormatter filtro = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaConvertida = LocalDate.parse(fecha, filtro);
            if (fechaConvertida.isBefore(LocalDate.now())) {
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

    public boolean comprobarNumero(String numero){
        String filtro = "\\d+";
        return Pattern.matches(filtro, numero);
    }
    public boolean comprobarTexto(String texto){
        String filtro = "^[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+(\\s[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+)*$";
        return Pattern.matches(filtro,texto);
    }

}
