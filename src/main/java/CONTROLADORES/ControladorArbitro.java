package CONTROLADORES;

import MODELOS.Arbitro;
import OTROS.Conexion;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorArbitro {

    public DefaultTableModel mostrarArbitro(){
        String sql = "SELECT * FROM arbitro";
        String[] columnas = {"ID", "Nombre Completo", "Fecha de nacimiento", "Partidos arbitrados", "Años experiencia"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id_arbitro");
                String nombre = resultSet.getString("nombreCompleto");
                Date fechaNacimiento = resultSet.getDate("fecha_nacimiento");
                int partidosArbitrados = resultSet.getInt("partidos_arbitrados");
                int anosExperiencia = resultSet.getInt("Años_experiencia");
                Object[] fila = {id, nombre, fechaNacimiento, partidosArbitrados, anosExperiencia};
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }
    public boolean anadirArbitro(Arbitro arbitro){
        String sql = "INSERT INTO arbitro(nombreCompleto, fecha_nacimiento, partidos_arbitrados, años_experiencia) VALUES (?, ?, ?, ?)";
        try{
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, arbitro.getNombreCompleto());
            preparedStatement.setDate(2, (Date) arbitro.getFecha_nacimiento());
            preparedStatement.setInt(3, arbitro.getPartidos_arbitrados());
            preparedStatement.setInt(4, arbitro.getAnos_experiencia());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean eliminarArbitro(int id){
        String sql = "DELETE FROM arbitro WHERE id_arbitro = ?";
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

    public boolean existeId(int id){
        String sql = "SELECT id_arbitro FROM arbitro WHERE id_arbitro = ?";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    public boolean existeNombre(String nombre){
        String sql = "SELECT nombreCompleto FROM arbitro WHERE nombreCompleto = ?";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    public String[] mostrarNombreArbitro(){
        String sql = "SELECT nombreCompleto FROM arbitro";
        try {
            List<String> arbitro = new ArrayList<>();
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                arbitro.add(resultSet.getString("nombreCompleto"));
            }
            String[] devolver = new String[arbitro.size()];
            for (int i = 0; i < devolver.length; i++) {
                devolver[i] = arbitro.get(i);
            }
            return devolver;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
