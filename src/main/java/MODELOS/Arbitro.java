package MODELOS;

import java.util.Date;

public class Arbitro {
    private int id_arbitro;
    private String nombreCompleto;
    private Date fecha_nacimiento;
    private int partidos_arbitrados;
    private int anos_experiencia;

    public Arbitro() {
    }

    public Arbitro(int id_arbitro, String nombreCompleto, Date fecha_nacimiento, int partidos_arbitrados, int anos_experiencia) {
        this.id_arbitro = id_arbitro;
        this.nombreCompleto = nombreCompleto;
        this.fecha_nacimiento = fecha_nacimiento;
        this.partidos_arbitrados = partidos_arbitrados;
        this.anos_experiencia = anos_experiencia;
    }

    public int getId_arbitro() {
        return id_arbitro;
    }

    public void setId_arbitro(int id_arbitro) {
        this.id_arbitro = id_arbitro;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public int getPartidos_arbitrados() {
        return partidos_arbitrados;
    }

    public void setPartidos_arbitrados(int partidos_arbitrados) {
        this.partidos_arbitrados = partidos_arbitrados;
    }

    public int getAnos_experiencia() {
        return anos_experiencia;
    }

    public void setAnos_experiencia(int anos_experiencia) {
        this.anos_experiencia = anos_experiencia;
    }

    @Override
    public String toString() {
        return "Arbitro{" +
                "id_arbitro=" + id_arbitro +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", fecha_nacimiento=" + fecha_nacimiento +
                ", partidos_arbitrados=" + partidos_arbitrados +
                ", anos_experiencia=" + anos_experiencia +
                '}';
    }
}
