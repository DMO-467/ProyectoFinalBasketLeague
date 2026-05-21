package CONTROLADORES;


import MODELOS.Patrocinador;
import OTROS.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorPatrocinador {
    // Declaramos las variables que vamos a reusar para hacer consultas a la base de datos
    Conexion c = new Conexion();
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    ResultSet resultSet = null;
    String sql;
// Muestra todos los patrocinadores de la tabla patrocinador en la interfaz grafica
    public DefaultTableModel mostrarPatrocinador(){
        sql = "SELECT * FROM patrocinador";
        // Nombres de los campos en la interfaz grafica
        String[] columnas = {"ID", "Nombre", "Telefono", "Email"};
        // Creamos el objeto tabla y lo modificamos para que no se pueda editar los datos clicando en los campos de las filas
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
                int id = resultSet.getInt("id_patrocinador");
                String nombre = resultSet.getString("nombre_patrocinador");
                int telefono = resultSet.getInt("telefono");
                String email = resultSet.getString("email");
                Object[] fila = {id, nombre, telefono, email};
                // Añadimos filas a la tabla de la interfaz
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }
// Devuelve el id del patrocinador dado su nombre
    public int cualId(String patrocinador){
        sql = "SELECT id_patrocinador FROM patrocinador WHERE  nombre_patrocinador= ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, patrocinador);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_patrocinador");
            }
            return -1;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean anadirPatrocinador(Patrocinador patrocinador){
        sql = "INSERT INTO patrocinador(nombre_patrocinador, telefono, email) VALUES (?, ?, ?)";
        try{
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, patrocinador.getNombre_patrocinador());
            preparedStatement.setInt(2, patrocinador.getTelefono());
            preparedStatement.setString(3, patrocinador.getEmail());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean eliminarPatrocinador(int id){
        sql = "DELETE FROM patrocinador WHERE id_patrocinador = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
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

    public boolean modificarPatrocinador(Patrocinador patrocinador){
        sql = "UPDATE patrocinador SET nombre_patrocinador = ?, telefono = ?, email = ? WHERE id_patrocinador = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, patrocinador.getNombre_patrocinador());
            preparedStatement.setInt(2, patrocinador.getTelefono());
            preparedStatement.setString(3, patrocinador.getEmail());
            preparedStatement.setInt(4, patrocinador.getId_patrocinador());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
// Devuelve un array con todos los nombres de todos los patrocinadores de la tabla patrocinador
public DefaultComboBoxModel<String> mostrarNombrePatrocinador() {
    sql = "SELECT nombre_patrocinador FROM patrocinador";

    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

    try {
        statement = c.realizarConexion().createStatement();
        resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            model.addElement(resultSet.getString("nombre_patrocinador"));
        }

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    return model;
}
//  Comprueba que el patrocinador dado se encuentra en la tabla de patrocinador
    public boolean ExistePatrocinador(Patrocinador patrocinador){
        sql = "SELECT id_patrocinador FROM patrocinador WHERE nombre_patrocinador = ? AND telefono = ? AND email = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, patrocinador.getNombre_patrocinador());
            preparedStatement.setInt(2, patrocinador.getTelefono());
            preparedStatement.setString(3, patrocinador.getEmail());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
// Muestra las filas afectadas por el patrocinador en la tabla patrocinios
    public DefaultTableModel mostrarFilasAfectadasPorPatrocinadorEnPatrocinios(int id){
        sql = "SELECT e.nombre_equipo, p.nombre_patrocinador FROM equipos e JOIN patrocinios pa ON e.id_equipo=pa.id_equipo JOIN patrocinador p ON pa.id_patrocinador=p.id_patrocinador WHERE pa.id_patrocinador= ?";
        String[] columnas = {"Equipo", "Patrocinador"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
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
// Devuelve todos los valores de un patrocinador dado su id
    public Patrocinador encontrarPatrocinador(int id){
        sql = "SELECT nombre_patrocinador, telefono, email FROM patrocinador WHERE id_patrocinador = ?";
        Patrocinador patrocinador = new Patrocinador();
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                patrocinador = new Patrocinador(id, resultSet.getString("nombre_patrocinador"), resultSet.getInt("telefono"), resultSet.getString("email"));
            }
            return patrocinador;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
// Genera un csv con todas las filas de la tabla patrocinador
    public boolean imprimirPatrocinadores(){
        sql = "SELECT * FROM patrocinador";

        // Carpeta documentos en la raíz del proyecto
        File directorioDocumentos = new File(System.getProperty("user.dir"), "documentos");
        if (!directorioDocumentos.exists()) {
            directorioDocumentos.mkdir();
        }

        File documento = new File(directorioDocumentos, "patrocinadores.csv");

        try (Statement statement = c.realizarConexion().createStatement();
             ResultSet resultSet = statement.executeQuery(sql);
             BufferedWriter escribir = new BufferedWriter(new FileWriter(documento))) {

            // Encabezado CSV
            escribir.write("ID, Nombre, Telefono, Email");
            escribir.newLine();

            // Filas
            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("id_patrocinador"));
                String nombre = resultSet.getString("nombre_patrocinador");
                String telefono = String.valueOf(resultSet.getInt("telefono"));
                String email = resultSet.getString("email");

                escribir.write(String.join(",", id, nombre, telefono, email));
                escribir.newLine();
            }
            return true;
        }catch (SQLException | IOException e){
            throw new RuntimeException(e);
        }
    }


}
