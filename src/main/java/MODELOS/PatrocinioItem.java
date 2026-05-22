package MODELOS;

public class PatrocinioItem {
    private Patrocinios patrocinio;
    private String texto;

    public PatrocinioItem(Patrocinios patrocinio, String texto) {
        this.patrocinio = patrocinio;
        this.texto = texto;
    }

    public Patrocinios getPatrocinio() {
        return patrocinio;
    }

    @Override
    public String toString() {
        return texto;
    }
}
