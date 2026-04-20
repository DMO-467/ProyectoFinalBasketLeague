package VISTAS;
import CONTROLADORES.*;
import OTROS.Regex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class GUI {

    public void mensaje(String dato){
        final JDialog dialog = new JDialog();
        dialog.setSize(200, 100);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel(dato, SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        new javax.swing.Timer(500, e -> dialog.dispose()) {{
            setRepeats(false);
            start();
        }};
    }
    public void ventanaAnadir(){
        JFrame ventana = new JFrame("Añadir");
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(1000, 650);

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
            GUI cambio = new GUI();
            if (comprobacion.verificacionInicioSesion(password)) {
                cambio.paginaPrincipal();
                inicio.setVisible(false);
            }else {
                cambio.mensaje("Contraseña Incorrecta");
            }

        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(contrasena, BorderLayout.CENTER);
        inicio.setVisible(true);
        usuario.addActionListener(accion);
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
        fondo.setVisible(true);
        ActionListener listaArbitro = e -> {
            ControladorArbitro mostrar = new ControladorArbitro();
            tabla.setModel(mostrar.mostrarArbitro());
        };
        arbitros.addActionListener(listaArbitro);
        ActionListener listaequipos = e -> {
            ControladorEquipos mostrar = new ControladorEquipos();
            tabla.setModel(mostrar.mostrarEquipos());
        };
        equipos.addActionListener(listaequipos);
        ActionListener listaPartidos = e -> {
            ControladorPartidos mostrar = new ControladorPartidos();
            tabla.setModel(mostrar.mostrarPartidos());
        };
        partidos.addActionListener(listaPartidos);
        ActionListener listaPatrocinios = e -> {
            ControladorPatrocinios mostrar = new ControladorPatrocinios();
            tabla.setModel(mostrar.mostrarPatrocinios());
        };
        patrocinios.addActionListener(listaPatrocinios);
        ActionListener listaPatrocinador = e -> {
            ControladorPatrocinador mostrar = new ControladorPatrocinador();
            tabla.setModel(mostrar.mostrarPatrocinador());
        };
        patrocinadores.addActionListener(listaPatrocinador);
    }
}
