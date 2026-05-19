package CONTROLADORES;


import MODELOS.Patrocinios;
import OTROS.Conexion;


import javax.swing.table.DefaultTableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorPatrocinios {
    // Declaramos las variables que vamos a usar para comunicarnos con la base de datos de manera en que no tengamos que crear tropecientos objetos sin necesidad
    Conexion c = new Conexion();
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    ResultSet resultSet = null;
    String sql;
// Muestra los nombres de todos los equipos y patrocinadores de la tabla patrocinios en vez de sus ides
    public DefaultTableModel mostrarPatrocinios(){
        sql = "SELECT p.nombre_patrocinador AS patrocinador, e.nombre_equipo AS equipo FROM patrocinador p INNER JOIN patrocinios pe ON p.id_patrocinador=pe.id_patrocinador INNER JOIN equipos e ON pe.id_equipo=e.id_equipo";
        // Son los nombres que se ven como campos en la interfaz
        String[] columnas = {"Patrocinador", "Equipo"};
        // Creamos el objeto tabla y lo modificamos para que no se pueda editar clicando en ningun campo
        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        try {
            statement = c.realizarConexion().createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String patrocinador = resultSet.getString("patrocinador");
                String equipo = resultSet.getString("equipo");
                Object[] fila = {patrocinador, equipo};
                // Añadimos las filas de la consulta a la tabla
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }
    public boolean anadirPatrocinio(Patrocinios patrocinio){
        sql = "INSERT INTO patrocinios(id_patrocinador, id_equipo) VALUES (?, ?)";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, patrocinio.getId_patrocinador());
            preparedStatement.setInt(2, patrocinio.getId_equipo());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminarPatrocinio(Patrocinios patrocinio){
        sql = "DELETE FROM patrocinios WHERE id_patrocinador = ? AND id_equipo = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, patrocinio.getId_patrocinador());
            preparedStatement.setInt(2, patrocinio.getId_equipo());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean modificarPatrocinio(Patrocinios patrocinio, int patrocinadorAntiguo, int equipoAntiguo){
        sql = "UPDATE patrocinios SET id_patrocinador = ?, id_equipo = ? WHERE id_patrocinador = ? AND id_equipo = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, patrocinio.getId_patrocinador());
            preparedStatement.setInt(2, patrocinio.getId_equipo());
            preparedStatement.setInt(3, patrocinadorAntiguo);
            preparedStatement.setInt(4, equipoAntiguo);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
// Dado el nombre de un patrocinador nos devuelve su id
    public int localizarIdPatrocinador(String nombre){
        sql = "SELECT id_patrocinador FROM patrocinador WHERE nombre_patrocinador = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_patrocinador");
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
// Dado el nombre de un equipo nos devuelve su id
    public int localizarIdEquipo(String nombre){
        sql = "SELECT id_equipo FROM equipos WHERE nombre_equipo = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_equipo");
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
// Comprueba que el patrocinio existe en la tabla patrocinios
    public boolean existePatrocinio(Patrocinios patrocinios){
        sql = "SELECT * FROM patrocinios WHERE id_patrocinador = ? AND id_equipo = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, patrocinios.getId_patrocinador());
            preparedStatement.setInt(2, patrocinios.getId_equipo());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
// Devuelve Todos los nombres de patrocinadores y equipos de sus respectivas tablas
    public String[] mostrarPatrociniosNombres(){
        sql = "SELECT p.nombre_patrocinador AS patrocinador, e.nombre_equipo AS equipo FROM patrocinador p INNER JOIN patrocinios pe ON p.id_patrocinador=pe.id_patrocinador INNER JOIN equipos e ON pe.id_equipo=e.id_equipo";
        try {
            statement = c.realizarConexion().createStatement();
            resultSet = statement.executeQuery(sql);
            List<String> patrocinios = new ArrayList<>();
            while (resultSet.next()){
                String patrocinador = resultSet.getString("patrocinador");
                String equipo = resultSet.getString("equipo");
                String patrocinio = patrocinador + equipo;
                patrocinios.add(patrocinio);
            }
            String[] devolver = new String[patrocinios.size()];
            for (int i = 0; i < devolver.length; i++) {
                devolver[i] = patrocinios.get(i);
            }
            return devolver;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    // Crea un csv con los nombres de equipos y patrocinadores que aparecen en la tabla patrocinios
    public boolean imprimirPatrocinios(){
        sql = "SELECT p.nombre_patrocinador AS patrocinador, e.nombre_equipo AS equipo FROM patrocinador p INNER JOIN patrocinios pe ON p.id_patrocinador=pe.id_patrocinador INNER JOIN equipos e ON pe.id_equipo=e.id_equipo";

        // Carpeta documentos en la raíz del proyecto
        File directorioDocumentos = new File(System.getProperty("user.dir"), "documentos");
        if (!directorioDocumentos.exists()) {
            directorioDocumentos.mkdir();
        }

        File documento = new File(directorioDocumentos, "patrocinios.csv");

        try (Statement statement = c.realizarConexion().createStatement();
             ResultSet resultSet = statement.executeQuery(sql);
             BufferedWriter escribir = new BufferedWriter(new FileWriter(documento))) {

            // Encabezado CSV
            escribir.write("Patrocinador, Equipo");
            escribir.newLine();

            // Filas
            while (resultSet.next()) {
                String patrocinador = resultSet.getString("patrocinador");
                String equipo = resultSet.getString("equipo");

                escribir.write(String.join(",", patrocinador, equipo));
                escribir.newLine();
            }
            return true;
        }catch (SQLException | IOException e){
            throw new RuntimeException(e);
        }
    }
}
