package VISTAS;
import CONTROLADORES.*;
import MODELOS.*;
import OTROS.Regex;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class GUI {
    int tablaPartidos = 3;
    int nTabla = tablaPartidos;
    int tablaArbitro = 1;
    int tablaEquipos = 2;
    int tablaPatrocinador = 5;
    int tablaPatrocinios = 4;
    int mensajeDeAcierto = 800;

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

        JDateChooser fecha = new JDateChooser();
        fecha.setDateFormatString("dd/MM/yyyy");

        JTextField partidos = new JTextField(columnas);
        JTextField anos = new JTextField(columnas);

        JComponent[] textos = {nombre, fecha, partidos, anos};

        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(textos[i], configurarConstraints(1, i));
        }

        ActionListener guardado = e -> {
            String nombreCompleto = nombre.getText();
            Date fechaSeleccionada = fecha.getDate();
            String partidosArbitrados = partidos.getText();
            String anosExperiencia = anos.getText();

            Regex comprobacion = new Regex();
            GUI g = new GUI();
            nombreCompleto = comprobacion.mayusculasNombres(nombreCompleto);
            if (comprobacion.comprobarTexto(nombreCompleto)) {
                if (fechaSeleccionada != null) {

                    LocalDate fechaNacimientoParseada = fechaSeleccionada.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    if (comprobacion.comprobarFecha(fechaNacimientoParseada)) {
                        if (comprobacion.comprobarNumero(partidosArbitrados, 5000)) {
                            int partidosArbitradosNumero = Integer.parseInt(partidosArbitrados);
                            if (comprobacion.comprobarNumero(anosExperiencia, 60)) {
                                int anosExperienciaNumero = Integer.parseInt(anosExperiencia);
                                Arbitro arbitro = new Arbitro(nombreCompleto, fechaNacimientoParseada,
                                        partidosArbitradosNumero, anosExperienciaNumero);

                                ControladorArbitro anadir = new ControladorArbitro();

                                if (anadir.anadirArbitro(arbitro)) {
                                    g.mensaje("Fila añadida", g.mensajeDeAcierto);
                                } else {
                                    g.mensaje("ERROR, no se ha podido añadir el arbitro");
                                }
                            } else {
                                g.mensaje("ERROR, años de experiencia no validos. Debe de ser un numero");
                            }

                        } else {
                            g.mensaje("ERROR, partidos arbitrados no valido. Debe de ser un numero");
                        }
                    }else {
                        g.mensaje("ERROR, fecha de nacimiento no valida. Debe de tener minimo 16 años");
                    }
                } else {
                    g.mensaje("ERROR, selecciona una fecha");
                }

            } else {
                g.mensaje("ERROR, nombre no valido. Solo se valen letras");
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

        JTextField nombre = new JTextField(15);
        JTextField partidosPerdidos = new JTextField(15);
        JTextField partidosGanados = new JTextField(15);
        JTextField trofeosLiga = new JTextField(15);
        JTextField[] valores = {nombre, partidosPerdidos, partidosGanados, trofeosLiga};

        String[] campos = {"Nombre", "Partidos perdidos", "Partidos ganados", "Trofeos liga"};

        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(valores[i], configurarConstraints(1, i));
        }

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, campos.length);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        ActionListener accion = e -> {
          String nombreEquipo = nombre.getText();
          String partidosPerdidosValor = partidosPerdidos.getText();
          String partidosGanadosValor = partidosGanados.getText();
          String trofeosLigaValor = trofeosLiga.getText();
          ControladorEquipos comprobaciones = new ControladorEquipos();
          Regex filtros = new Regex();
          GUI g = new GUI();
          nombreEquipo = filtros.mayusculasNombres(nombreEquipo);
          if (filtros.comprobarTexto(nombreEquipo)) {
              if (filtros.comprobarNumero(partidosPerdidosValor, 2000)) {
                  int partidosPerdidosFiltrado = Integer.parseInt(partidosPerdidosValor);
                  if (filtros.comprobarNumero(partidosGanadosValor, 2000)) {
                      int partidosGanadosFiltrado = Integer.parseInt(partidosGanadosValor);
                      if (filtros.comprobarNumero(trofeosLigaValor, 200)) {
                          int trofeosLigaFiltrado = Integer.parseInt(trofeosLigaValor);
                          Equipos equipo = new Equipos(nombreEquipo, partidosPerdidosFiltrado, partidosGanadosFiltrado, trofeosLigaFiltrado);
                          if (comprobaciones.existeNombre(nombreEquipo)) {
                              g.mensaje("Este equipo ya existe");
                          }else {
                              if (comprobaciones.anadirEquipo(equipo)) {
                                  g.mensaje("Se ha añadido correctamente", g.mensajeDeAcierto);
                              }else {
                                  g.mensaje("No se ha podido añadir el equipo");
                              }
                          }
                      }else {
                          g.mensaje("ERROR trofeos liga, solo se valen numeros");
                      }
                  }else {
                    g.mensaje("ERROR en partidos ganados, solo se valen numeros");
                  }
              }else {
                  g.mensaje("ERROR en partidos perdidos, solo se valen numeros");
              }
          }else {
             g.mensaje("ERROR nombre no valido, Solo se valen letras");
          }
        };

        frame.add(panel);
        frame.setVisible(true);
        boton.addActionListener(accion);
    }
    public static void ventanaPartidos(String[] equipos, String[] arbitros) {
        JFrame frame = new JFrame("Añadir Partido");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        String[] camposTexto = {"Fecha", "Hora", "Resultado local", "Resultado visitante"};

        JDateChooser fecha = new JDateChooser();
        fecha.setDateFormatString("dd/MM/yyyy");

        JTextField hora = new JTextField(15);
        JTextField resultadoLocal = new JTextField(15);
        JTextField resultadoVisitante = new JTextField(15);

        JComponent[] valores = {fecha, hora, resultadoLocal, resultadoVisitante};

        for (int i = 0; i < camposTexto.length; i++) {
            panel.add(new JLabel(camposTexto[i] + ":"), configurarConstraints(0, i));
            panel.add(valores[i], configurarConstraints(1, i));
        }

        int fila = camposTexto.length;

        JComboBox<String> cEquipoLocal = new JComboBox<>(equipos);
        panel.add(new JLabel("Equipo local:"), configurarConstraints(0, fila));
        panel.add(cEquipoLocal, configurarConstraints(1, fila++));

        JComboBox<String> cEquipoVisitante = new JComboBox<>(equipos);
        panel.add(new JLabel("Equipo visitante:"), configurarConstraints(0, fila));
        panel.add(cEquipoVisitante, configurarConstraints(1, fila++));

        JComboBox<String> cArbitro1 = new JComboBox<>(arbitros);
        panel.add(new JLabel("Árbitro 1:"), configurarConstraints(0, fila));
        panel.add(cArbitro1, configurarConstraints(1, fila++));

        JComboBox<String> cArbitro2 = new JComboBox<>(arbitros);
        panel.add(new JLabel("Árbitro 2:"), configurarConstraints(0, fila));
        panel.add(cArbitro2, configurarConstraints(1, fila++));

        ActionListener accion = e -> {
            Regex filtros = new Regex();
            ControladorPartidos comprobaciones = new ControladorPartidos();
            GUI interfaz = new GUI();
            ControladorEquipos controladorEquipos = new ControladorEquipos();
            ControladorArbitro controladorArbitro = new ControladorArbitro();

            Date fechaSeleccionada = fecha.getDate();
            String horaPartido = hora.getText();
            String resultadoLocalPartido = resultadoLocal.getText();
            String resultadoVisitantePartido = resultadoVisitante.getText();

            String equipoLocalPartido = (String) cEquipoLocal.getSelectedItem();
            String equipoVisitantePartido = (String) cEquipoVisitante.getSelectedItem();
            String arbitro1Partido = (String) cArbitro1.getSelectedItem();
            String arbitro2Partido = (String) cArbitro2.getSelectedItem();

            if (fechaSeleccionada != null) {

                LocalDate fechaPartidoParseada = fechaSeleccionada.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                if (filtros.comprobarHora(horaPartido)) {

                    horaPartido = horaPartido + ":00";
                    Time horaPartidoParseado = Time.valueOf(horaPartido);

                    if (filtros.comprobarNumero(resultadoLocalPartido, 30)) {
                        int resultadoLocalPartidoParseado = Integer.parseInt(resultadoLocalPartido);

                        if (filtros.comprobarNumero(resultadoVisitantePartido, 30)) {
                            int resultadoVisitantePartidoParseado = Integer.parseInt(resultadoVisitantePartido);

                            if (!equipoLocalPartido.equals(equipoVisitantePartido)) {

                                if (!arbitro1Partido.equals(arbitro2Partido)) {

                                    Partidos partido = new Partidos(
                                            fechaPartidoParseada,
                                            horaPartidoParseado,
                                            controladorEquipos.cualId(equipoLocalPartido),
                                            controladorEquipos.cualId(equipoVisitantePartido),
                                            resultadoLocalPartidoParseado,
                                            resultadoVisitantePartidoParseado,
                                            controladorArbitro.cualId(arbitro1Partido),
                                            controladorArbitro.cualId(arbitro2Partido)
                                    );

                                    if (comprobaciones.anadirPartido(partido)) {
                                        interfaz.mensaje("Partido añadido correctamente", interfaz.mensajeDeAcierto);
                                    } else {
                                        interfaz.mensaje("ERROR, no se ha podido añadir el partido");
                                    }

                                } else {
                                    interfaz.mensaje("ERROR, los arbitros tienen que ser distintos");
                                }

                            } else {
                                interfaz.mensaje("ERROR, los equipos tienen que ser distintos");
                            }

                        } else {
                            interfaz.mensaje("ERROR, resultado visitante no valido, tiene que ser un numero");
                        }

                    } else {
                        interfaz.mensaje("ERROR, resultado local no valido, tiene que ser un numero");
                    }

                } else {
                    interfaz.mensaje("ERROR, hora no valida, ejemplo: 16:12");
                }

            } else {
                interfaz.mensaje("ERROR, selecciona una fecha");
            }
        };

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, fila);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        frame.add(panel);
        frame.setVisible(true);
        boton.addActionListener(accion);
    }
    public static void ventanaPatrocinador() {
        JFrame frame = new JFrame("Añadir Patrocinador");
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        JTextField nombre = new JTextField(15);
        JTextField telefono = new JTextField(15);
        JTextField email = new JTextField(15);
        JTextField[] jTextFields = {nombre, telefono, email};
        String[] campos = {"Nombre", "Telefono", "Email"};

        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(jTextFields[i], configurarConstraints(1, i));
        }

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, campos.length);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        ActionListener accion = e -> {
            Regex filtros = new Regex();
            ControladorPatrocinador anadir = new ControladorPatrocinador();
            GUI g = new GUI();
            String nombrePatrocinador = nombre.getText();
            String telefonoPatrocinador = telefono.getText();
            String emailPatrocinador = email.getText();
            nombrePatrocinador = filtros.mayusculasNombres(nombrePatrocinador);
            if (filtros.comprobarTexto(nombrePatrocinador)) {
                if (filtros.comprobarTelefono(telefonoPatrocinador)) {
                    int numeroTelefonico = Integer.parseInt(telefonoPatrocinador);
                    if (filtros.comprobarEmail(emailPatrocinador)) {
                        Patrocinador p = new Patrocinador(nombrePatrocinador, numeroTelefonico, emailPatrocinador);
                        if (anadir.ExistePatrocinador(p)) {
                            g.mensaje("Este patrocinador ya existe");
                        }else {
                            if (anadir.anadirPatrocinador(p)) {
                                g.mensaje("Fila añadida correctamente", g.mensajeDeAcierto);
                            }else {
                                g.mensaje("No se ha podido añadir al patrocinador");
                            }
                        }
                    }else {
                        g.mensaje("ERROR, email no valido (Tiene que tener: texto@texto.extensionMax(3))");
                    }
                }else {
                    g.mensaje("ERROR, telefono no valido. Tiene que: Empezar por 6, 7 o 9 y tener 9 o 12 digitos");
                }
            }else {
                g.mensaje("ERROR, nombre no valido (Solo se valen letras)");
            }
        };

        frame.add(panel);
        frame.setVisible(true);
        boton.addActionListener(accion);
    }
    public static void ventanaPatrocinios(String[] patrocinadores, String[] equipos) {
        JFrame frame = new JFrame("Asignar Patrocinio");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        JComboBox<String> cPatrocinadores = new JComboBox<>(patrocinadores);
        panel.add(new JLabel("Patrocinador:"), configurarConstraints(0, 0));
        panel.add(cPatrocinadores, configurarConstraints(1, 0));

        JComboBox<String> cEquipos = new JComboBox<>(equipos);
        panel.add(new JLabel("Equipo:"), configurarConstraints(0, 1));
        panel.add(cEquipos, configurarConstraints(1, 1));

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, 2);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        String[] seleccionados = new String[2];
        int[] ides = new int[2];
        ActionListener accion = e -> {
            ControladorPatrocinios anadir = new ControladorPatrocinios();
            seleccionados[0] = (String) cPatrocinadores.getSelectedItem();
            seleccionados[1] = (String) cEquipos.getSelectedItem();
            ides[0] = anadir.localizarIdPatrocinador(seleccionados[0]);
            ides[1] = anadir.localizarIdEquipo(seleccionados[1]);
            Patrocinios patrocinios = new Patrocinios(ides[0], ides[1]);
            GUI g = new GUI();
            if (anadir.existePatrocinio(patrocinios)) {
                g.mensaje("Este patrocinio ya existe");
            }else {
                if (anadir.anadirPatrocinio(patrocinios)) {
                    g.mensaje("Fila añadida correctamente", g.mensajeDeAcierto);
                }else {
                    g.mensaje("No se ha podido añadir la fila");
                }
            }
        };

        frame.add(panel);
        frame.setVisible(true);
        boton.addActionListener(accion);
    }

    public void ventanaConfirmarEliminacionArbitro(String arbitro){
        ControladorArbitro controladorArbitro = new ControladorArbitro();
        JFrame inicio = new JFrame("Eliminar");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(1000, 650);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JTable tabla = new JTable();
        JScrollPane subeYBaja = new JScrollPane(tabla);
        tabla.setModel(controladorArbitro.mostrarFilasAfectadasPorArbitro(controladorArbitro.cualId(arbitro)));
        ActionListener accion = e -> {
            if (controladorArbitro.eliminarArbitro(controladorArbitro.cualId(arbitro))) {
                mensaje("Arbitro eliminado correctamente", mensajeDeAcierto);
                inicio.setVisible(false);
            }else {
                mensaje("No se ha podido eliminar al arbitro");
            }
        };
        ActionListener accion2 = e -> {
            inicio.dispose();
        };
        JPanel mensaje = new JPanel();
        mensaje.setPreferredSize(new Dimension(100, 80));
        JLabel texto = new JLabel("Estas seguro de que quieres eliminar a " + arbitro + "? se eliminaran también las siguientes filas relacionadas");
        mensaje.add(texto);
        JButton confirmar = new JButton("Confirmar");
        JButton cancelar = new JButton("Cancelar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        botones.add(cancelar);
        inicio.add(mensaje, BorderLayout.NORTH);
        inicio.add(subeYBaja, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
        confirmar.addActionListener(accion);
        cancelar.addActionListener(accion2);
    }

    public void ventanaConfirmarEliminacionEquipo(String equipo){
        ControladorEquipos controladorEquipos = new ControladorEquipos();
        JFrame inicio = new JFrame("Eliminar");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(1000, 650);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JTable tabla = new JTable();
        JScrollPane subeYBaja = new JScrollPane(tabla);
        tabla.setModel(controladorEquipos.mostrarFilasAfectadasPorEquipoEnPartido(controladorEquipos.cualId(equipo)));
        ActionListener accion = e -> {
            if (controladorEquipos.eliminarEquipo(controladorEquipos.cualId(equipo))) {
                mensaje("Equipo eliminado correctamente", mensajeDeAcierto);
                inicio.setVisible(false);
            }else {
                mensaje("No se ha podido eliminar al equipo");
            }
        };
        ActionListener accion2 = e -> {
            inicio.dispose();
        };
        ActionListener accion3 = e -> {
            tabla.setModel(controladorEquipos.mostrarFilasAfectadasPorEquipoEnPatrocinios(controladorEquipos.cualId(equipo)));
        };
        ActionListener accion4 = e -> {
            tabla.setModel(controladorEquipos.mostrarFilasAfectadasPorEquipoEnPartido(controladorEquipos.cualId(equipo)));
        };
        JPanel mensaje = new JPanel();
        JLabel texto = new JLabel("Seguro que quieres eliminar al " + equipo + "? Si lo haces eliminaras las siguientes filas relacionadas");
        mensaje.add(texto);
        JButton confirmar = new JButton("Confirmar");
        JButton cancelar = new JButton("Cancelar");
        JButton tablaPartido = new JButton("Partidos");
        JButton tablaPatrocinios = new JButton("Patrocinios");
        JPanel cContrasena = new JPanel();
        cContrasena.add(tablaPartido);
        cContrasena.add(tablaPatrocinios);
        cContrasena.add(confirmar);
        cContrasena.add(cancelar);
        inicio.add(mensaje, BorderLayout.NORTH);
        inicio.add(subeYBaja, BorderLayout.CENTER);
        inicio.add(cContrasena, BorderLayout.SOUTH);
        inicio.setVisible(true);
        confirmar.addActionListener(accion);
        cancelar.addActionListener(accion2);
        tablaPartido.addActionListener(accion4);
        tablaPatrocinios.addActionListener(accion3);
    };

    public void ventanaConfirmarEliminacionPartido(Integer id){
        JFrame inicio = new JFrame("Eliminar");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(700, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel mensaje = new JPanel();
        JLabel texto = new JLabel("¿Estas seguro de que quieres eliminar este partido? id: " + id);
        mensaje.add(texto);
        ActionListener accion = e -> {
            ControladorPartidos controladorPartidos = new ControladorPartidos();
            if (controladorPartidos.eliminarPartido(id)) {
                mensaje("Partido eliminado correctamente", mensajeDeAcierto);
                inicio.setVisible(false);
            }else {
                mensaje("No se ha podido eliminar al Partido");
            }
        };
        ActionListener accion2 = e -> {
            inicio.dispose();
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        JButton confirmar = new JButton("Confirmar");
        JButton cancelar = new JButton("Cancelar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        botones.add(cancelar);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(mensaje, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
        confirmar.addActionListener(accion);
        cancelar.addActionListener(accion2);
    };

    public void ventanaConfirmarEliminacionPatrocinio(String patrocinador, String equipo){
        JFrame inicio = new JFrame("Eliminar");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(700, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel mensaje = new JPanel();
        JLabel texto = new JLabel("¿Estas seguro de que quieres eliminar este patrocinio: " + patrocinador + " " + equipo + "?");
        mensaje.add(texto);
        ActionListener accion = e -> {
            ControladorPatrocinios controladorPatrocinios = new ControladorPatrocinios();
            Patrocinios patrocinio = new Patrocinios(controladorPatrocinios.localizarIdPatrocinador(patrocinador), controladorPatrocinios.localizarIdEquipo(equipo));
            if (controladorPatrocinios.existePatrocinio(patrocinio)) {
                if (controladorPatrocinios.eliminarPatrocinio(patrocinio)) {
                    mensaje("Patrocinio eliminado correctamente", mensajeDeAcierto);
                    inicio.setVisible(false);
                }else {
                    mensaje("No se ha podido eliminar el patrocinio");
                }
            }else {
                mensaje("ERROR, no existe este patrocinio");
            }

        };
        ActionListener accion2 = e -> {
            inicio.dispose();
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        JButton confirmar = new JButton("Confirmar");
        JButton cancelar = new JButton("Cancelar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        botones.add(cancelar);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(mensaje, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
        confirmar.addActionListener(accion);
        cancelar.addActionListener(accion2);
    };

    public void ventanaConfirmarEliminacionPatrocinador(String patrocinadorNombre){
        ControladorPatrocinador controladorPatrocinador = new ControladorPatrocinador();
        JFrame inicio = new JFrame("Eliminar");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(700, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel mensaje = new JPanel();
        JLabel texto = new JLabel("¿Estas seguro de que quieres eliminar " + patrocinadorNombre + "? Se eliminaran las siguientes filas relacionadas");
        mensaje.add(texto);
        JTable tabla = new JTable();
        JScrollPane subeYBaja = new JScrollPane(tabla);
        tabla.setModel(controladorPatrocinador.mostrarFilasAfectadasPorPatrocinadorEnPatrocinios(controladorPatrocinador.cualId(patrocinadorNombre)));
        ActionListener accion = e -> {
            if (controladorPatrocinador.eliminarPatrocinador(controladorPatrocinador.cualId(patrocinadorNombre))) {
                mensaje("Patrocinador eliminado correctamente", mensajeDeAcierto);
                inicio.setVisible(false);
            }else {
                mensaje("No se ha podido eliminar el patrocinador");
            }

        };
        ActionListener accion2 = e -> {
            inicio.dispose();
        };
        JButton confirmar = new JButton("Confirmar");
        JButton cancelar = new JButton("Cancelar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        botones.add(cancelar);
        inicio.add(mensaje, BorderLayout.NORTH);
        inicio.add(subeYBaja, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
        confirmar.addActionListener(accion);
        cancelar.addActionListener(accion2);
    };





    public void ventanaEliminarArbitro(String[] arbitrosNombre){
        JFrame inicio = new JFrame("Eliminar arbitro");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel contrasena = new JPanel();
        JLabel texto = new JLabel("Arbitro:");
        JComboBox<String> arbitros = new JComboBox<>(arbitrosNombre);
        arbitros.setPreferredSize(new  Dimension(100, 25));
        contrasena.add(texto);
        contrasena.add(arbitros);
        ActionListener accion = e -> {
            String arbitro = (String) arbitros.getSelectedItem();
            ventanaConfirmarEliminacionArbitro(arbitro);
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(contrasena, BorderLayout.CENTER);
        inicio.setVisible(true);
        arbitros.addActionListener(accion);
    }
    public void ventanaEliminarEquipo(String[] equiposNombre){
        JFrame inicio = new JFrame("Eliminar equipo");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaEquipos = new JPanel();
        JLabel texto = new JLabel("equipo:");
        JComboBox<String> equipos = new JComboBox<>(equiposNombre);
        equipos.setPreferredSize(new  Dimension(100, 25));
        listaEquipos.add(texto);
        listaEquipos.add(equipos);
        ActionListener accion = e -> {
            String equipo = (String) equipos.getSelectedItem();
            ventanaConfirmarEliminacionEquipo(equipo);
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaEquipos, BorderLayout.CENTER);
        inicio.setVisible(true);
        equipos.addActionListener(accion);
    }
    public void ventanaEliminarPartido(Integer[] idesPartidos){
        JFrame inicio = new JFrame("Eliminar partido");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPartidos = new JPanel();
        JLabel texto = new JLabel("partido:");
        JComboBox<Integer> partidos = new JComboBox<>(idesPartidos);
        partidos.setPreferredSize(new  Dimension(100, 25));
        listaPartidos.add(texto);
        listaPartidos.add(partidos);
        ActionListener accion = e -> {
            Integer partido = (Integer) partidos.getSelectedItem();
            ventanaConfirmarEliminacionPartido(partido);
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPartidos, BorderLayout.CENTER);
        inicio.setVisible(true);
        partidos.addActionListener(accion);
    }
    public void ventanaEliminarPatrocinio(String[] patrocinadores, String[] equipos){
        JFrame inicio = new JFrame("Eliminar patrocinio");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(500, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPatrocinios = new JPanel();
        JLabel texto = new JLabel("patrocinador:");
        JComboBox<String> cPatrocinadores = new JComboBox<>(patrocinadores);
        JLabel texto2 = new JLabel("Equipo:");
        JComboBox<String> cEquipos = new JComboBox<>(equipos);
        cPatrocinadores.setPreferredSize(new  Dimension(100, 25));
        cEquipos.setPreferredSize(new Dimension(100, 25));
        JButton confirmar = new JButton("Confirmar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        listaPatrocinios.add(texto);
        listaPatrocinios.add(cPatrocinadores);
        listaPatrocinios.add(texto2);
        listaPatrocinios.add(cEquipos);
        ActionListener accion = e -> {
            String patrocinador = (String) cPatrocinadores.getSelectedItem();
            String equipo = (String) cEquipos.getSelectedItem();
            ventanaConfirmarEliminacionPatrocinio(patrocinador, equipo);
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPatrocinios, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
        confirmar.addActionListener(accion);
    }
    public void ventanaEliminarPatrocinador(String[] patrocinadoresNombre){
        JFrame inicio = new JFrame("Eliminar patrocinador");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(500, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPatrocinadores = new JPanel();
        JLabel texto = new JLabel("patrocinador:");
        JComboBox<String> cPatrocinadores = new JComboBox<>(patrocinadoresNombre);
        cPatrocinadores.setPreferredSize(new  Dimension(100, 25));
        JButton confirmar = new JButton("Confirmar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        listaPatrocinadores.add(texto);
        listaPatrocinadores.add(cPatrocinadores);
        ActionListener accion = e -> {
            String patrocinador = (String) cPatrocinadores.getSelectedItem();
            ventanaConfirmarEliminacionPatrocinador(patrocinador);
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPatrocinadores, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
        confirmar.addActionListener(accion);
    }

    public void ventanaConfirmarModificacionArbitro(Arbitro arbitro){
        JFrame frame = new JFrame("Modificar Árbitro");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        String[] campos = {"Nombre Completo", "Fecha de nacimiento", "Partidos arbitrados", "Años experiencia"};
        int columnas = 15;

        JTextField nombre = new JTextField(arbitro.getNombreCompleto(), columnas);

        JDateChooser fecha = new JDateChooser(Date.from(arbitro.getFecha_nacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        fecha.setDateFormatString("dd/MM/yyyy");

        JTextField partidos = new JTextField(String.valueOf(arbitro.getPartidos_arbitrados()), columnas);
        JTextField anos = new JTextField(String.valueOf(arbitro.getAnos_experiencia()), columnas);

        JComponent[] textos = {nombre, fecha, partidos, anos};

        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(textos[i], configurarConstraints(1, i));
        }

        ActionListener guardado = e -> {
            String nombreCompleto = nombre.getText();
            Date fechaSeleccionada = fecha.getDate();
            String partidosArbitrados = partidos.getText();
            String anosExperiencia = anos.getText();

            Regex comprobacion = new Regex();
            GUI g = new GUI();
            nombreCompleto = comprobacion.mayusculasNombres(nombreCompleto);
            if (comprobacion.comprobarTexto(nombreCompleto)) {
                arbitro.setNombreCompleto(nombreCompleto);
                if (fechaSeleccionada != null) {
                    LocalDate fechaNacimientoParseada = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (comprobacion.comprobarFecha(fechaNacimientoParseada)) {
                        arbitro.setFecha_nacimiento(fechaNacimientoParseada);
                        if (comprobacion.comprobarNumero(partidosArbitrados, 5000)) {
                            arbitro.setPartidos_arbitrados(Integer.parseInt(partidosArbitrados));
                            if (comprobacion.comprobarNumero(anosExperiencia, 60)) {
                                arbitro.setAnos_experiencia(Integer.parseInt(anosExperiencia));
                                ControladorArbitro anadir = new ControladorArbitro();

                                if (anadir.modificarArbitro(arbitro)) {
                                    g.mensaje("Fila modificada correctamente", g.mensajeDeAcierto);
                                } else {
                                    g.mensaje("ERROR, no se ha podido modificar el arbitro");
                                }

                            } else {
                                g.mensaje("ERROR, años de experiencia no validos. Debe de ser un numero");
                            }

                        } else {
                            g.mensaje("ERROR, partidos arbitrados no valido. Debe de ser un numero");
                        }
                    }else {
                        g.mensaje("ERROR, fecha de nacimiento no valida. Debe de tener minimo 16 años");
                    }
                } else {
                    g.mensaje("ERROR, selecciona una fecha");
                }

            } else {
                g.mensaje("ERROR, nombre no valido solo se valen letras");
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

    public void ventanaConfirmarModificacionEquipo(Equipos equipo){
        JFrame frame = new JFrame("Modificar Equipo");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        JTextField nombre = new JTextField(equipo.getNombre_equipo(), 15);
        JTextField partidosPerdidos = new JTextField(String.valueOf(equipo.getPartidos_perdidos()), 15);
        JTextField partidosGanados = new JTextField(String.valueOf(equipo.getPartidos_ganados()), 15);
        JTextField trofeosLiga = new JTextField(String.valueOf(equipo.getTrofeos_liga()), 15);
        JTextField[] valores = {nombre, partidosPerdidos, partidosGanados, trofeosLiga};

        String[] campos = {"Nombre", "Partidos perdidos", "Partidos ganados", "Trofeos liga"};

        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(valores[i], configurarConstraints(1, i));
        }

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, campos.length);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        ActionListener accion = e -> {
            String nombreEquipo = nombre.getText();
            String partidosPerdidosValor = partidosPerdidos.getText();
            String partidosGanadosValor = partidosGanados.getText();
            String trofeosLigaValor = trofeosLiga.getText();
            ControladorEquipos comprobaciones = new ControladorEquipos();
            Regex filtros = new Regex();
            GUI g = new GUI();
            if (filtros.comprobarTexto(nombreEquipo)) {
                equipo.setNombre_equipo(nombreEquipo);
                if (filtros.comprobarNumero(partidosPerdidosValor, 2000)) {
                    equipo.setPartidos_perdidos(Integer.parseInt(partidosPerdidosValor));
                    if (filtros.comprobarNumero(partidosGanadosValor, 2000)) {
                        equipo.setPartidos_ganados(Integer.parseInt(partidosGanadosValor));
                        if (filtros.comprobarNumero(trofeosLigaValor, 200)) {
                            equipo.setTrofeos_liga(Integer.parseInt(trofeosLigaValor));
                            if (comprobaciones.modificarEquipo(equipo)) {
                                g.mensaje("El equipo ha sido modificado correctamente", g.mensajeDeAcierto);
                            }else {
                                g.mensaje("No se ha podido modificar el equipo");
                            }
                        }else {
                            g.mensaje("ERROR trofeos liga, solo se valen numeros");
                        }
                    }else {
                        g.mensaje("ERROR en partidos ganados, solo se valen numeros");
                    }
                }else {
                    g.mensaje("ERROR en partidos perdidos, solo se valen numeros");
                }
            }else {
                g.mensaje("ERROR nombre no valido, Solo se valen letras");
            }
        };

        frame.add(panel);
        frame.setVisible(true);
        boton.addActionListener(accion);
    }

    public void ventanaConfirmarModificacionPartido(Partidos partido){
        ControladorEquipos controladorEquipos = new ControladorEquipos();
        ControladorArbitro controladorArbitro = new ControladorArbitro();
        JFrame frame = new JFrame("Modificar Partido");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        String[] camposTexto = {"Fecha", "Hora", "Resultado local", "Resultado visitante"};

        JDateChooser fecha = new JDateChooser(Date.from(partido.getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        fecha.setDateFormatString("dd/MM/yyyy");
        String horaFormateada = partido.getHora().toLocalTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
        JTextField hora = new JTextField(horaFormateada, 15);
        JTextField resultadoLocal = new JTextField(String.valueOf(partido.getResultado_local()), 15);
        JTextField resultadoVisitante = new JTextField(String.valueOf(partido.getResultado_visitante()), 15);

        JComponent[] valores = {fecha, hora, resultadoLocal, resultadoVisitante};

        for (int i = 0; i < camposTexto.length; i++) {
            panel.add(new JLabel(camposTexto[i] + ":"), configurarConstraints(0, i));
            panel.add(valores[i], configurarConstraints(1, i));
        }

        int fila = camposTexto.length;

        JComboBox<String> cEquipoLocal = new JComboBox<>(controladorEquipos.mostrarNombreEquipos());
        cEquipoLocal.setSelectedItem(controladorEquipos.cualNombre(partido.getId_equipo_local()));
        panel.add(new JLabel("Equipo local:"), configurarConstraints(0, fila));
        panel.add(cEquipoLocal, configurarConstraints(1, fila++));

        JComboBox<String> cEquipoVisitante = new JComboBox<>(controladorEquipos.mostrarNombreEquipos());
        cEquipoVisitante.setSelectedItem(controladorEquipos.cualNombre(partido.getId_equipo_visitante()));
        panel.add(new JLabel("Equipo visitante:"), configurarConstraints(0, fila));
        panel.add(cEquipoVisitante, configurarConstraints(1, fila++));

        JComboBox<String> cArbitro1 = new JComboBox<>(controladorArbitro.mostrarNombreArbitro());
        cArbitro1.setSelectedItem(controladorArbitro.cualNombre(partido.getArbitro1()));
        panel.add(new JLabel("Árbitro 1:"), configurarConstraints(0, fila));
        panel.add(cArbitro1, configurarConstraints(1, fila++));

        JComboBox<String> cArbitro2 = new JComboBox<>(controladorArbitro.mostrarNombreArbitro());
        cArbitro2.setSelectedItem(controladorArbitro.cualNombre(partido.getArbitro2()));
        panel.add(new JLabel("Árbitro 2:"), configurarConstraints(0, fila));
        panel.add(cArbitro2, configurarConstraints(1, fila++));

        ActionListener accion = e -> {
            Regex filtros = new Regex();
            ControladorPartidos comprobaciones = new ControladorPartidos();
            GUI interfaz = new GUI();

            Date fechaSeleccionada = fecha.getDate();
            String horaPartido = hora.getText();
            String resultadoLocalPartido = resultadoLocal.getText();
            String resultadoVisitantePartido = resultadoVisitante.getText();

            String equipoLocalPartido = (String) cEquipoLocal.getSelectedItem();
            String equipoVisitantePartido = (String) cEquipoVisitante.getSelectedItem();
            String arbitro1Partido = (String) cArbitro1.getSelectedItem();
            String arbitro2Partido = (String) cArbitro2.getSelectedItem();

            if (fechaSeleccionada != null) {
                partido.setFecha(fechaSeleccionada.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());
                if (filtros.comprobarHora(horaPartido)) {
                    horaPartido = horaPartido + ":00";
                    partido.setHora(Time.valueOf(horaPartido));
                    if (filtros.comprobarNumero(resultadoLocalPartido, 30)) {
                        partido.setResultado_local(Integer.parseInt(resultadoLocalPartido));

                        if (filtros.comprobarNumero(resultadoVisitantePartido, 30)) {
                            int resultadoVisitantePartidoParseado = Integer.parseInt(resultadoVisitantePartido);

                            if (!equipoLocalPartido.equals(equipoVisitantePartido)) {
                                partido.setId_equipo_local(controladorEquipos.cualId(equipoLocalPartido));
                                partido.setId_equipo_visitante(controladorEquipos.cualId(equipoVisitantePartido));
                                if (!arbitro1Partido.equals(arbitro2Partido)) {
                                    partido.setArbitro1(controladorArbitro.cualId(arbitro1Partido));
                                    partido.setArbitro2(controladorArbitro.cualId(arbitro2Partido));
                                    if (comprobaciones.modificarPartido(partido)) {
                                        interfaz.mensaje("Partido modificado correctamente", interfaz.mensajeDeAcierto);
                                    } else {
                                        interfaz.mensaje("ERROR, no se ha podido modificar el partido");
                                    }

                                } else {
                                    interfaz.mensaje("ERROR, los arbitros tienen que ser distintos");
                                }

                            } else {
                                interfaz.mensaje("ERROR, los equipos tienen que ser distintos");
                            }

                        } else {
                            interfaz.mensaje("ERROR, resultado visitante no valido, tiene que ser un numero");
                        }

                    } else {
                        interfaz.mensaje("ERROR, resultado local no valido, tiene que ser un numero");
                    }

                } else {
                    interfaz.mensaje("ERROR, hora no valida, ejemplo: 16:12");
                }

            } else {
                interfaz.mensaje("ERROR, selecciona una fecha");
            }
        };

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, fila);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        frame.add(panel);
        frame.setVisible(true);
        boton.addActionListener(accion);
    }

    public void ventanaConfirmarModificacionPatrocinio(Patrocinador patrocinador, Equipos equipo){
        ControladorEquipos controladorEquipos = new ControladorEquipos();
        ControladorPatrocinador controladorPatrocinador = new ControladorPatrocinador();
        JFrame frame = new JFrame("Asignar Patrocinio");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        JComboBox<String> cPatrocinadores = new JComboBox<>(controladorPatrocinador.mostrarNombrePatrocinador());
        cPatrocinadores.setSelectedItem(patrocinador.getNombre_patrocinador());
        panel.add(new JLabel("Patrocinador:"), configurarConstraints(0, 0));
        panel.add(cPatrocinadores, configurarConstraints(1, 0));

        JComboBox<String> cEquipos = new JComboBox<>(controladorEquipos.mostrarNombreEquipos());
        cEquipos.setSelectedItem(equipo.getNombre_equipo());
        panel.add(new JLabel("Equipo:"), configurarConstraints(0, 1));
        panel.add(cEquipos, configurarConstraints(1, 1));

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, 2);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        String[] seleccionados = new String[2];
        int[] ides = new int[2];
        ActionListener accion = e -> {
            ControladorPatrocinios anadir = new ControladorPatrocinios();
            seleccionados[0] = (String) cPatrocinadores.getSelectedItem();
            seleccionados[1] = (String) cEquipos.getSelectedItem();
            ides[0] = anadir.localizarIdPatrocinador(seleccionados[0]);
            ides[1] = anadir.localizarIdEquipo(seleccionados[1]);
            Patrocinios patrocinios = new Patrocinios(ides[0], ides[1]);
            GUI g = new GUI();
            if (anadir.modificarPatrocinio(patrocinios, patrocinador.getId_patrocinador(), equipo.getId_equipo())) {
                g.mensaje("Fila modificada correctamente", g.mensajeDeAcierto);
            }else {
                g.mensaje("No se ha podido modificar la fila");
            }
        };

        frame.add(panel);
        frame.setVisible(true);
        boton.addActionListener(accion);
    }


    public void ventanaConfirmarModificacionPatrocinador(Patrocinador patrocinador){
        JFrame frame = new JFrame("Modificar Patrocinador");
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        JTextField nombre = new JTextField(patrocinador.getNombre_patrocinador(),15);
        JTextField telefono = new JTextField(String.valueOf(patrocinador.getTelefono()),15);
        JTextField email = new JTextField(patrocinador.getEmail(),15);
        JTextField[] jTextFields = {nombre, telefono, email};
        String[] campos = {"Nombre", "Telefono", "Email"};

        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(jTextFields[i], configurarConstraints(1, i));
        }

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, campos.length);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        ActionListener accion = e -> {
            Regex filtros = new Regex();
            ControladorPatrocinador anadir = new ControladorPatrocinador();
            GUI g = new GUI();
            String nombrePatrocinador = nombre.getText();
            String telefonoPatrocinador = telefono.getText();
            String emailPatrocinador = email.getText();
            if (filtros.comprobarTexto(nombrePatrocinador)) {
                patrocinador.setNombre_patrocinador(nombrePatrocinador);
                if (filtros.comprobarTelefono(telefonoPatrocinador)) {
                    patrocinador.setTelefono(Integer.parseInt(telefonoPatrocinador));
                    if (filtros.comprobarEmail(emailPatrocinador)) {
                        patrocinador.setEmail(emailPatrocinador);
                        if (anadir.ExistePatrocinador(patrocinador)) {
                            g.mensaje("Este patrocinador ya existe");
                        }else {
                            if (anadir.modificarPatrocinador(patrocinador)) {
                                g.mensaje("Fila modificada correctamente", g.mensajeDeAcierto);
                            }else {
                                g.mensaje("No se ha podido modificar al patrocinador");
                            }
                        }
                    }else {
                        g.mensaje("ERROR, email no valido (Tiene que tener: texto@texto.extensionMax(3))");
                    }
                }else {
                    g.mensaje("ERROR, telefono no valido. Tiene que: Empezar por 6, 7 o 9 y tener 9 o 12 digitos");
                }
            }else {
                g.mensaje("ERROR, nombre no valido (Solo se valen letras y cada palabra empieza con una mayuscula)");
            }
        };

        frame.add(panel);
        frame.setVisible(true);
        boton.addActionListener(accion);
    }

    public void ventanaModificarArbitro(String[] arbitrosNombre){
        ControladorArbitro controladorArbitro = new ControladorArbitro();
        JFrame inicio = new JFrame("Modificar arbitro");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel contrasena = new JPanel();
        JLabel texto = new JLabel("Arbitro:");
        JComboBox<String> arbitros = new JComboBox<>(arbitrosNombre);
        arbitros.setPreferredSize(new  Dimension(100, 25));
        contrasena.add(texto);
        contrasena.add(arbitros);
        ActionListener accion = e -> {
            String arbitro = (String) arbitros.getSelectedItem();
            ventanaConfirmarModificacionArbitro(controladorArbitro.encontrarArbitro(controladorArbitro.cualId(arbitro)));
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(contrasena, BorderLayout.CENTER);
        inicio.setVisible(true);
        arbitros.addActionListener(accion);
    }
    public void ventanaModificarEquipo(String[] equiposNombre){
        ControladorEquipos controladorEquipos = new ControladorEquipos();
        JFrame inicio = new JFrame("Modificar equipo");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaEquipos = new JPanel();
        JLabel texto = new JLabel("equipo:");
        JComboBox<String> equipos = new JComboBox<>(equiposNombre);
        equipos.setPreferredSize(new  Dimension(100, 25));
        listaEquipos.add(texto);
        listaEquipos.add(equipos);
        ActionListener accion = e -> {
            String equipo = (String) equipos.getSelectedItem();
            ventanaConfirmarModificacionEquipo(controladorEquipos.encontrarEquipo(controladorEquipos.cualId(equipo)));
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaEquipos, BorderLayout.CENTER);
        inicio.setVisible(true);
        equipos.addActionListener(accion);
    }
    public void ventanaModificarPartido(Integer[] idesPartidos){
        ControladorPartidos controladorPartidos = new ControladorPartidos();
        JFrame inicio = new JFrame("Modificar partido");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPartidos = new JPanel();
        JLabel texto = new JLabel("partido:");
        JComboBox<Integer> partidos = new JComboBox<>(idesPartidos);
        partidos.setPreferredSize(new  Dimension(100, 25));
        listaPartidos.add(texto);
        listaPartidos.add(partidos);
        ActionListener accion = e -> {
            Integer partido = (Integer) partidos.getSelectedItem();
            ventanaConfirmarModificacionPartido(controladorPartidos.encontrarPartido(partido));
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPartidos, BorderLayout.CENTER);
        inicio.setVisible(true);
        partidos.addActionListener(accion);
    }
    public void ventanaModificarPatrocinio(String[] equipos, String[] patrocinadores){
        ControladorPatrocinador controladorPatrocinador = new ControladorPatrocinador();
        ControladorEquipos controladorEquipos = new ControladorEquipos();
        JFrame inicio = new JFrame("Modificar patrocinio");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(500, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPatrocinios = new JPanel();
        JLabel texto = new JLabel("patrocinador:");
        JComboBox<String> cPatrocinadores = new JComboBox<>(patrocinadores);
        JLabel texto2 = new JLabel("Equipo:");
        JComboBox<String> cEquipos = new JComboBox<>(equipos);
        cPatrocinadores.setPreferredSize(new  Dimension(100, 25));
        cEquipos.setPreferredSize(new Dimension(100, 25));
        JButton confirmar = new JButton("Confirmar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        listaPatrocinios.add(texto);
        listaPatrocinios.add(cPatrocinadores);
        listaPatrocinios.add(texto2);
        listaPatrocinios.add(cEquipos);
        ActionListener accion = e -> {
            String patrocinador = (String) cPatrocinadores.getSelectedItem();
            String equipo = (String) cEquipos.getSelectedItem();
            ventanaConfirmarModificacionPatrocinio(controladorPatrocinador.encontrarPatrocinador(controladorPatrocinador.cualId(patrocinador)), controladorEquipos.encontrarEquipo(controladorEquipos.cualId(equipo)));
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPatrocinios, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
        confirmar.addActionListener(accion);
    }
    public void ventanaModificarPatrocinador(String[] patrocinadoresNombre){
        JFrame inicio = new JFrame("Modificar patrocinador");
        inicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicio.setSize(500, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPatrocinadores = new JPanel();
        JLabel texto = new JLabel("patrocinador:");
        JComboBox<String> cPatrocinadores = new JComboBox<>(patrocinadoresNombre);
        cPatrocinadores.setPreferredSize(new  Dimension(100, 25));
        JButton confirmar = new JButton("Confirmar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        listaPatrocinadores.add(texto);
        listaPatrocinadores.add(cPatrocinadores);
        ActionListener accion = e -> {
            String patrocinador = (String) cPatrocinadores.getSelectedItem();
            ControladorPatrocinador controladorPatrocinador = new ControladorPatrocinador();
            ventanaConfirmarModificacionPatrocinador(controladorPatrocinador.encontrarPatrocinador(controladorPatrocinador.cualId(patrocinador)));
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPatrocinadores, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
        confirmar.addActionListener(accion);
    }

    public void ventanaCambioPassword() {
        JFrame frame = new JFrame("Cambiar contraseña");
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        JButton documento = new JButton("Documento");
        JPanel opciones = new JPanel();
        opciones.add(anadir);
        opciones.add(eliminar);
        opciones.add(modificar);
        opciones.add(documento);
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
            nTabla = tablaPatrocinios;
            ControladorPatrocinios mostrar = new ControladorPatrocinios();
            tabla.setModel(mostrar.mostrarPatrocinios());
        };
        patrocinios.addActionListener(listaPatrocinios);
        ActionListener listaPatrocinador = e -> {
            nTabla = tablaPatrocinador;
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

                ventanaPatrocinador();
            }
            if (nTabla == tablaPatrocinios) {
                ControladorPatrocinador pa = new ControladorPatrocinador();
                ventanaPatrocinios(pa.mostrarNombrePatrocinador(), eq.mostrarNombreEquipos());
            }

        };
        anadir.addActionListener(anade);
        ActionListener elimina = e -> {
            ControladorEquipos controladorEquipos = new ControladorEquipos();
            ControladorPatrocinador controladorPatrocinador = new ControladorPatrocinador();
            if (nTabla == tablaArbitro) {
                ControladorArbitro ar = new ControladorArbitro();
                ventanaEliminarArbitro(ar.mostrarNombreArbitro());
            }
            if (nTabla == tablaEquipos) {
                ventanaEliminarEquipo(controladorEquipos.mostrarNombreEquipos());
            }
            if (nTabla == tablaPartidos) {
                ControladorPartidos controladorPartidos = new ControladorPartidos();
                ventanaEliminarPartido(controladorPartidos.mostrarIdesPartidos());
            }
            if (nTabla == tablaPatrocinador) {
                ventanaEliminarPatrocinador(controladorPatrocinador.mostrarNombrePatrocinador());
            }
            if (nTabla == tablaPatrocinios) {
                ventanaEliminarPatrocinio(controladorPatrocinador.mostrarNombrePatrocinador(), controladorEquipos.mostrarNombreEquipos());
            }

        };
        eliminar.addActionListener(elimina);
        ActionListener modifica = e -> {
            ControladorEquipos controladorEquipos = new ControladorEquipos();
            ControladorPatrocinador controladorPatrocinador = new ControladorPatrocinador();
            if (nTabla == tablaArbitro) {
                ControladorArbitro ar = new ControladorArbitro();
                ventanaModificarArbitro(ar.mostrarNombreArbitro());
            }
            if (nTabla == tablaEquipos) {
                ventanaModificarEquipo(controladorEquipos.mostrarNombreEquipos());
            }
            if (nTabla == tablaPartidos) {
                ControladorPartidos controladorPartidos = new ControladorPartidos();
                ventanaModificarPartido(controladorPartidos.mostrarIdesPartidos());
            }
            if (nTabla == tablaPatrocinador) {
                ventanaModificarPatrocinador(controladorPatrocinador.mostrarNombrePatrocinador());
            }
            if (nTabla == tablaPatrocinios) {
                ventanaModificarPatrocinio(controladorEquipos.mostrarNombreEquipos(), controladorPatrocinador.mostrarNombrePatrocinador());
            }
        };
        modificar.addActionListener(modifica);
        ActionListener imprimirDocumento = e -> {
            if (nTabla == tablaArbitro) {
                ControladorArbitro ar = new ControladorArbitro();
                if (ar.imprimirArbitros()) {
                    mensaje("Se ha creado el documento arbitros correctamente", mensajeDeAcierto);
                }
            }
            if (nTabla == tablaEquipos) {
                ControladorEquipos controladorEquipos = new ControladorEquipos();
                if (controladorEquipos.imprimirEquipos()) {
                    mensaje("Se ha creado el documento equipos correctamente", mensajeDeAcierto);
                }
            }
            if (nTabla == tablaPartidos) {
                ControladorPartidos controladorPartidos = new ControladorPartidos();
                if (controladorPartidos.imprimirPartidos()) {
                    mensaje("Se ha creado el documento partidos correctamente", mensajeDeAcierto);
                }
            }
            if (nTabla == tablaPatrocinador) {
                ControladorPatrocinador controladorPatrocinador = new ControladorPatrocinador();
                if (controladorPatrocinador.imprimirPatrocinadores()) {
                    mensaje("Se ha creado el documento patrocinadores correctamente", mensajeDeAcierto);
                }
            }
            if (nTabla == tablaPatrocinios) {
                ControladorPatrocinios controladorPatrocinios = new ControladorPatrocinios();
                if (controladorPatrocinios.imprimirPatrocinios()) {
                    mensaje("Se ha creado el documento patrocinios correctamente", mensajeDeAcierto);
                }
            }
        };
        documento.addActionListener(imprimirDocumento);
    }
}
