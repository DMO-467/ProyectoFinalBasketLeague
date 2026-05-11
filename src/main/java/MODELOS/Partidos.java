package MODELOS;

import java.sql.Time;
import java.time.LocalDate;

public class Partidos {
    private int id_partido;
    private LocalDate fecha;
    private Time hora;
    private int id_equipo_local;
    private int id_equipo_visitante;
    private int resultado_local;
    private int resultado_visitante;
    private int arbitro1;
    private int arbitro2;

    public Partidos() {
    }

    public Partidos(LocalDate fecha, Time hora, int id_equipo_local, int id_equipo_visitante, int arbitro1, int arbitro2) {
        this.fecha = fecha;
        this.hora = hora;
        this.id_equipo_local = id_equipo_local;
        this.id_equipo_visitante = id_equipo_visitante;
        this.arbitro1 = arbitro1;
        this.arbitro2 = arbitro2;
    }

    public Partidos(LocalDate fecha, Time hora, int id_equipo_local, int id_equipo_visitante, int resultado_local, int resultado_visitante, int arbitro1, int arbitro2) {
        this.fecha = fecha;
        this.hora = hora;
        this.id_equipo_local = id_equipo_local;
        this.id_equipo_visitante = id_equipo_visitante;
        this.resultado_local = resultado_local;
        this.resultado_visitante = resultado_visitante;
        this.arbitro1 = arbitro1;
        this.arbitro2 = arbitro2;
    }

    public Partidos(int id_partido, LocalDate fecha, Time hora, int id_equipo_local, int id_equipo_visitante, int resultado_local, int resultado_visitante, int arbitro1, int arbitro2) {
        this.id_partido = id_partido;
        this.fecha = fecha;
        this.hora = hora;
        this.id_equipo_local = id_equipo_local;
        this.id_equipo_visitante = id_equipo_visitante;
        this.resultado_local = resultado_local;
        this.resultado_visitante = resultado_visitante;
        this.arbitro1 = arbitro1;
        this.arbitro2 = arbitro2;
    }

    public int getId_partido() {
        return id_partido;
    }

    public void setId_partido(int id_partido) {
        this.id_partido = id_partido;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public int getId_equipo_local() {
        return id_equipo_local;
    }

    public void setId_equipo_local(int id_equipo_local) {
        this.id_equipo_local = id_equipo_local;
    }

    public int getId_equipo_visitante() {
        return id_equipo_visitante;
    }

    public void setId_equipo_visitante(int id_equipo_visitante) {
        this.id_equipo_visitante = id_equipo_visitante;
    }

    public int getResultado_local() {
        return resultado_local;
    }

    public void setResultado_local(int resultado_local) {
        this.resultado_local = resultado_local;
    }

    public int getResultado_visitante() {
        return resultado_visitante;
    }

    public void setResultado_visitante(int resultado_visitante) {
        this.resultado_visitante = resultado_visitante;
    }

    public int getArbitro1() {
        return arbitro1;
    }

    public void setArbitro1(int arbitro1) {
        this.arbitro1 = arbitro1;
    }

    public int getArbitro2() {
        return arbitro2;
    }

    public void setArbitro2(int arbitro2) {
        this.arbitro2 = arbitro2;
    }

    @Override
    public String toString() {
        return "Partidos{" +
                "id_partido=" + id_partido +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", id_equipo_local=" + id_equipo_local +
                ", id_equipo_visitante=" + id_equipo_visitante +
                ", resultado_local=" + resultado_local +
                ", resultado_visitante=" + resultado_visitante +
                ", arbitro1=" + arbitro1 +
                ", arbitro2=" + arbitro2 +
                '}';
    }
}
