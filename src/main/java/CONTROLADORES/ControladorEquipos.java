package CONTROLADORES;

import MODELOS.Arbitro;
import MODELOS.Equipos;
import MODELOS.Patrocinador;
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

    public boolean modificarEquipo(Equipos equipo){
        String sql = "UPDATE equipos SET nombre_equipo= ?, partidos_perdidos = ?, partidos_ganados = ?, trofeos_liga = ? WHERE id_equipo = ?";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, equipo.getNombre_equipo());
            preparedStatement.setInt(2, equipo.getPartidos_perdidos());
            preparedStatement.setInt(3, equipo.getPartidos_ganados());
            preparedStatement.setInt(4, equipo.getTrofeos_liga());
            preparedStatement.setInt(5, equipo.getId_equipo());
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
    public String cualNombre(int id){
        String sql = "SELECT nombre_equipo FROM equipos WHERE id_equipo = ?";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nombre_equipo");
            }
            return "";
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

    public DefaultTableModel mostrarFilasAfectadasPorEquipoEnPartido(int id){
        String sql = "SELECT p.id_partido, p.fecha, p.hora, el.nombre_equipo AS equipoLocal, ev.nombre_equipo AS equipoVisitante, p.resultado_local, p.resultado_visitante, a1.nombreCompleto AS arbitro1, a2.nombreCompleto AS arbitro2 FROM partidos p JOIN equipos el ON p.id_equipo_local = el.id_equipo JOIN equipos ev ON p.id_equipo_visitante = ev.id_equipo JOIN arbitro a1 ON p.arbitro1 = a1.id_arbitro JOIN arbitro a2 ON p.arbitro2 = a2.id_arbitro WHERE id_equipo_local = ? OR id_equipo_visitante = ?";
        String[] columnas = {"partido", "fecha", "hora", "equipo local", "equipo visitante", "resultado local", "resultado visitante", "arbitro1", "arbitro2"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int idPartido = resultSet.getInt("p.id_partido");
                Date fecha = resultSet.getDate("p.fecha");
                Time hora = resultSet.getTime("p.hora");
                String equipoLocal = resultSet.getString("equipoLocal");
                String equipoVisitante = resultSet.getString("equipoVisitante");
                int resultadoLocal = resultSet.getInt("p.resultado_local");
                int resultadoVisitante = resultSet.getInt("p.resultado_visitante");
                String arbitro1 = resultSet.getString("arbitro1");
                String arbitro2 = resultSet.getString("arbitro2");

                Object[] fila = {idPartido, fecha, hora, equipoLocal, equipoVisitante, resultadoLocal, resultadoVisitante, arbitro1, arbitro2};
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }
    public DefaultTableModel mostrarFilasAfectadasPorEquipoEnPatrocinios(int id){
        String sql = "SELECT e.nombre_equipo, p.nombre_patrocinador FROM equipos e JOIN patrocinios pa ON e.id_equipo=pa.id_equipo JOIN patrocinador p ON pa.id_patrocinador=p.id_patrocinador WHERE pa.id_equipo= ?";
        String[] columnas = {"Equipo", "Patrocinador"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String equipo = resultSet.getString("e.nombre_equipo");
                String patrocinador = resultSet.getString("p.nombre_patrocinador");
                Object[] fila = {equipo, patrocinador};
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }

    public Equipos encontrarEquipo(int id) {
        String sql = "SELECT nombre_equipo, partidos_perdidos, partidos_ganados, trofeos_liga FROM equipos WHERE id_equipo = ?";
        Equipos equipo = new Equipos();
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                equipo = new Equipos(resultSet.getString("nombre_equipo"), resultSet.getInt("partidos_perdidos"), resultSet.getInt("partidos_ganados"), resultSet.getInt("trofeos_liga"));
            }
            return equipo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
