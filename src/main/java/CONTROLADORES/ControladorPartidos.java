package CONTROLADORES;

import MODELOS.Partidos;
import OTROS.Conexion;

import java.sql.*;

public class ControladorPartidos {

    public void mostrarPartidos(){
        String sql = "SELECT * FROM partidos";
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id_partido");
                Date fecha = resultSet.getDate("fecha");
                Time hora = resultSet.getTime("hora");
                int idEquipoLocal = resultSet.getInt("id_equipo_local");
                int idEquipoVisitante = resultSet.getInt("id_equipo_visitante");
                int resultadoLocal = resultSet.getInt("resultado_local");
                int resultadoVisitante = resultSet.getInt("resultado_visitante");
                int arbitro1 = resultSet.getInt("arbitro1");
                int arbitro2 = resultSet.getInt("arbitro2");

                System.out.println(id + "\t" + fecha + "\t" + hora + "\t" + idEquipoLocal + "\t" + idEquipoVisitante + "\t" + resultadoLocal + "\t" + resultadoVisitante + "\t" + arbitro1 + "\t" + arbitro2);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean anadirPartido(Partidos partido){
        String sql = "INSERT INTO partidos(fecha, hora, id_equipo_local, id_equipo_visitante, resultado_local, resultado_visitante, arbitro1, arbitro2) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setDate(1, (Date) partido.getFecha());
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

}
