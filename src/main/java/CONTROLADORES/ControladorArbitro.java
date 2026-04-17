package CONTROLADORES;

import OTROS.Conexion;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ControladorArbitro {

    public void mostrarArbitro(){
        String sql = "SELECT * FROM arbitro";
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
                System.out.println(id + "\t" + nombre + "\t" + fechaNacimiento + "\t" + partidosArbitrados + "\t" + anosExperiencia);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
