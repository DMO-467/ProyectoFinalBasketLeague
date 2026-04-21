package VISTAS;
import CONTROLADORES.*;
import OTROS.Regex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI {
    int tablaPartidos = 3;
    int nTabla = tablaPartidos;
    int tablaArbitro = 1;
    int tablaEquipos = 2;
    int tablaPatrocinador = 4;
    int tablaPatrocinios = 5;
    String[] camposArbitro = {"Nombre Completo", "Fecha de nacimiento", "Partidos arbitrados", "Años experiencia"};
    String[] camposEquipos = {"Nombre", "Partidos perdidos", "Partidos ganados", "Trofeos liga"};
    String [] camposPartidos = {"Fecha", "Hora", "Equipo local", "Equipo visitante", "Resultado local", "Resultado visitante", "Arbitro 1" , "Arbitro 2"};
    String[] camposPatrocinador = {"Nombre", "Telefono", "Email"};
    String[] camposPatrocinios = {"Patrocinador", "Equipo"};


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
    public void ventanaAnadir(String[] textos){
        JFrame ventana = new JFrame("Añadir");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setSize(400, 200);
        ventana.setLocationRelativeTo(null);

        CardLayout cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);

        // Array para guardar los campos
        JTextField[] campos = new JTextField[textos.length];

        for (int i = 0; i < textos.length; i++) {
            JPanel tarjeta = new JPanel(new BorderLayout(10, 10));

            JLabel label = new JLabel(textos[i], SwingConstants.CENTER);
            JTextField campo = new JTextField();

            campos[i] = campo;

            tarjeta.add(label, BorderLayout.NORTH);
            tarjeta.add(campo, BorderLayout.CENTER);

            panel.add(tarjeta, "card" + i);
        }

        JButton boton = new JButton("Siguiente");

        boton.addActionListener(new ActionListener() {
            int indice = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (indice < textos.length - 1) {
                    indice++;
                    cardLayout.next(panel);

                    // Si estamos en la última tarjeta, cambiamos el texto del botón
                    if (indice == textos.length - 1) {
                        boton.setText("Guardar");
                    }
                } else {
                    // Recoger datos
                    System.out.println("Datos introducidos:");
                    for (int i = 0; i < campos.length; i++) {
                        System.out.println(textos[i] + ": " + campos[i].getText());
                    }

                    ventana.dispose(); // cerrar solo esta ventana
                }
            }
        });

        ventana.add(panel, BorderLayout.CENTER);
        ventana.add(boton, BorderLayout.SOUTH);
        ventana.setVisible(true);
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
            if (nTabla == tablaArbitro) {
                ventanaAnadir(camposArbitro);
            }
            if (nTabla == tablaEquipos) {
                ventanaAnadir(camposEquipos);
            }
            if (nTabla == tablaPartidos) {
                ventanaAnadir(camposPartidos);
            }
            if (nTabla == tablaPatrocinador) {
                ventanaAnadir(camposPatrocinador);
            }
            if (nTabla == tablaPatrocinios) {
                ventanaAnadir(camposPatrocinios);
            }

        };
        anadir.addActionListener(anade);
    }
}
