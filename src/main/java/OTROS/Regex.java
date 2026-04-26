package OTROS;

import VISTAS.GUI;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
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
        boolean esValido = Pattern.matches(filtro, String.valueOf(telefono));
        return esValido;
    }
    public boolean comprobarEmail(String email){
        String filtro = "\\w+@\\w+\\.[a-z]{2,3}";
        boolean esValido = Pattern.matches(filtro, email);
        return esValido;
    }
    public boolean comprobarFecha(String fecha){
        DateTimeFormatter filtro = DateTimeFormatter.ofLocalizedDate("dd/mm/yyyy");
    }

    public boolean comprobarNumero(String numero){

    }
    public boolean comprobarTexto(){

    }

}
