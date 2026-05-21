package CONTROLADORES;

import MODELOS.Arbitro;
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

public class ControladorArbitro {
    // Declaramos las variables con las que realizamos las consultas a la base de datos para no crear tropecientos objetos sin necesidad
    Conexion c = new Conexion();
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String sql;
    // Metodo con el que mostramos en la pagina principal del proyecto una tabla con todos los campos y filas de la tabla arbitro de la base de datos
    public DefaultTableModel mostrarArbitro(){
        sql = "SELECT * FROM arbitro";
        // Nombres de los campos de la tabla pero solo visual en la interfaz
        String[] columnas = {"ID", "Nombre Completo", "Fecha de nacimiento", "Partidos arbitrados", "Años experiencia"};
        // Creamos el objeto de la tabla y lo modificamos para que el usuario no pueda cambiar el valor de un campo clicando dos veces
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
                int id = resultSet.getInt("id_arbitro");
                String nombre = resultSet.getString("nombreCompleto");
                Date fechaNacimiento = resultSet.getDate("fecha_nacimiento");
                int partidosArbitrados = resultSet.getInt("partidos_arbitrados");
                int anosExperiencia = resultSet.getInt("Años_experiencia");
                Object[] fila = {id, nombre, fechaNacimiento, partidosArbitrados, anosExperiencia};
                // Añadimos las filas a la tabla
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }
    public boolean anadirArbitro(Arbitro arbitro){
        sql = "INSERT INTO arbitro(nombreCompleto, fecha_nacimiento, partidos_arbitrados, años_experiencia) VALUES (?, ?, ?, ?)";
        try{
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, arbitro.getNombreCompleto());
            preparedStatement.setDate(2, Date.valueOf(arbitro.getFecha_nacimiento()));
            preparedStatement.setInt(3, arbitro.getPartidos_arbitrados());
            preparedStatement.setInt(4, arbitro.getAnos_experiencia());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean eliminarArbitro(int id){
        String sql = "DELETE FROM arbitro WHERE id_arbitro = ?";
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

    public boolean modificarArbitro(Arbitro arbitro){
        String sql = "UPDATE arbitro SET nombreCompleto = ?, fecha_nacimiento = ?, partidos_arbitrados = ?, años_experiencia = ? WHERE id_arbitro = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, arbitro.getNombreCompleto());
            preparedStatement.setDate(2, Date.valueOf(arbitro.getFecha_nacimiento()));
            preparedStatement.setInt(3, arbitro.getPartidos_arbitrados());
            preparedStatement.setInt(4, arbitro.getAnos_experiencia());
            preparedStatement.setInt(5, arbitro.getId_arbitro());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    // Comprueba que el id introducido se encuentra en la tabla arbitro
    public boolean existeId(int id){
        String sql = "SELECT id_arbitro FROM arbitro WHERE id_arbitro = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    // Comprueba que el nombre introducido este en la tabla arbitro
    public boolean existeNombre(String nombre){
        String sql = "SELECT nombreCompleto FROM arbitro WHERE nombreCompleto = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    // crea un array del campo nombreCompleto de la tabla arbitro dinámico
    public DefaultComboBoxModel<String> mostrarNombreArbitro() {
        String sql = "SELECT nombreCompleto FROM arbitro";

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        try {
            statement = c.realizarConexion().createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                model.addElement(resultSet.getString("nombreCompleto"));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return model;
    }
    // Muestra el id de un arbitro dado su nombre
    public int cualId(String nombre){
        String sql = "SELECT id_arbitro FROM arbitro WHERE nombreCompleto = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_arbitro");
            }
            return -1;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    // Muestra el nombre de un arbitro dado su id
    public String cualNombre(int id){
        String sql = "SELECT nombreCompleto FROM arbitro WHERE id_arbitro = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nombreCompleto");
            }
            return "";
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
// Muestra aquellas filas de la tabla partidos que tienen relacion con la fila que tiene el id de arbitro dado en forma de tabla
    public DefaultTableModel mostrarFilasAfectadasPorArbitro(int id){
        String sql = "SELECT p.id_partido, p.fecha, p.hora, el.nombre_equipo AS equipoLocal, ev.nombre_equipo AS equipoVisitante, p.resultado_local, p.resultado_visitante, a1.nombreCompleto AS arbitro1, a2.nombreCompleto AS arbitro2 FROM partidos p JOIN equipos el ON p.id_equipo_local = el.id_equipo JOIN equipos ev ON p.id_equipo_visitante = ev.id_equipo JOIN arbitro a1 ON p.arbitro1 = a1.id_arbitro JOIN arbitro a2 ON p.arbitro2 = a2.id_arbitro WHERE arbitro1 = ? OR arbitro2 = ?";
        String[] columnas = {"partido", "fecha", "hora", "equipo local", "equipo visitante", "resultado local", "resultado visitante", "arbitro1", "arbitro2"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int idPartido = resultSet.getInt("p.id_partido");
                Date fecha = resultSet.getDate("p.fecha");
                Time hora = resultSet.getTime("p.hora");
                String equipoLocal = resultSet.getString("equipoLocal");
                String equipoVisitante = resultSet.getString("equipoVisitante");
                int resultadoLocal = resultSet.getInt("p.resultado_local");
                int resultadoVisitante = resultSet.getInt("p.resultado_visitante");
                String arbitro1 = resultSet.getString("arbitro1");
                String arbitro2 = resultSet.getString("arbitro2");

                Object[] fila = {idPartido, fecha, hora, equipoLocal, equipoVisitante, resultadoLocal, resultadoVisitante, arbitro1, arbitro2};
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }
// Devuelve todos los campos de una fila dado su id
    public Arbitro encontrarArbitro(int id) {
        String sql = "SELECT nombreCompleto, fecha_nacimiento, partidos_arbitrados, años_experiencia FROM arbitro WHERE id_arbitro = ?";
        Arbitro arbitro = new Arbitro();
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                arbitro = new Arbitro(id, resultSet.getString("nombreCompleto"), resultSet.getDate("fecha_nacimiento").toLocalDate(), resultSet.getInt("partidos_arbitrados"), resultSet.getInt("años_experiencia"));
            }
            return arbitro;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Crea un documento csv con todas las filas de la tabla arbitro
    public boolean imprimirArbitros(){
        String sql = "SELECT * FROM arbitro";

        // Carpeta documentos en la raíz del proyecto
        File directorioDocumentos = new File(System.getProperty("user.dir"), "documentos");
        if (!directorioDocumentos.exists()) {
            directorioDocumentos.mkdir();
        }

        File documento = new File(directorioDocumentos, "arbitros.csv");

        try (Statement statement = c.realizarConexion().createStatement();
             ResultSet resultSet = statement.executeQuery(sql);
             BufferedWriter escribir = new BufferedWriter(new FileWriter(documento))) {

            // Escribir encabezado
            escribir.write("ID,Nombre,Fecha de nacimiento,Partidos arbitrados,Años de experiencia");
            escribir.newLine();

            // Escribir filas
            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("id_arbitro"));
                String nombre = resultSet.getString("nombreCompleto");
                String fecha = resultSet.getDate("fecha_nacimiento").toString();
                String partidos = String.valueOf(resultSet.getInt("partidos_arbitrados"));
                String anios = String.valueOf(resultSet.getInt("años_experiencia"));

                escribir.write(String.join(",", id, nombre, fecha, partidos, anios));
                escribir.newLine();
            }
            return true;
        }catch (SQLException | IOException e){
            throw new RuntimeException(e);
        }
    }
}
