package CONTROLADORES;

import MODELOS.Patrocinador;
import OTROS.Conexion;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorPatrocinador {

    public DefaultTableModel mostrarPatrocinador(){
        String sql = "SELECT * FROM patrocinador";
        String[] columnas = {"ID", "Nombre", "Telefono", "Email"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id_patrocinador");
                String nombre = resultSet.getString("nombre_patrocinador");
                int telefono = resultSet.getInt("telefono");
                String email = resultSet.getString("email");
                Object[] fila = {id, nombre, telefono, email};
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }
    public DefaultTableModel mostrarFilasAfectadasPorPatrocinador(int id){
        String sql = "SELECT p.nombre_patrocinador AS patrocinador, e.nombre_equipo AS equipo FROM patrocinador p INNER JOIN patrocinios pe ON p.id_patrocinador=pe.id_patrocinador INNER JOIN equipos e ON pe.id_equipo=e.id_equipo WHERE pe.id_patrocinador = ?";
        String[] columnas = {"Patrocinador", "Equipo"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String patrocinador = resultSet.getString("patrocinador");
                String equipo = resultSet.getString("equipo");
                Object[] fila = {patrocinador, equipo};
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }

    public int cualId(String patrocinador){
        String sql = "SELECT id_patrocinador FROM patrocinador WHERE  nombre_patrocinador= ?";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, patrocinador);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_patrocinador");
            }
            return -1;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean anadirPatrocinador(Patrocinador patrocinador){
        String sql = "INSERT INTO patrocinador(nombre_patrocinador, telefono, email) VALUES (?, ?, ?)";
        try{
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
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
        String sql = "DELETE FROM patrocinador WHERE id_patrocinador = ?";
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
    public String[] mostrarNombrePatrocinador(){
        String sql = "SELECT nombre_patrocinador FROM patrocinador";
        try {
            List<String> patrocinador = new ArrayList<>();
            Conexion c = new Conexion();
            Statement statement = c.realizarConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                patrocinador.add(resultSet.getString("nombre_patrocinador"));
            }
            String[] devolver = new String[patrocinador.size()];
            for (int i = 0; i < devolver.length; i++) {
                devolver[i] = patrocinador.get(i);
            }
            return devolver;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean ExistePatrocinador(Patrocinador patrocinador){
        String sql = "SELECT id_patrocinador FROM patrocinador WHERE nombre_patrocinador = ? AND telefono = ? AND email = ?";
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, patrocinador.getNombre_patrocinador());
            preparedStatement.setInt(2, patrocinador.getTelefono());
            preparedStatement.setString(3, patrocinador.getEmail());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


}
