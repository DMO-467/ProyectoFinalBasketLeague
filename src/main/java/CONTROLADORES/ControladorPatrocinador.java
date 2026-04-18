package CONTROLADORES;

import MODELOS.Patrocinador;
import OTROS.Conexion;

import java.sql.*;

public class ControladorPatrocinador {

    public void mostrarPatrocinador(){
        String sql = "SELECT * FROM patrocinador";
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id_patrocinador");
                String nombre = resultSet.getString("nombre_patrocinador");
                int telefono = resultSet.getInt("telefono");
                String email = resultSet.getString("email");
                System.out.println(id + "\t" + nombre + "\t" + telefono + "\t" + email);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean anadirPatrocinador(Patrocinador patrocinador){
        String sql = "INSERT INTO patrocinador(nombre_patrocinador, telefono, email) VALUES (?, ?, ?)";
        try{
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, patrocinador.getNombre_patrocinador());
            preparedStatement.setInt(2, patrocinador.getTelefono());
            preparedStatement.setString(3, patrocinador.getEmail());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
