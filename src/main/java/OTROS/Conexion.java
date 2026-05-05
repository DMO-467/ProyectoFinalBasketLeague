package OTROS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private String url = "jdbc:mysql://localhost:3306/BasketLeague";
    private String usuario = "root";
    private String contrasena = "Mysql123!";
    Connection connection = null;
    
    public Connection realizarConexion(){
        try {
            connection = DriverManager.getConnection(url, usuario, contrasena);
            return connection;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean cerrarConexion(){
        try {
            if (connection != null) {
                connection.close();
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean consultarConexion(){
        if (connection != null) {
            return true;
        }else {
            return false;
        }
    }
}
