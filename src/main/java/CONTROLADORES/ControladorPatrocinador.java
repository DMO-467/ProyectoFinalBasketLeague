package CONTROLADORES;

import MODELOS.Patrocinador;
import OTROS.Conexion;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ControladorPatrocinador {

    public DefaultTableModel mostrarPatrocinador(){
        String sql = "SELECT * FROM patrocinador";
        String[] columnas = {"ID", "Nombre", "Telefono", "Email"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id_patrocinador");
                String nombre = resultSet.getString("nombre_patrocinador");
                int telefono = resultSet.getInt("telefono");
                String email = resultSet.getString("email");
                Object[] fila = {id, nombre, telefono, email};
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
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
