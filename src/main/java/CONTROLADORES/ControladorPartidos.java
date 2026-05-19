package CONTROLADORES;


import MODELOS.PartidoItem;
import MODELOS.Partidos;
import OTROS.Conexion;

import javax.swing.table.DefaultTableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorPartidos {
        Conexion c = new Conexion();
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String sql;

    public DefaultTableModel mostrarPartidos(){
        sql = "SELECT p.id_partido, p.fecha, p.hora, el.nombre_equipo AS equipoLocal, ev.nombre_equipo AS equipoVisitante, p.resultado_local, p.resultado_visitante, a1.nombreCompleto AS arbitro1, a2.nombreCompleto AS arbitro2 FROM partidos p JOIN equipos el ON p.id_equipo_local = el.id_equipo JOIN equipos ev ON p.id_equipo_visitante = ev.id_equipo JOIN arbitro a1 ON p.arbitro1 = a1.id_arbitro JOIN arbitro a2 ON p.arbitro2 = a2.id_arbitro";
        String[] columnas = {"ID", "Fecha", "Hora", "Equipo local", "Equipo visitante", "Resultado local", "Resultado visitante", "Arbitro 1" , "Arbitro 2"};
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
                int id = resultSet.getInt("p.id_partido");
                Date fecha = resultSet.getDate("p.fecha");
                Time hora = resultSet.getTime("p.hora");
                String equipoLocal = resultSet.getString("equipoLocal");
                String equipoVisitante = resultSet.getString("equipoVisitante");
                int resultadoLocal = resultSet.getInt("p.resultado_local");
                int resultadoVisitante = resultSet.getInt("p.resultado_visitante");
                String arbitro1 = resultSet.getString("arbitro1");
                String arbitro2 = resultSet.getString("arbitro2");

                Object[] fila = {id, fecha, hora, equipoLocal, equipoVisitante, resultadoLocal, resultadoVisitante, arbitro1, arbitro2};
                modelo.addRow(fila);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return modelo;
    }

    public boolean anadirPartido(Partidos partido){
        sql = "INSERT INTO partidos(fecha, hora, id_equipo_local, id_equipo_visitante, resultado_local, resultado_visitante, arbitro1, arbitro2) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(partido.getFecha()));
            preparedStatement.setTime(2, partido.getHora());
            preparedStatement.setInt(3, partido.getId_equipo_local());
            preparedStatement.setInt(4, partido.getId_equipo_visitante());
            preparedStatement.setInt(5, partido.getResultado_local());
            preparedStatement.setInt(6, partido.getResultado_visitante());
            preparedStatement.setInt(7, partido.getArbitro1());
            preparedStatement.setInt(8, partido.getArbitro2());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean eliminarPartido(int id){
        sql = "DELETE FROM partidos WHERE id_partido = ?";
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
    public boolean modificarPartido(Partidos partido){
        sql = "UPDATE partidos SET fecha= ?, hora = ?, id_equipo_local = ?, id_equipo_visitante = ?, resultado_local = ?, resultado_visitante = ?, arbitro1= ?, arbitro2 = ? WHERE id_partido = ?";
        try {
            preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(partido.getFecha()));
            preparedStatement.setTime(2, partido.getHora());
            preparedStatement.setInt(3, partido.getId_equipo_local());
            preparedStatement.setInt(4, partido.getId_equipo_visitante());
            preparedStatement.setInt(5, partido.getResultado_local());
            preparedStatement.setInt(6, partido.getResultado_visitante());
            preparedStatement.setInt(7, partido.getArbitro1());
            preparedStatement.setInt(8, partido.getArbitro2());
            preparedStatement.setInt(9, partido.getId_partido());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public PartidoItem[] mostrarPartidosEliminarOModificar(){
        sql = "SELECT p.id_partido, p.fecha, p.hora, p.id_equipo_local, p.id_equipo_visitante, p.resultado_local, p.resultado_visitante, p.arbitro1, p.arbitro2, el.nombre_equipo AS equipoLocal, ev.nombre_equipo AS equipoVisitante FROM partidos p JOIN equipos el ON p.id_equipo_local = el.id_equipo JOIN equipos ev ON p.id_equipo_visitante = ev.id_equipo";
        try {

            statement = c.realizarConexion().createStatement();
            resultSet = statement.executeQuery(sql);

            List<PartidoItem> lista = new ArrayList<>();

            while (resultSet.next()) {

                Partidos partido = new Partidos(
                        resultSet.getInt("id_partido"),
                        resultSet.getDate("fecha").toLocalDate(),
                        resultSet.getTime("hora"),
                        resultSet.getInt("id_equipo_local"),
                        resultSet.getInt("id_equipo_visitante"),
                        resultSet.getInt("resultado_local"),
                        resultSet.getInt("resultado_visitante"),
                        resultSet.getInt("arbitro1"),
                        resultSet.getInt("arbitro2")
                );

                String texto = resultSet.getDate("fecha").toLocalDate()
                        + " - "
                        + resultSet.getString("equipoLocal")
                        + " vs "
                        + resultSet.getString("equipoVisitante");

                lista.add(new PartidoItem(partido, texto));
            }

            return lista.toArray(new PartidoItem[0]);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Partidos encontrarPartido(Integer id) {
        sql = "SELECT fecha, hora, id_equipo_local, id_equipo_visitante, resultado_local, resultado_visitante, arbitro1, arbitro2 FROM partidos WHERE id_partido = ?";
        Partidos partido = new Partidos();
        try {
            Conexion c = new Conexion();
            PreparedStatement preparedStatement = c.realizarConexion().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                partido = new Partidos(id, resultSet.getDate("fecha").toLocalDate(), resultSet.getTime("hora"), resultSet.getInt("id_equipo_local"), resultSet.getInt("id_equipo_visitante"), resultSet.getInt("resultado_local"), resultSet.getInt("resultado_visitante"), resultSet.getInt("arbitro1"), resultSet.getInt("arbitro2"));
            }
            return partido;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean imprimirPartidos(){
        sql = "SELECT p.id_partido, p.fecha, p.hora, el.nombre_equipo AS equipoLocal, ev.nombre_equipo AS equipoVisitante, p.resultado_local, p.resultado_visitante, a1.nombreCompleto AS arbitro1, a2.nombreCompleto AS arbitro2 FROM partidos p JOIN equipos el ON p.id_equipo_local = el.id_equipo JOIN equipos ev ON p.id_equipo_visitante = ev.id_equipo JOIN arbitro a1 ON p.arbitro1 = a1.id_arbitro JOIN arbitro a2 ON p.arbitro2 = a2.id_arbitro";

        // Carpeta documentos en la raíz del proyecto
        File directorioDocumentos = new File(System.getProperty("user.dir"), "documentos");
        if (!directorioDocumentos.exists()) {
            directorioDocumentos.mkdir();
        }

        File documento = new File(directorioDocumentos, "partidos.csv");

        try (Statement statement = c.realizarConexion().createStatement();
             ResultSet resultSet = statement.executeQuery(sql);
             BufferedWriter escribir = new BufferedWriter(new FileWriter(documento))) {

            // Encabezado CSV
            escribir.write("ID, Fecha, Hora, Equipo local, Equipo visitante, Resultado local, Resultado visitante, Arbitro 1 , Arbitro 2");
            escribir.newLine();

            // Filas
            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("p.id_partido"));
                String fecha = resultSet.getDate("p.fecha").toString();
                String hora = resultSet.getTime("p.hora").toString();
                String equipoLocal = resultSet.getString("equipoLocal");
                String equipoVisitante = resultSet.getString("equipoVisitante");
                String resultadoLocal = String.valueOf(resultSet.getInt("p.resultado_local"));
                String resultadoVisitante = String.valueOf(resultSet.getInt("p.resultado_visitante"));
                String arbitro1 = resultSet.getString("arbitro1");
                String arbitro2 = resultSet.getString("arbitro2");

                escribir.write(String.join(",", id, fecha, hora, equipoLocal, equipoVisitante, resultadoLocal, resultadoVisitante, arbitro1, arbitro2));
                escribir.newLine();
            }
            return true;
        }catch (SQLException | IOException e){
            throw new RuntimeException(e);
        }
    }

}
