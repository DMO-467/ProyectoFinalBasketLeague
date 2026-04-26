package MODELOS;

public class Patrocinador {
    private int id_patrocinador;
    private String nombre_patrocinador;
    private int telefono;
    private String email;

    public Patrocinador() {
    }

    public Patrocinador(String nombre_patrocinador, int telefono, String email) {
        this.nombre_patrocinador = nombre_patrocinador;
        this.telefono = telefono;
        this.email = email;
    }

    public Patrocinador(int id_patrocinador, String nombre_patrocinador, int telefono, String email) {
        this.id_patrocinador = id_patrocinador;
        this.nombre_patrocinador = nombre_patrocinador;
        this.telefono = telefono;
        this.email = email;
    }

    public int getId_patrocinador() {
        return id_patrocinador;
    }

    public void setId_patrocinador(int id_patrocinador) {
        this.id_patrocinador = id_patrocinador;
    }

    public String getNombre_patrocinador() {
        return nombre_patrocinador;
    }

    public void setNombre_patrocinador(String nombre_patrocinador) {
        this.nombre_patrocinador = nombre_patrocinador;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Patrocinador{" +
                "id_patrocinador=" + id_patrocinador +
                ", nombre_patrocinador='" + nombre_patrocinador + '\'' +
                ", telefono=" + telefono +
                ", email='" + email + '\'' +
                '}';
    }
}
