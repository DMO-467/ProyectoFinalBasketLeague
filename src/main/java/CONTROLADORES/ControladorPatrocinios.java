package CONTROLADORES;

import OTROS.Conexion;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ControladorPatrocinios {

    public void mostrarPatrocinios(){
        String sql = "SELECT * FROM patrocinios";
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id_patrocinador = resultSet.getInt("id_patrocinador");
                int id_equipo = resultSet.getInt("id_equipo");
                System.out.println(id_equipo + "\t" + id_patrocinador);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
