package CONTROLADORES;

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
}
