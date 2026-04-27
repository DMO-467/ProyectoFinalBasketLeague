package VISTAS;
import CONTROLADORES.*;
import MODELOS.Arbitro;
import OTROS.Regex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class GUI {
    int tablaPartidos = 3;
    int nTabla = tablaPartidos;
    int tablaArbitro = 1;
    int tablaEquipos = 2;
    int tablaPatrocinador = 4;
    int tablaPatrocinios = 5;

    private static int reglaDeTres(int n, int a, int b){
        return n*a/b;
    }
    private static GridBagConstraints configurarConstraints(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    public void mensaje(String dato, int tiempo){
        final JDialog dialog = new JDialog();
        int ancho = reglaDeTres(dato.length(), 200, 21);
        dialog.setSize(ancho, 100);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel(dato, SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        new javax.swing.Timer(tiempo, e -> dialog.dispose()) {{
            setRepeats(false);
            start();
        }};
    }
    public void mensaje(String dato) {
        final JDialog dialog = new JDialog();
        dialog.setTitle("Aviso");

        int ancho = reglaDeTres(dato.length(), 200, 21);
        dialog.setSize(ancho, 100);
        dialog.setLayout(new BorderLayout());

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel(dato, SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(null);

        dialog.setModal(true);

        dialog.setVisible(true);
    }

    public static void ventanaArbitro() {
        JFrame frame = new JFrame("Añadir Árbitro");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        String[] campos = {"Nombre Completo", "Fecha de nacimiento", "Partidos arbitrados", "Años experiencia"};
        int columnas = 15;
        JTextField nombre = new JTextField(columnas);
        JTextField fecha = new JTextField(columnas);
        JTextField partidos = new JTextField(columnas);
        JTextField anos = new JTextField(columnas);
        JTextField[] textos = {nombre, fecha, partidos, anos};
        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(textos[i], configurarConstraints(1, i));
        }
        ActionListener guardado = e -> {
            String nombreCompleto = nombre.getText();
            String fechaNacimiento = fecha.getText();
            String partidosArbitrados = partidos.getText();
            String anosExperiencia = anos.getText();
            Regex comprobacion = new Regex();
            GUI g = new GUI();
            if (comprobacion.comprobarTexto(nombreCompleto)) {
                if (comprobacion.comprobarFecha(fechaNacimiento)) {
                    LocalDate fechaNacimientoParseada = comprobacion.textoFecha(fechaNacimiento);
                    if (comprobacion.comprobarNumero(partidosArbitrados)) {
                        int partidosArbitradosNumero = Integer.parseInt(partidosArbitrados);
                        if (comprobacion.comprobarNumero(anosExperiencia)) {
                            int anosExperienciaNumero = Integer.parseInt(anosExperiencia);
                            Arbitro arbitro = new Arbitro(nombreCompleto, fechaNacimientoParseada, partidosArbitradosNumero, anosExperienciaNumero);
                            ControladorArbitro anadir = new ControladorArbitro();
                            if (anadir.anadirArbitro(arbitro)) {
                                g.mensaje("Fila añadida", 800);
                            }else {
                                g.mensaje("ERROR, valor no valido");
                            }
                        }else {
                            g.mensaje("ERROR, años de experiencia no validos");
                        }
                    }else {
                        g.mensaje("ERROR, partidos arbitrados no valido");
                    }
                }else {
                    g.mensaje("ERROR, fecha no valida");
                }
            }else {
                g.mensaje("ERROR, nombre no valido");
            }
        };

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, campos.length);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        frame.add(panel);
        frame.setVisible(true);
        boton.addActionListener(guardado);
    }
    public static void ventanaEquipos() {
        JFrame frame = new JFrame("Añadir Equipo");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        String[] campos = {"Nombre", "Partidos perdidos", "Partidos ganados", "Trofeos liga"};

        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(new JTextField(15), configurarConstraints(1, i));
        }

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, campos.length);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }
    public static void ventanaPartidos(String[] equipos, String[] arbitros) {
        JFrame frame = new JFrame("Añadir Partido");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        String[] camposTexto = {"Fecha", "Hora", "Resultado local", "Resultado visitante"};

        for (int i = 0; i < camposTexto.length; i++) {
            panel.add(new JLabel(camposTexto[i] + ":"), configurarConstraints(0, i));
            panel.add(new JTextField(15), configurarConstraints(1, i));
        }
        int fila = camposTexto.length;

        panel.add(new JLabel("Equipo local:"), configurarConstraints(0, fila));
        panel.add(new JComboBox<>(equipos), configurarConstraints(1, fila++));

        panel.add(new JLabel("Equipo visitante:"), configurarConstraints(0, fila));
        panel.add(new JComboBox<>(equipos), configurarConstraints(1, fila++));

        panel.add(new JLabel("Árbitro 1:"), configurarConstraints(0, fila));
        panel.add(new JComboBox<>(arbitros), configurarConstraints(1, fila++));

        panel.add(new JLabel("Árbitro 2:"), configurarConstraints(0, fila));
        panel.add(new JComboBox<>(arbitros), configurarConstraints(1, fila++));

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, fila);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }
    public static void ventanaPatrocinador() {
        JFrame frame = new JFrame("Añadir Patrocinador");
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        String[] campos = {"Nombre", "Telefono", "Email"};

        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(new JTextField(15), configurarConstraints(1, i));
        }

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, campos.length);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }
    public static void ventanaPatrocinios(String[] patrocinadores, String[] equipos) {
        JFrame frame = new JFrame("Asignar Patrocinio");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        panel.add(new JLabel("Patrocinador:"), configurarConstraints(0, 0));
        panel.add(new JComboBox<>(patrocinadores), configurarConstraints(1, 0));

        panel.add(new JLabel("Equipo:"), configurarConstraints(0, 1));
        panel.add(new JComboBox<>(equipos), configurarConstraints(1, 1));

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, 2);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }
    public void ventanaCambioPassword() {
        JFrame frame = new JFrame("Cambiar contraseña");
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());

        JLabel texto = new JLabel("Contraseña antigua:");
        javax.swing.JPasswordField contrasenaAntigua = new JPasswordField(15);
        panel.add(texto, configurarConstraints(0, 0));

        JPasswordField contrasenaNueva = new JPasswordField(15);
        panel.add(new JLabel("Contraseña nueva:"), configurarConstraints(0, 1));
        panel.add(contrasenaNueva, configurarConstraints(1, 1));
        panel.add(contrasenaAntigua, configurarConstraints(1, 0));

        ActionListener accion = e -> {
            String password = new String(contrasenaAntigua.getPassword());
            String nueva = new String(contrasenaNueva.getPassword());
            Regex comprobacion = new Regex();
            if (comprobacion.cambiarContrasena(password, nueva)) {
                mensaje("La contraseña se ha cambiado correctamente");
                frame.dispose();
                ventanaDeLogeo();
            }else {
                mensaje("Contraseña Incorrecta", 500);
            }

        };

        GridBagConstraints gbcBoton = configurarConstraints(1, 2);
        gbcBoton.anchor = GridBagConstraints.CENTER;
        JButton confirmar = new JButton("Aceptar");
        panel.add(confirmar, gbcBoton);

        frame.add(panel);
        frame.setVisible(true);
        confirmar.addActionListener(accion);
    }

    public void ventanaDeLogeo(){
        JFrame inicio = new JFrame("Inicio de Sesión");
        inicio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel contrasena = new JPanel();
        JLabel texto = new JLabel("Contraseña");
        JPasswordField usuario = new JPasswordField();
        usuario.setPreferredSize(new  Dimension(100, 25));
        contrasena.add(texto);
        contrasena.add(usuario);
        ActionListener accion = e -> {
            String password = new String(usuario.getPassword());
            Regex comprobacion = new Regex();
            if (comprobacion.verificacionInicioSesion(password)) {
                paginaPrincipal();
                inicio.setVisible(false);
            }else {
                mensaje("Contraseña Incorrecta", 500);
            }

        };
        ActionListener accion2 = e -> {
            ventanaCambioPassword();
            inicio.setVisible(false);
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        JButton cambiar = new JButton("Cambiar contraseña");
        JPanel cContrasena = new JPanel();
        cContrasena.add(cambiar);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(contrasena, BorderLayout.CENTER);
        inicio.add(cContrasena, BorderLayout.SOUTH);
        inicio.setVisible(true);
        usuario.addActionListener(accion);
        cambiar.addActionListener(accion2);
    }
    public void paginaPrincipal(){
        JFrame fondo = new JFrame("Basket League");
        fondo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fondo.setSize(1000, 650);
        fondo.setLayout(new BorderLayout());
        fondo.setLocationRelativeTo(null);
        JButton arbitros = new JButton("Arbitros");
        JButton equipos = new JButton("Equipos");
        JButton partidos = new JButton("Partidos");
        JButton patrocinios = new JButton("Patrocinios");
        JButton patrocinadores = new JButton("Patrocinadores");
        JPanel tablas = new JPanel();
        tablas.add(arbitros);
        tablas.add(equipos);
        tablas.add(partidos);
        tablas.add(patrocinios);
        tablas.add(patrocinadores);
        fondo.add(tablas, BorderLayout.NORTH);
        JTable tabla = new JTable();
        JScrollPane subeYBaja = new JScrollPane(tabla);
        fondo.add(subeYBaja, BorderLayout.CENTER);
        JButton anadir = new JButton("Añadir");
        JButton eliminar = new JButton("Eliminar");
        JButton modificar = new JButton("Modificar");
        JPanel opciones = new JPanel();
        opciones.add(anadir);
        opciones.add(eliminar);
        opciones.add(modificar);
        fondo.add(opciones, BorderLayout.SOUTH);
        ControladorPartidos tablaPrincipal = new ControladorPartidos();
        tabla.setModel(tablaPrincipal.mostrarPartidos());
        fondo.setVisible(true);
        ActionListener listaArbitro = e -> {
            nTabla = tablaArbitro;
            ControladorArbitro mostrar = new ControladorArbitro();
            tabla.setModel(mostrar.mostrarArbitro());
        };
        arbitros.addActionListener(listaArbitro);
        ActionListener listaequipos = e -> {
            nTabla = tablaEquipos;
            ControladorEquipos mostrar = new ControladorEquipos();
            tabla.setModel(mostrar.mostrarEquipos());
        };
        equipos.addActionListener(listaequipos);
        ActionListener listaPartidos = e -> {
            nTabla = tablaPartidos;
            ControladorPartidos mostrar = new ControladorPartidos();
            tabla.setModel(mostrar.mostrarPartidos());
        };
        partidos.addActionListener(listaPartidos);
        ActionListener listaPatrocinios = e -> {
            nTabla = tablaPatrocinador;
            ControladorPatrocinios mostrar = new ControladorPatrocinios();
            tabla.setModel(mostrar.mostrarPatrocinios());
        };
        patrocinios.addActionListener(listaPatrocinios);
        ActionListener listaPatrocinador = e -> {
            nTabla = tablaPatrocinios;
            ControladorPatrocinador mostrar = new ControladorPatrocinador();
            tabla.setModel(mostrar.mostrarPatrocinador());
        };
        patrocinadores.addActionListener(listaPatrocinador);

        ActionListener anade = e -> {
            ControladorEquipos eq = new ControladorEquipos();
            if (nTabla == tablaArbitro) {
                ventanaArbitro();
            }
            if (nTabla == tablaEquipos) {
                ventanaEquipos();
            }
            if (nTabla == tablaPartidos) {
                ControladorArbitro ar = new ControladorArbitro();
                ventanaPartidos(eq.mostrarNombreEquipos(), ar.mostrarNombreArbitro());
            }
            if (nTabla == tablaPatrocinador) {
                ControladorPatrocinador pa = new ControladorPatrocinador();
                ventanaPatrocinios(pa.mostrarNombrePatrocinador(), eq.mostrarNombreEquipos());
            }
            if (nTabla == tablaPatrocinios) {
                ventanaPatrocinador();
            }

        };
        anadir.addActionListener(anade);
    }
}
