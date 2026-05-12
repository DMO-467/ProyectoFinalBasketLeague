package CONTROLADORES;

import MODELOS.Patrocinador;
import MODELOS.Patrocinios;
import OTROS.Conexion;
import OTROS.Regex;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorPatrocinios {

    public DefaultTableModel mostrarPatrocinios(){
        String sql = "SELECT p.nombre_patrocinador AS patrocinador, e.nombre_equipo AS equipo FROM patrocinador p INNER JOIN patrocinios pe ON p.id_patrocinador=pe.id_patrocinador INNER JOIN equipos e ON pe.id_equipo=e.id_equipo";
        String[] columnas = {"Patrocinador", "Equipo"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String patrocinador = resultSet.getString("patrocinador");
                String equipo = resultSet.getString("equipo");
                Object[] fila = {patrocinador, equipo};
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

    public boolean eliminarPatrocinio(Patrocinios patrocinio){
        String sql = "DELETE FROM patrocinios WHERE id_patrocinador = ? AND id_equipo = ?";
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
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean modificarPatrocinio(Patrocinios patrocinio, int patrocinadorAntiguo, int equipoAntiguo){
        String sql = "UPDATE patrocinios SET id_patrocinador = ?, id_equipo = ? WHERE id_patrocinador = ? AND id_equipo = ?";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, patrocinio.getId_patrocinador());
            preparedStatement.setInt(2, patrocinio.getId_equipo());
            preparedStatement.setInt(3, patrocinadorAntiguo);
            preparedStatement.setInt(4, equipoAntiguo);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public int localizarIdPatrocinador(String nombre){
        String sql = "SELECT id_patrocinador FROM patrocinador WHERE nombre_patrocinador = ?";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_patrocinador");
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int localizarIdEquipo(String nombre){
        String sql = "SELECT id_equipo FROM equipos WHERE nombre_equipo = ?";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_equipo");
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean existePatrocinio(Patrocinios patrocinios){
        String sql = "SELECT * FROM patrocinios WHERE id_patrocinador = ? AND id_equipo = ?";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, patrocinios.getId_patrocinador());
            preparedStatement.setInt(2, patrocinios.getId_equipo());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public String[] mostrarPatrociniosNombres(){
        String sql = "SELECT p.nombre_patrocinador AS patrocinador, e.nombre_equipo AS equipo FROM patrocinador p INNER JOIN patrocinios pe ON p.id_patrocinador=pe.id_patrocinador INNER JOIN equipos e ON pe.id_equipo=e.id_equipo";
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> patrocinios = new ArrayList<>();
            while (resultSet.next()){
                String patrocinador = resultSet.getString("patrocinador");
                String equipo = resultSet.getString("equipo");
                String patrocinio = patrocinador + equipo;
                patrocinios.add(patrocinio);
            }
            String[] devolver = new String[patrocinios.size()];
            for (int i = 0; i < devolver.length; i++) {
                devolver[i] = patrocinios.get(i);
            }
            return devolver;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
