package CONTROLADORES;

import MODELOS.Partidos;
import OTROS.Conexion;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorPartidos {

    public DefaultTableModel mostrarPartidos(){
        String sql = "SELECT p.id_partido, p.fecha, p.hora, el.nombre_equipo AS equipoLocal, ev.nombre_equipo AS equipoVisitante, p.resultado_local, p.resultado_visitante, a1.nombreCompleto AS arbitro1, a2.nombreCompleto AS arbitro2 FROM partidos p JOIN equipos el ON p.id_equipo_local = el.id_equipo JOIN equipos ev ON p.id_equipo_visitante = ev.id_equipo JOIN arbitro a1 ON p.arbitro1 = a1.id_arbitro JOIN arbitro a2 ON p.arbitro2 = a2.id_arbitro";
        String[] columnas = {"ID", "Fecha", "Hora", "Equipo local", "Equipo visitante", "Resultado local", "Resultado visitante", "Arbitro 1" , "Arbitro 2"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("p.id_partido");
                Date fecha = resultSet.getDate("p.fecha");
                Time hora = resultSet.getTime("p.hora");
                String equipoLocal = resultSet.getString("equipoLocal");
                String equipoVisitante = resultSet.getString("equipoVisitante");
                int resultadoLocal = resultSet.getInt("p.resultado_local");
                int resultadoVisitante = resultSet.getInt("p.resultado_visitante");
                String arbitro1 = resultSet.getString("arbitro1");
                String arbitro2 = resultSet.getString("arbitro2");

                Object[] fila = {id, fecha, hora, equipoLocal, equipoVisitante, resultadoLocal, resultadoVisitante, arbitro1, arbitro2};
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }

    public boolean anadirPartido(Partidos partido){
        String sql = "INSERT INTO partidos(fecha, hora, id_equipo_local, id_equipo_visitante, resultado_local, resultado_visitante, arbitro1, arbitro2) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(partido.getFecha()));
            preparedStatement.setTime(2, partido.getHora());
            preparedStatement.setInt(3, partido.getId_equipo_local());
            preparedStatement.setInt(4, partido.getId_equipo_visitante());
            preparedStatement.setInt(5, partido.getResultado_local());
            preparedStatement.setInt(6, partido.getResultado_visitante());
            preparedStatement.setInt(7, partido.getArbitro1());
            preparedStatement.setInt(8, partido.getArbitro2());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean eliminarPartido(int id){
        String sql = "DELETE FROM partidos WHERE id_partido = ?";
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

    public Integer[] mostrarIdesPartidos(){
        String sql = "SELECT id_partido FROM partidos";
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Integer> ides = new ArrayList<>();
            while (resultSet.next()){
                ides.add(resultSet.getInt("id_partido"));
            }
            Integer[] devolver = new Integer[ides.size()];
            for (int i = 0; i < devolver.length; i++) {
                devolver[i] = ides.get(i);
            }
            return devolver;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
