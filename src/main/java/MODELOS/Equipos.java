package MODELOS;

public class Equipos {
    private int id_equipo;
    private String nombre_equipo;
    private int partidos_perdidos;
    private int partidos_ganados;
    private int trofeos_liga;

    public Equipos() {
    }

    public Equipos(String nombre_equipo, int partidos_perdidos, int partidos_ganados, int trofeos_liga) {
        this.nombre_equipo = nombre_equipo;
        this.partidos_perdidos = partidos_perdidos;
        this.partidos_ganados = partidos_ganados;
        this.trofeos_liga = trofeos_liga;
    }

    public Equipos(int id_equipo, String nombre_equipo, int partidos_perdidos, int partidos_ganados, int trofeos_liga) {
        this.id_equipo = id_equipo;
        this.nombre_equipo = nombre_equipo;
        this.partidos_perdidos = partidos_perdidos;
        this.partidos_ganados = partidos_ganados;
        this.trofeos_liga = trofeos_liga;
    }

    public int getId_equipo() {
        return id_equipo;
    }

    public void setId_equipo(int id_equipo) {
        this.id_equipo = id_equipo;
    }

    public String getNombre_equipo() {
        return nombre_equipo;
    }

    public void setNombre_equipo(String nombre_equipo) {
        this.nombre_equipo = nombre_equipo;
    }

    public int getPartidos_perdidos() {
        return partidos_perdidos;
    }

    public void setPartidos_perdidos(int partidos_perdidos) {
        this.partidos_perdidos = partidos_perdidos;
    }

    public int getPartidos_ganados() {
        return partidos_ganados;
    }

    public void setPartidos_ganados(int partidos_ganados) {
        this.partidos_ganados = partidos_ganados;
    }

    public int getTrofeos_liga() {
        return trofeos_liga;
    }

    public void setTrofeos_liga(int trofeos_liga) {
        this.trofeos_liga = trofeos_liga;
    }


    @Override
    public String toString() {
        return "Equipos{" +
                "id_equipo=" + id_equipo +
                ", nombre_equipo='" + nombre_equipo + '\'' +
                ", partidos_perdidos=" + partidos_perdidos +
                ", partidos_ganados=" + partidos_ganados +
                ", trofeos_liga=" + trofeos_liga +
                '}';
    }
}
