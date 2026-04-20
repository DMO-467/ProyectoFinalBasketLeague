package CONTROLADORES;

import MODELOS.Patrocinios;
import OTROS.Conexion;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ControladorPatrocinios {

    public DefaultTableModel mostrarPatrocinios(){
        String sql = "SELECT * FROM patrocinios";
        String[] columnas = {"Patrocinador", "Equipo"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id_patrocinador = resultSet.getInt("id_patrocinador");
                int id_equipo = resultSet.getInt("id_equipo");
                Object[] fila = {id_patrocinador, id_equipo};
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }
    public boolean anadirPatrocinio(Patrocinios patrocinio){
        String sql = "INSERT INTO patrocinios(id_patrocinador, id_equipo) VALUES (?, ?)";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, patrocinio.getId_patrocinador());
            preparedStatement.setInt(2, patrocinio.getId_equipo());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminarPatrocinio(int id){
        String sql = "DELETE FROM patrocinio WHERE id_patrocinio = ?";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
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
