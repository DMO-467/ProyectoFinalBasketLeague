package CONTROLADORES;

import OTROS.Conexion;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ControladorPatrocinador {

    public void mostrarPatrocinador(){
        String sql = "SELECT * FROM patrocinador";
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id_patrocinador");
                String nombre = resultSet.getString("nombre_patrocinador");
                int telefono = resultSet.getInt("telefono");
                String email = resultSet.getString("email");
                System.out.println(id + "\t" + nombre + "\t" + telefono + "\t" + email);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
