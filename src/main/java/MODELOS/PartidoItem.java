package MODELOS;

public class PartidoItem {
    private Partidos partido;
    private String texto;

    public PartidoItem(Partidos partido, String texto) {
        this.partido = partido;
        this.texto = texto;
    }

    public Partidos getPartido() {
        return partido;
    }

    @Override
    public String toString() {
        return texto;
    }
}
