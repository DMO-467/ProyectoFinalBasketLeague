package CONTROLADORES;

import OTROS.Conexion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
