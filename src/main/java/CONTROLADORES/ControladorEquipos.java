package CONTROLADORES;

import MODELOS.Equipos;
import OTROS.Conexion;

import java.sql.*;

public class ControladorEquipos {
    public void mostrarEquipos(){
        String sql = "SELECT * FROM equipos";
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
                System.out.println(id + "\t" + nombre + "\t" + partidosPerdidos + "\t" + partidosGanados + "\t" + trofeosLiga);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
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
}
