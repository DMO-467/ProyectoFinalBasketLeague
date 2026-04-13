package MODELOS;

public class Patrocinios {
    private int id_patrocinador;
    private int id_equipo;

    public Patrocinios() {
    }

    public Patrocinios(int id_patrocinador, int id_equipo) {
        this.id_patrocinador = id_patrocinador;
        this.id_equipo = id_equipo;
    }

    public int getId_patrocinador() {
        return id_patrocinador;
    }

    public void setId_patrocinador(int id_patrocinador) {
        this.id_patrocinador = id_patrocinador;
    }

    public int getId_equipo() {
        return id_equipo;
    }

    public void setId_equipo(int id_equipo) {
        this.id_equipo = id_equipo;
    }

    @Override
    public String toString() {
        return "Patrocinios{" +
                "id_patrocinador=" + id_patrocinador +
                ", id_equipo=" + id_equipo +
                '}';
    }
}
