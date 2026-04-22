package CONTROLADORES;

import MODELOS.Equipos;
import OTROS.Conexion;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorEquipos {
    public DefaultTableModel mostrarEquipos(){
        String sql = "SELECT * FROM equipos";
        String[] columnas = {"ID", "Nombre", "Partidos perdidos", "Partidos ganados", "Trofeos liga"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id_equipo");
                String nombre = resultSet.getString("nombre_equipo");
                int partidosPerdidos = resultSet.getInt("partidos_perdidos");
                int partidosGanados = resultSet.getInt("partidos_ganados");
                int trofeosLiga = resultSet.getInt("trofeos_liga");
                Object[] fila = {id, nombre, partidosPerdidos, partidosGanados, trofeosLiga};
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }
    public boolean anadirEquipo(Equipos equipo){
        String sql = "INSERT INTO equipos(nombre_equipo, partidos_perdidos, partidos_ganados, trofeos_liga) VALUES (?, ?, ?, ?)";
        try{
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, equipo.getNombre_equipo());
            preparedStatement.setInt(2, equipo.getPartidos_perdidos());
            preparedStatement.setInt(3, equipo.getPartidos_ganados());
            preparedStatement.setInt(4, equipo.getTrofeos_liga());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean eliminarEquipo(int id){
        String sql = "DELETE FROM equipos WHERE id_equipo = ?";
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
        String sql = "SELECT id_equipo FROM equipos WHERE id_equipo = ?";
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
        String sql = "SELECT nombre_equipo FROM equipos WHERE nombre_equipo = ?";
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
    public int cualId(String nombre){
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
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    public String[] mostrarNombreEquipos(){
       String sql = "SELECT nombre_equipo FROM equipos";
       try {
           List<String> equipos = new ArrayList<>();
           Conexion c = new Conexion();
           Statement statement = c.realizarConexion().createStatement();
           ResultSet resultSet = statement.executeQuery(sql);
           while (resultSet.next()){
               equipos.add(resultSet.getString("nombre_equipo"));
           }
           String[] devolver = new String[equipos.size()];
           for (int i = 0; i < devolver.length; i++) {
               devolver[i] = equipos.get(i);
           }
           return devolver;
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }
    public String[] noMostrarNombreEquipo(String nombre){
        String sql = "SELECT nombre_equipo FROM equipos WHERE UPPER(nombre_equipo) != UPPER(?)";
        try {
            List<String> equipos = new ArrayList<>();
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                equipos.add(resultSet.getString("nombre_equipo"));
            }
            String[] devolver = new String[equipos.size()];
            for (int i = 0; i < devolver.length; i++) {
                devolver[i] = equipos.get(i);
            }
            return devolver;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
