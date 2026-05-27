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

// En esta clase se encuentran todas las ventanas de la interfaz grafica
public class GUI {
    private JTable tabla;
    // Declaramos las variables con las que identificamos las tablas en la pagina principal para ir cambiando de tabla
    int tablaPartidos = 3;
    int nTabla = tablaPartidos;
    int tablaArbitro = 1;
    int tablaEquipos = 2;
    int tablaPatrocinador = 5;
    int tablaPatrocinios = 4;
    // Para aquellos mensajes de acierto le asignamos un tiempo para que desaparezca de la pantalla tras transcurrirlo (El numero es el que yo he considerado idoneo)
    int mensajeDeAcierto = 1000;
    int tiempoDeCierre = 1300;
    // Los controladores y el regex los uso en practicamente todas las ventanas para hacer todas las funciones de añadir eliminar...
    // por lo que los declaramos en la clase para no crear mas objetos de los necesarios
    ControladorArbitro controladorArbitro = new ControladorArbitro();
    ControladorEquipos controladorEquipos = new ControladorEquipos();
    ControladorPatrocinios controladorPatrocinios = new ControladorPatrocinios();
    ControladorPartidos controladorPartidos = new ControladorPartidos();
    ControladorPatrocinador controladorPatrocinador = new ControladorPatrocinador();
    Regex comprobacion = new Regex();
    private JComboBox<String> arbitros;
    private JComboBox<String> equipos;
    private JComboBox<PartidoItem> partidos;
    private JComboBox<String> patrocinadores;
    private JComboBox<PatrocinioItem> patrocinios;
    // Los campos numericos tienen un limite de maximo para evitar que el programa falle por sobre pasar el limite asi que estas variables evitan ese problema y tambien ponen un limite mas logico
    int limitePartidosArbitrados = 5000;
    int limiteAñosExperiencia = 60;
    int limitePartidosJugados = 2000;
    int limiteTrofeosLiga = 200;
    int limiteResultadoEquipo = 200;
// Con esta funcion calculo el tamaño idoneo de las ventanas ya que hacemos una regla de tres con la distancia de las palabras y sale el mejor tamaño para la ventana
    private static int reglaDeTres(int n, int a, int b){
        return n*a/b;
    }
    // En esta clase diseñamos la posicion de los elementos de las ventanas para que aparezcan de la mejor forma
    private static GridBagConstraints configurarConstraints(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }
    // Este metodo es una ventana con la que muestro informacion al usuario y el parametro de Component permite que aparezcan los mensajes en las ventanas de dialogo (Jdialog)
    public void mensaje(Component padre, String texto, int tiempo) {

        JWindow toast = new JWindow();

        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(60, 60, 60));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        toast.add(label);
        toast.pack();

        toast.setLocationRelativeTo(padre);

        toast.setAlwaysOnTop(true);
        toast.setVisible(true);

        javax.swing.Timer timer = new javax.swing.Timer(tiempo, e -> toast.dispose());
        timer.setRepeats(false);
        timer.start();
    }
    // Metodo con el que muestro mensajes por pantalla pero sin tiempo (Lo uso para los errores ya que es importante que el usuario se detenga a leerlo)
    public void mensaje(Component padre, String dato) {
        final JDialog dialog = new JDialog(javax.swing.SwingUtilities.getWindowAncestor(padre));
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
// Metodo que abre una ventana con los campos de la tabla arbitro para poder añadir uno nuevo
    public void ventanaArbitro() {
        // Es un Dialog para que no se pueda interactuar con el resto de cosas externas a la ventana y pese menos
        JDialog frame = new JDialog((Frame) null, "Añadir arbitro", true);
        // Solo cierra la ventana Dialog despues sigue abierta la aplicacion
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
    // Son los nombres de los campos de la tabla (No exactamente como estan llamados en la base de datos)
        String[] campos = {"Nombre Completo", "Fecha de nacimiento", "Partidos arbitrados", "Años experiencia"};
        int columnas = 15;

        JTextField nombre = new JTextField(columnas);

        JDateChooser fecha = new JDateChooser();
        fecha.setDateFormatString("dd/MM/yyyy");

        JTextField partidos = new JTextField(columnas);
        JTextField anos = new JTextField(columnas);

        JComponent[] textos = {nombre, fecha, partidos, anos};
// Añado a cada campo de texto que se ha creado su texto correspodiente con el nombre del campo que se va a añadir
        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(textos[i], configurarConstraints(1, i));
        }

        JButton boton = new JButton("Guardar");
        // En esta accion compruebo que todos los campos añadidos son validos e inserto el nuevo arbitro a la base de datos
        ActionListener guardado = e -> {
            String nombreCompleto = nombre.getText();
            Date fechaSeleccionada = fecha.getDate();
            String partidosArbitrados = partidos.getText();
            String anosExperiencia = anos.getText();
            nombreCompleto = comprobacion.mayusculasNombres(nombreCompleto);
            if (comprobacion.comprobarTexto(nombreCompleto)) {
                if (fechaSeleccionada != null) {

                    LocalDate fechaNacimientoParseada = fechaSeleccionada.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    if (comprobacion.comprobarFecha(fechaNacimientoParseada)) {
                        if (comprobacion.comprobarNumero(partidosArbitrados, limitePartidosArbitrados)) {
                            int partidosArbitradosNumero = Integer.parseInt(partidosArbitrados);
                            if (comprobacion.comprobarNumero(anosExperiencia, limiteAñosExperiencia)) {
                                int anosExperienciaNumero = Integer.parseInt(anosExperiencia);
                                if (controladorArbitro.trabajaMenosQueVive(fechaNacimientoParseada, anosExperienciaNumero)) {
                                    if (controladorArbitro.partidosJugadosXAnos(partidosArbitradosNumero, anosExperienciaNumero)) {
                                        Arbitro arbitro = new Arbitro(nombreCompleto, fechaNacimientoParseada,
                                                partidosArbitradosNumero, anosExperienciaNumero);

                                        if (controladorArbitro.existeNombre(nombreCompleto)) {
                                            mensaje(boton, "Este arbitro ya existe", mensajeDeAcierto);
                                        }else {
                                            if (controladorArbitro.anadirArbitro(arbitro)) {
                                                refrescarTabla();

                                                mensaje(boton, "Fila añadida", mensajeDeAcierto);
                                                javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> frame.dispose());
                                                t.setRepeats(false);
                                                t.start();

                                            } else {
                                                mensaje(boton,"ERROR, no se ha podido añadir el arbitro");
                                            }
                                        }
                                    }else {
                                        mensaje(boton, "ERROR, no se puede arbitrar tantos partidos en tan poco tiempo (110 partidos por año)");
                                    }
                                }else {
                                    mensaje(boton, "ERROR, no se puede introducir un valor de años trabajados superior a la edad del arbitro");
                                }
                            } else {
                                mensaje(boton,"ERROR, años de experiencia no validos. Debe de ser un numero entre 0 y " + limiteAñosExperiencia);
                            }

                        } else {
                            mensaje(boton,"ERROR, partidos arbitrados no valido. Debe de ser un numero entre 0 y " + limitePartidosArbitrados);
                        }
                    }else {
                        mensaje(boton,"ERROR, fecha de nacimiento no valida. Debe de tener minimo 16 años y maximo 60");
                    }
                } else {
                    mensaje(boton,"ERROR, selecciona una fecha");
                }

            } else {
                mensaje(boton,"ERROR nombre no valido, Solo se valen letras y minimo 2");
            }
        };


        GridBagConstraints gbc = configurarConstraints(1, campos.length);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);
        boton.addActionListener(guardado);
        frame.add(panel);
        frame.setVisible(true);
    }
    // Metodo que abre una ventana con los campos de la tabla equipos para poder añadir uno nuevo
    public void ventanaEquipos() {
        JDialog frame = new JDialog((Frame) null, "Añadir equipo", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        JTextField nombre = new JTextField(15);
        JTextField partidosPerdidos = new JTextField(15);
        JTextField partidosGanados = new JTextField(15);
        JTextField trofeosLiga = new JTextField(15);
        JTextField[] valores = {nombre, partidosPerdidos, partidosGanados, trofeosLiga};

        String[] campos = {"Nombre", "Partidos perdidos", "Partidos ganados", "Trofeos liga"};
        // Añade el nombre de los campos con sus respectivos campos de texto
        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(valores[i], configurarConstraints(1, i));
        }

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, campos.length);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);
// Corrobora que los campos añadidos por el usuario son válidos y crea el nuevo equipo en la base de datos
        ActionListener accion = e -> {
          String nombreEquipo = nombre.getText();
          String partidosPerdidosValor = partidosPerdidos.getText();
          String partidosGanadosValor = partidosGanados.getText();
          String trofeosLigaValor = trofeosLiga.getText();

          nombreEquipo = comprobacion.mayusculasNombres(nombreEquipo);
          if (comprobacion.comprobarTexto(nombreEquipo)) {
              if (comprobacion.comprobarNumero(partidosPerdidosValor, limitePartidosJugados)) {
                  int partidosPerdidosFiltrado = Integer.parseInt(partidosPerdidosValor);
                  if (comprobacion.comprobarNumero(partidosGanadosValor, limitePartidosJugados)) {
                      int partidosGanadosFiltrado = Integer.parseInt(partidosGanadosValor);
                      if (comprobacion.comprobarNumero(trofeosLigaValor, limiteTrofeosLiga)) {
                          int trofeosLigaFiltrado = Integer.parseInt(trofeosLigaValor);
                          Equipos equipo = new Equipos(nombreEquipo, partidosPerdidosFiltrado, partidosGanadosFiltrado, trofeosLigaFiltrado);
                          if (controladorEquipos.existeNombre(nombreEquipo)) {
                              mensaje(boton,"Este equipo ya existe", mensajeDeAcierto);
                          }else {
                              if (controladorEquipos.anadirEquipo(equipo)) {
                                  refrescarTabla();
                                  mensaje(boton, "Fila añadida", mensajeDeAcierto);
                                  javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> frame.dispose());
                                  t.setRepeats(false);
                                  t.start();
                              }else {
                                  mensaje(boton,"No se ha podido añadir el equipo");
                              }
                          }
                      }else {
                          mensaje(boton,"ERROR trofeos liga, solo se valen numeros entre 0 y " + limiteTrofeosLiga);
                      }
                  }else {
                    mensaje(boton,"ERROR en partidos ganados, solo se valen numeros entre 0 y " + limitePartidosJugados);
                  }
              }else {
                  mensaje(boton,"ERROR en partidos perdidos, solo se valen numeros entre 0 y " + limitePartidosJugados);
              }
          }else {
             mensaje(boton,"ERROR nombre no valido, Solo se valen letras y minimo 2");
          }
        };
        boton.addActionListener(accion);
        frame.add(panel);
        frame.setVisible(true);
    }

    // Metodo que abre una ventana con los campos de la tabla partidos para poder añadir uno nuevo
    public void ventanaPartidos() {
        JDialog frame = new JDialog((Frame) null, "Añadir partido", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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

        JComboBox<String> cEquipoLocal = new JComboBox<>(controladorEquipos.mostrarNombreEquipos());
        panel.add(new JLabel("Equipo local:"), configurarConstraints(0, fila));
        panel.add(cEquipoLocal, configurarConstraints(1, fila++));

        JComboBox<String> cEquipoVisitante = new JComboBox<>(controladorEquipos.mostrarNombreEquipos());
        panel.add(new JLabel("Equipo visitante:"), configurarConstraints(0, fila));
        panel.add(cEquipoVisitante, configurarConstraints(1, fila++));

        JComboBox<String> cArbitro1 = new JComboBox<>(controladorArbitro.mostrarNombreArbitro());
        panel.add(new JLabel("Árbitro 1:"), configurarConstraints(0, fila));
        panel.add(cArbitro1, configurarConstraints(1, fila++));

        JComboBox<String> cArbitro2 = new JComboBox<>(controladorArbitro.mostrarNombreArbitro());
        panel.add(new JLabel("Árbitro 2:"), configurarConstraints(0, fila));
        panel.add(cArbitro2, configurarConstraints(1, fila++));
        JButton boton = new JButton("Guardar");

        ActionListener accion = e -> {

            Date fechaSeleccionada = fecha.getDate();
            String horaPartido = hora.getText();
            String resultadoLocalPartido = resultadoLocal.getText();
            String resultadoVisitantePartido = resultadoVisitante.getText();

            String equipoLocalPartido = (String) cEquipoLocal.getSelectedItem();
            String equipoVisitantePartido = (String) cEquipoVisitante.getSelectedItem();
            String arbitro1Partido = (String) cArbitro1.getSelectedItem();
            String arbitro2Partido = (String) cArbitro2.getSelectedItem();

            if (fechaSeleccionada != null) {
                if (comprobacion.comprobarFechaPartido(fechaSeleccionada.toString())) {
                    LocalDate fechaPartidoParseada = fechaSeleccionada.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();


                    if (comprobacion.comprobarHora(horaPartido)) {

                        horaPartido = horaPartido + ":00";
                        Time horaPartidoParseado = Time.valueOf(horaPartido);

                        if (comprobacion.comprobarNumero(resultadoLocalPartido, limiteResultadoEquipo)) {
                            int resultadoLocalPartidoParseado = Integer.parseInt(resultadoLocalPartido);

                            if (comprobacion.comprobarNumero(resultadoVisitantePartido, limiteResultadoEquipo)) {
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

                                        if (controladorPartidos.anadirPartido(partido)) {
                                            refrescarTabla();

                                            mensaje(boton, "Partido añadido correctamente", mensajeDeAcierto);
                                            javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> frame.dispose());
                                            t.setRepeats(false);
                                            t.start();
                                        } else {
                                            mensaje(boton,"ERROR, no se ha podido añadir el partido");
                                        }

                                    } else {
                                        mensaje(boton,"ERROR, los arbitros tienen que ser distintos");
                                    }

                                } else {
                                    mensaje(boton,"ERROR, los equipos tienen que ser distintos");
                                }

                            } else {
                                mensaje(boton,"ERROR, resultado visitante no valido, tiene que ser un numero entre 0 y " + limiteResultadoEquipo);
                            }

                        } else {
                            mensaje(boton,"ERROR, resultado local no valido, tiene que ser un numero entre 0 y " + limiteResultadoEquipo);
                        }

                    } else {
                        mensaje(boton,"ERROR, hora no valida, ejemplo: 16:12");
                    }
                }else {
                    mensaje(boton,"ERROR, la fecha del partido no puede ser anterior a 1892");
                }


            } else {
                mensaje(boton,"ERROR, selecciona una fecha");
            }
        };

        GridBagConstraints gbc = configurarConstraints(1, fila);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);
        boton.addActionListener(accion);
        frame.add(panel);
        frame.setVisible(true);
    }
    // Metodo que abre una ventana con los campos de la tabla patrocinador para poder añadir uno nuevo

    public void ventanaPatrocinador() {
        JDialog frame = new JDialog((Frame) null, "Añadir patrocinador", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        JTextField nombre = new JTextField(15);
        JTextField telefono = new JTextField(15);
        JTextField email = new JTextField(15);
        JTextField[] jTextFields = {nombre, telefono, email};
        String[] campos = {"Nombre", "Telefono", "Email"};
// Coloca el nombre del campo que se añade al lado del campo de texto
        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(campos[i] + ":"), configurarConstraints(0, i));
            panel.add(jTextFields[i], configurarConstraints(1, i));
        }

        JButton boton = new JButton("Guardar");
        GridBagConstraints gbc = configurarConstraints(1, campos.length);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);

        ActionListener accion = e -> {

            String nombrePatrocinador = nombre.getText();
            String telefonoPatrocinador = telefono.getText();
            String emailPatrocinador = email.getText();
            nombrePatrocinador = comprobacion.mayusculasNombres(nombrePatrocinador);
            if (comprobacion.comprobarTexto(nombrePatrocinador)) {
                if (comprobacion.comprobarTelefono(telefonoPatrocinador)) {
                    int numeroTelefonico = Integer.parseInt(telefonoPatrocinador);
                    if (comprobacion.comprobarEmail(emailPatrocinador)) {
                        Patrocinador p = new Patrocinador(nombrePatrocinador, numeroTelefonico, emailPatrocinador);
                        if (controladorPatrocinador.ExistePatrocinador(p)) {
                            mensaje(boton,"Este patrocinador ya existe", mensajeDeAcierto);
                        }else {
                            if (controladorPatrocinador.anadirPatrocinador(p)) {
                                refrescarTabla();

                                mensaje(boton, "Fila añadida", mensajeDeAcierto);
                                javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> frame.dispose());
                                t.setRepeats(false);
                                t.start();
                            }else {
                                mensaje(boton,"No se ha podido añadir al patrocinador");
                            }
                        }
                    }else {
                        mensaje(boton,"ERROR, email no valido (Tiene que tener: texto@texto.extensionMax(3))");
                    }
                }else {
                    mensaje(boton,"ERROR, telefono no valido. Tiene que: Empezar por 6, 7 o 9 y tener 9 o 12 digitos");
                }
            }else {
                mensaje(boton,"ERROR nombre no valido, Solo se valen letras y minimo 2");
            }
        };
        boton.addActionListener(accion);
        frame.add(panel);
        frame.setVisible(true);
    }

    // Metodo que abre una ventana con los campos de la tabla patrocinios para poder añadir uno nuevo
    public void ventanaPatrocinios() {
        JDialog frame = new JDialog((Frame) null, "Añadir patrocinio", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        JComboBox<String> cPatrocinadores = new JComboBox<>(controladorPatrocinador.mostrarNombrePatrocinador());
        panel.add(new JLabel("Patrocinador:"), configurarConstraints(0, 0));
        panel.add(cPatrocinadores, configurarConstraints(1, 0));

        JComboBox<String> cEquipos = new JComboBox<>(controladorEquipos.mostrarNombreEquipos());
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

            if (anadir.existePatrocinio(patrocinios)) {
                mensaje(boton,"Este patrocinio ya existe");
            }else {
                if (anadir.anadirPatrocinio(patrocinios)) {
                    refrescarTabla();

                    mensaje(boton, "Fila añadida", mensajeDeAcierto);
                    javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> frame.dispose());
                    t.setRepeats(false);
                    t.start();
                }else {
                    mensaje(boton,"No se ha podido añadir la fila");
                }
            }
        };
        boton.addActionListener(accion);
        frame.add(panel);
        frame.setVisible(true);
    }

    // Este metodo genera una ventana con las filas afectas por la fila que se va a eliminar en la tabla arbitro
    public void ventanaConfirmarEliminacionArbitro(String arbitro){
        JDialog inicio = new JDialog((Frame) null, "Eliminar", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(1000, 650);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JTable tabla = new JTable();
        JScrollPane subeYBaja = new JScrollPane(tabla);
        tabla.setModel(controladorArbitro.mostrarFilasAfectadasPorArbitro(controladorArbitro.cualId(arbitro)));
        JButton confirmar = new JButton("Confirmar");
        ActionListener accion = e -> {
            if (controladorArbitro.eliminarArbitro(controladorArbitro.cualId(arbitro))) {
                refrescarTabla();
                arbitros.setModel(controladorArbitro.mostrarNombreArbitro());
                mensaje(confirmar, "Fila eliminada", mensajeDeAcierto);
                javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> inicio.dispose());
                t.setRepeats(false);
                t.start();

            }else {
                mensaje(confirmar,"No se ha podido eliminar al arbitro");
            }
        };
        ActionListener accion2 = e -> {
            inicio.dispose();
        };
        JPanel mensaje = new JPanel();
        mensaje.setPreferredSize(new Dimension(100, 80));
        JLabel texto = new JLabel("Estas seguro de que quieres eliminar a " + arbitro + "? se eliminaran también las siguientes filas relacionadas");
        mensaje.add(texto);

        JButton cancelar = new JButton("Cancelar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        botones.add(cancelar);
        confirmar.addActionListener(accion);
        cancelar.addActionListener(accion2);
        inicio.add(mensaje, BorderLayout.NORTH);
        inicio.add(subeYBaja, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
    }

    // Este metodo genera una ventana con las filas afectas por la fila que se va a eliminar en la tabla equipos
    public void ventanaConfirmarEliminacionEquipo(String equipo){
        JDialog inicio = new JDialog((Frame) null, "Eliminar", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(1000, 650);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JTable tabla = new JTable();
        JScrollPane subeYBaja = new JScrollPane(tabla);
        tabla.setModel(controladorEquipos.mostrarFilasAfectadasPorEquipoEnPartido(controladorEquipos.cualId(equipo)));
        JButton confirmar = new JButton("Confirmar");
        ActionListener accion = e -> {
            if (controladorEquipos.eliminarEquipo(controladorEquipos.cualId(equipo))) {
                refrescarTabla();
                equipos.setModel(controladorEquipos.mostrarNombreEquipos());
                mensaje(confirmar, "Fila eliminada", mensajeDeAcierto);
                javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> inicio.dispose());
                t.setRepeats(false);
                t.start();
            }else {
                mensaje(confirmar,"No se ha podido eliminar al equipo");
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

        JButton cancelar = new JButton("Cancelar");
        JButton tablaPartido = new JButton("Partidos");
        JButton tablaPatrocinios = new JButton("Patrocinios");
        JPanel cContrasena = new JPanel();
        cContrasena.add(tablaPartido);
        cContrasena.add(tablaPatrocinios);
        cContrasena.add(confirmar);
        cContrasena.add(cancelar);
        confirmar.addActionListener(accion);
        cancelar.addActionListener(accion2);
        tablaPartido.addActionListener(accion4);
        tablaPatrocinios.addActionListener(accion3);
        inicio.add(mensaje, BorderLayout.NORTH);
        inicio.add(subeYBaja, BorderLayout.CENTER);
        inicio.add(cContrasena, BorderLayout.SOUTH);
        inicio.setVisible(true);
    }

    // Esta ventana pregunta al usuario si está seguro de que desea eliminar la fila seleccionada en la tabla partidos
    public void ventanaConfirmarEliminacionPartido(Partidos partido){
        JDialog inicio = new JDialog((Frame) null, "Eliminar", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(700, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel mensaje = new JPanel();
        JLabel texto = new JLabel("¿Estas seguro de que quieres eliminar este partido? fecha: " + partido.getFecha());
        mensaje.add(texto);
        JButton confirmar = new JButton("Confirmar");
        ActionListener accion = e -> {
            if (controladorPartidos.eliminarPartido(partido.getId_partido())) {
                refrescarTabla();
                partidos.setModel(
                        new DefaultComboBoxModel<>(
                                controladorPartidos.mostrarPartidosEliminarOModificar()
                        )
                );
                mensaje(confirmar, "Fila eliminada", mensajeDeAcierto);
                javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> inicio.dispose());
                t.setRepeats(false);
                t.start();
            }else {
                mensaje(confirmar,"No se ha podido eliminar el Partido");
            }
        };
        ActionListener accion2 = e -> {
            inicio.dispose();
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));

        JButton cancelar = new JButton("Cancelar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        botones.add(cancelar);
        // Al ser un JDialog se añade primero la función al boton si lo haces al revés no llegará a coger la función el boton por lo que no hará nada al clickar
        confirmar.addActionListener(accion);
        cancelar.addActionListener(accion2);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(mensaje, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
    }

    // Esta ventana pregunta al usuario si está seguro de que desea eliminar la fila seleccionada en la tabla patrocinios
    public void ventanaConfirmarEliminacionPatrocinio(PatrocinioItem patrocinioItem){
        JDialog inicio = new JDialog((Frame) null, "Eliminar", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(700, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel mensaje = new JPanel();
        JLabel texto = new JLabel("¿Estas seguro de que quieres eliminar este patrocinio: " + patrocinioItem.toString() + "?");
        mensaje.add(texto);
        JButton confirmar = new JButton("Confirmar");
        ActionListener accion = e -> {
                if (controladorPatrocinios.eliminarPatrocinio(patrocinioItem.getPatrocinio())) {
                    refrescarTabla();
                    patrocinios.setModel(
                            new DefaultComboBoxModel<>(
                                    controladorPatrocinios.mostrarPatrociniosNombre()
                            )
                    );
                    mensaje(confirmar, "Fila eliminada", mensajeDeAcierto);
                    javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> inicio.dispose());
                    t.setRepeats(false);
                    t.start();
                }else {
                    mensaje(confirmar,"No se ha podido eliminar el patrocinio");
                }
        };
        ActionListener accion2 = e -> {
            inicio.dispose();
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));

        JButton cancelar = new JButton("Cancelar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        botones.add(cancelar);
        confirmar.addActionListener(accion);
        cancelar.addActionListener(accion2);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(mensaje, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
    }

    // Este metodo genera una ventana con las filas afectas por la fila que se va a eliminar en la tabla patrocinador
    public void ventanaConfirmarEliminacionPatrocinador(String patrocinadorNombre){
        JDialog inicio = new JDialog((Frame) null, "Eliminar", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(700, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel mensaje = new JPanel();
        JLabel texto = new JLabel("¿Estas seguro de que quieres eliminar " + patrocinadorNombre + "? Se eliminaran las siguientes filas relacionadas");
        mensaje.add(texto);
        JTable tabla = new JTable();
        JScrollPane subeYBaja = new JScrollPane(tabla);
        tabla.setModel(controladorPatrocinador.mostrarFilasAfectadasPorPatrocinadorEnPatrocinios(controladorPatrocinador.cualId(patrocinadorNombre)));
        JButton confirmar = new JButton("Confirmar");
        ActionListener accion = e -> {
            if (controladorPatrocinador.eliminarPatrocinador(controladorPatrocinador.cualId(patrocinadorNombre))) {
                refrescarTabla();
                patrocinadores.setModel(controladorPatrocinador.mostrarNombrePatrocinador());
                mensaje(confirmar, "Fila eliminada", mensajeDeAcierto);
                javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> inicio.dispose());
                t.setRepeats(false);
                t.start();
            }else {
                mensaje(confirmar,"No se ha podido eliminar el patrocinador");
            }

        };
        ActionListener accion2 = e -> {
            inicio.dispose();
        };

        JButton cancelar = new JButton("Cancelar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        botones.add(cancelar);
        confirmar.addActionListener(accion);
        cancelar.addActionListener(accion2);
        inicio.add(mensaje, BorderLayout.NORTH);
        inicio.add(subeYBaja, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
    }

// Ventana que muestra el nombre de los arbitro que hay en la base de datos para que el usuario elija el que desea eliminar
    public void ventanaEliminarArbitro(){
        JDialog inicio = new JDialog((Frame) null, "Eliminar Arbitro", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel contrasena = new JPanel();
        JLabel texto = new JLabel("Arbitro:");
        arbitros = new JComboBox<>();
        arbitros.setModel(controladorArbitro.mostrarNombreArbitro());
        arbitros.setPreferredSize(new  Dimension(100, 25));
        contrasena.add(texto);
        contrasena.add(arbitros);
        ActionListener accion = e -> {
            String arbitro = (String) arbitros.getSelectedItem();
            ventanaConfirmarEliminacionArbitro(arbitro);
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        arbitros.addActionListener(accion);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(contrasena, BorderLayout.CENTER);
        inicio.setVisible(true);
    }

    // Ventana que muestra el nombre de los equipos que hay en la base de datos para que el usuario elija el que desea eliminar
    public void ventanaEliminarEquipo(){
        JDialog inicio = new JDialog((Frame) null, "Eliminar Equipo", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaEquipos = new JPanel();
        JLabel texto = new JLabel("equipo:");
        equipos = new JComboBox<>();
        equipos.setModel(controladorEquipos.mostrarNombreEquipos());
        equipos.setPreferredSize(new  Dimension(100, 25));
        listaEquipos.add(texto);
        listaEquipos.add(equipos);
        ActionListener accion = e -> {
            String equipo = (String) equipos.getSelectedItem();
            ventanaConfirmarEliminacionEquipo(equipo);
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        equipos.addActionListener(accion);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaEquipos, BorderLayout.CENTER);
        inicio.setVisible(true);
    }

    // Ventana que muestra el nombre de los partidos que hay en la base de datos para que el usuario elija el que desea eliminar
    public void ventanaEliminarPartido(){
        JDialog inicio = new JDialog((Frame) null, "Eliminar Partido", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(580, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPartidos = new JPanel();
        JLabel texto = new JLabel("partido:");
        partidos = new JComboBox<>();
        partidos.setModel(new DefaultComboBoxModel<>(controladorPartidos.mostrarPartidosEliminarOModificar()));
        partidos.setPreferredSize(new  Dimension(280, 30));
        listaPartidos.add(texto);
        listaPartidos.add(partidos);
        ActionListener accion = e -> {
            PartidoItem seleccionado = (PartidoItem) partidos.getSelectedItem();

            if (seleccionado != null) {
                ventanaConfirmarEliminacionPartido(seleccionado.getPartido());
            }
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        partidos.addActionListener(accion);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPartidos, BorderLayout.CENTER);
        inicio.setVisible(true);
    }

    // Ventana que muestra el nombre de los equipos y patrocinios que hay en la base de datos para que el usuario elija el patrocinio que desea eliminar
    public void ventanaEliminarPatrocinio(){
        JDialog inicio = new JDialog((Frame) null, "Eliminar Patrocinio", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(500, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPatrocinios = new JPanel();
        JLabel texto = new JLabel("patrocinio:");
        patrocinios = new JComboBox<>();
        patrocinios.setModel(new DefaultComboBoxModel<>(controladorPatrocinios.mostrarPatrociniosNombre()));
        patrocinios.setPreferredSize(new  Dimension(150, 25));
        JButton confirmar = new JButton("Confirmar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        listaPatrocinios.add(texto);
        listaPatrocinios.add(patrocinios);
        ActionListener accion = e -> {
            PatrocinioItem patrocinio = (PatrocinioItem) patrocinios.getSelectedItem();
            ventanaConfirmarEliminacionPatrocinio(patrocinio);
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        confirmar.addActionListener(accion);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPatrocinios, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
    }

    // Ventana que muestra el nombre de los patrocinadores que hay en la base de datos para que el usuario elija el que desea eliminar
    public void ventanaEliminarPatrocinador(){
        JDialog inicio = new JDialog((Frame) null, "Eliminar Patrocinador", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(500, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPatrocinadores = new JPanel();
        JLabel texto = new JLabel("patrocinador:");
        patrocinadores = new JComboBox<>();
        patrocinadores.setModel(controladorPatrocinador.mostrarNombrePatrocinador());
        patrocinadores.setPreferredSize(new  Dimension(100, 25));
        JButton confirmar = new JButton("Confirmar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        listaPatrocinadores.add(texto);
        listaPatrocinadores.add(patrocinadores);
        ActionListener accion = e -> {
            String patrocinador = (String) patrocinadores.getSelectedItem();
            ventanaConfirmarEliminacionPatrocinador(patrocinador);
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        confirmar.addActionListener(accion);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPatrocinadores, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
    }

    // En esta ventana el usuario puede modificar los campos de la tabla arbitro de una fila seleccionada anteriormente
    public void ventanaConfirmarModificacionArbitro(Arbitro arbitro){
        JDialog frame = new JDialog((Frame) null, "Modificar Arbitro", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
        JButton boton = new JButton("Guardar");
        ActionListener guardado = e -> {
            String nombreCompleto = nombre.getText();
            Date fechaSeleccionada = fecha.getDate();
            String partidosArbitrados = partidos.getText();
            String anosExperiencia = anos.getText();

            nombreCompleto = comprobacion.mayusculasNombres(nombreCompleto);
            if (comprobacion.comprobarTexto(nombreCompleto)) {
                arbitro.setNombreCompleto(nombreCompleto);
                if (fechaSeleccionada != null) {
                    LocalDate fechaNacimientoParseada = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (comprobacion.comprobarFecha(fechaNacimientoParseada)) {
                        arbitro.setFecha_nacimiento(fechaNacimientoParseada);
                        if (comprobacion.comprobarNumero(partidosArbitrados, limitePartidosArbitrados)) {
                            arbitro.setPartidos_arbitrados(Integer.parseInt(partidosArbitrados));
                            if (comprobacion.comprobarNumero(anosExperiencia, limiteAñosExperiencia)) {
                                int anosExperienciaParseado = Integer.parseInt(anosExperiencia);
                                if (controladorArbitro.trabajaMenosQueVive(fechaNacimientoParseada, anosExperienciaParseado)) {
                                    if (controladorArbitro.partidosJugadosXAnos(arbitro.getPartidos_arbitrados(), anosExperienciaParseado)) {
                                        arbitro.setAnos_experiencia(anosExperienciaParseado);
                                        if (controladorArbitro.modificarArbitro(arbitro)) {
                                            refrescarTabla();
                                            arbitros.setModel(controladorArbitro.mostrarNombreArbitro());
                                            mensaje(boton,"Fila modificada correctamente", mensajeDeAcierto);

                                            javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> frame.dispose());
                                            t.setRepeats(false);
                                            t.start();

                                        } else {
                                            mensaje(boton,"ERROR, no se ha podido modificar el arbitro");
                                        }
                                    }else {
                                        mensaje(boton, "ERROR, no se puede arbitrar tantos partidos en tan poco tiempo (110 partidos por año)");
                                    }
                                }else {
                                    mensaje(boton, "ERROR, los años de experiencia tienen que ser menos que la edad del arbitro");
                                }

                            } else {
                                mensaje(boton,"ERROR, años de experiencia no validos. Debe de ser un numero entre 0 y " + limiteAñosExperiencia);
                            }

                        } else {
                            mensaje(boton,"ERROR, partidos arbitrados no valido. Debe de ser un numero entre 0 y " + limitePartidosArbitrados);
                        }
                    }else {
                        mensaje(boton,"ERROR, fecha de nacimiento no valida. Debe de tener mínimo 16 años y maximo 60");
                    }
                } else {
                    mensaje(boton,"ERROR, selecciona una fecha");
                }

            } else {
                mensaje(boton,"ERROR nombre no valido, Solo se valen letras y minimo 2");
            }
        };


        GridBagConstraints gbc = configurarConstraints(1, campos.length);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);
        boton.addActionListener(guardado);
        frame.add(panel);
        frame.setVisible(true);
    }

    // En esta ventana el usuario puede modificar los campos de la tabla equipos de una fila seleccionada anteriormente
    public void ventanaConfirmarModificacionEquipo(Equipos equipo){
        JDialog frame = new JDialog((Frame) null, "Modificar Equipo", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
            if (comprobacion.comprobarTexto(nombreEquipo)) {
                equipo.setNombre_equipo(nombreEquipo);
                if (comprobacion.comprobarNumero(partidosPerdidosValor, limitePartidosJugados)) {
                    equipo.setPartidos_perdidos(Integer.parseInt(partidosPerdidosValor));
                    if (comprobacion.comprobarNumero(partidosGanadosValor, limitePartidosJugados)) {
                        equipo.setPartidos_ganados(Integer.parseInt(partidosGanadosValor));
                        if (comprobacion.comprobarNumero(trofeosLigaValor, limiteTrofeosLiga)) {
                            equipo.setTrofeos_liga(Integer.parseInt(trofeosLigaValor));
                            if (controladorEquipos.modificarEquipo(equipo)) {
                                refrescarTabla();
                                equipos.setModel(controladorEquipos.mostrarNombreEquipos());
                                mensaje(boton,"El equipo ha sido modificado correctamente", mensajeDeAcierto);
                                javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> frame.dispose());
                                t.setRepeats(false);
                                t.start();
                            }else {
                                mensaje(boton,"No se ha podido modificar el equipo");
                            }
                        }else {
                            mensaje(boton,"ERROR trofeos liga, solo se valen numeros entre 0 y " + limiteTrofeosLiga);
                        }
                    }else {
                        mensaje(boton,"ERROR en partidos ganados, solo se valen numeros entre 0 y " + limitePartidosJugados);
                    }
                }else {
                    mensaje(boton,"ERROR en partidos perdidos, solo se valen numeros entre 0 y " + limitePartidosJugados);
                }
            }else {
                mensaje(boton,"ERROR nombre no valido, Solo se valen letras y minimo 2");
            }
        };
        boton.addActionListener(accion);
        frame.add(panel);
        frame.setVisible(true);
    }

    // En esta ventana el usuario puede modificar los campos de la tabla partidos de una fila seleccionada anteriormente
    public void ventanaConfirmarModificacionPartido(Partidos partido){
        JDialog frame = new JDialog((Frame) null, "Modificar Partido", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
        JButton boton = new JButton("Guardar");
        ActionListener accion = e -> {

            Date fechaSeleccionada = fecha.getDate();
            String horaPartido = hora.getText();
            String resultadoLocalPartido = resultadoLocal.getText();
            String resultadoVisitantePartido = resultadoVisitante.getText();

            String equipoLocalPartido = (String) cEquipoLocal.getSelectedItem();
            String equipoVisitantePartido = (String) cEquipoVisitante.getSelectedItem();
            String arbitro1Partido = (String) cArbitro1.getSelectedItem();
            String arbitro2Partido = (String) cArbitro2.getSelectedItem();

            if (fechaSeleccionada != null) {
                if (comprobacion.comprobarFechaPartido(fechaSeleccionada.toString())) {
                    partido.setFecha(fechaSeleccionada.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                    if (comprobacion.comprobarHora(horaPartido)) {
                        horaPartido = horaPartido + ":00";
                        partido.setHora(Time.valueOf(horaPartido));
                        if (comprobacion.comprobarNumero(resultadoLocalPartido, limiteResultadoEquipo)) {
                            partido.setResultado_local(Integer.parseInt(resultadoLocalPartido));

                            if (comprobacion.comprobarNumero(resultadoVisitantePartido, limiteResultadoEquipo)) {
                                partido.setResultado_visitante(Integer.parseInt(resultadoVisitantePartido));
                                if (!equipoLocalPartido.equals(equipoVisitantePartido)) {
                                    partido.setId_equipo_local(controladorEquipos.cualId(equipoLocalPartido));
                                    partido.setId_equipo_visitante(controladorEquipos.cualId(equipoVisitantePartido));
                                    if (!arbitro1Partido.equals(arbitro2Partido)) {
                                        partido.setArbitro1(controladorArbitro.cualId(arbitro1Partido));
                                        partido.setArbitro2(controladorArbitro.cualId(arbitro2Partido));
                                        if (controladorPartidos.modificarPartido(partido)) {
                                            refrescarTabla();
                                            partidos.setModel(
                                                    new DefaultComboBoxModel<>(
                                                            controladorPartidos.mostrarPartidosEliminarOModificar()
                                                    )
                                            );
                                            mensaje(boton,"Partido modificado correctamente", mensajeDeAcierto);
                                            javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> frame.dispose());
                                            t.setRepeats(false);
                                            t.start();
                                        } else {
                                            mensaje(boton,"ERROR, no se ha podido modificar el partido");
                                        }

                                    } else {
                                        mensaje(boton,"ERROR, los arbitros tienen que ser distintos");
                                    }

                                } else {
                                    mensaje(boton,"ERROR, los equipos tienen que ser distintos");
                                }

                            } else {
                                mensaje(boton,"ERROR, resultado visitante no valido, tiene que ser un numero entre 0 y 30");
                            }

                        } else {
                            mensaje(boton,"ERROR, resultado local no valido, tiene que ser un numero entre 0 y 30");
                        }

                    } else {
                        mensaje(boton,"ERROR, hora no valida, ejemplo: 16:12");
                    }
                }else {
                    mensaje(boton,"ERROR, la fecha tiene que ser posterior a 1892");
                }
            } else {
                mensaje(boton,"ERROR, selecciona una fecha");
            }
        };

        GridBagConstraints gbc = configurarConstraints(1, fila);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);
        boton.addActionListener(accion);
        frame.add(panel);
        frame.setVisible(true);
    }

    // En esta ventana el usuario puede modificar los campos de la tabla patrocinios seleccionando un patrocinador y un equipo existentes en sus tablas correspondientes de una fila seleccionada anteriormente
    public void ventanaConfirmarModificacionPatrocinio(Patrocinador patrocinador, Equipos equipo){
        JDialog frame = new JDialog((Frame) null, "Asignar Patrocinio", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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

            seleccionados[0] = (String) cPatrocinadores.getSelectedItem();
            seleccionados[1] = (String) cEquipos.getSelectedItem();
            ides[0] = controladorPatrocinios.localizarIdPatrocinador(seleccionados[0]);
            ides[1] = controladorPatrocinios.localizarIdEquipo(seleccionados[1]);
            Patrocinios patrocinio = new Patrocinios(ides[0], ides[1]);

            if (controladorPatrocinios.modificarPatrocinio(patrocinio, patrocinador.getId_patrocinador(), equipo.getId_equipo())) {
                refrescarTabla();
                patrocinios.setModel(
                        new DefaultComboBoxModel<>(
                                controladorPatrocinios.mostrarPatrociniosNombre()
                        )
                );
                mensaje(boton,"Fila modificada correctamente", mensajeDeAcierto);
                javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> frame.dispose());
                t.setRepeats(false);
                t.start();
            }else {
                mensaje(boton,"No se ha podido modificar la fila");
            }
        };
        boton.addActionListener(accion);
        frame.add(panel);
        frame.setVisible(true);
    }

    // En esta ventana el usuario puede modificar los campos de la tabla patrocinador de una fila seleccionada anteriormente
    public void ventanaConfirmarModificacionPatrocinador(Patrocinador patrocinador){
        JDialog frame = new JDialog((Frame) null, "Modificar Patrocinador", true);
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
            String nombrePatrocinador = nombre.getText();
            String telefonoPatrocinador = telefono.getText();
            String emailPatrocinador = email.getText();
            if (comprobacion.comprobarTexto(nombrePatrocinador)) {
                patrocinador.setNombre_patrocinador(nombrePatrocinador);
                if (comprobacion.comprobarTelefono(telefonoPatrocinador)) {
                    patrocinador.setTelefono(Integer.parseInt(telefonoPatrocinador));
                    if (comprobacion.comprobarEmail(emailPatrocinador)) {
                        patrocinador.setEmail(emailPatrocinador);
                        if (controladorPatrocinador.ExistePatrocinador(patrocinador)) {
                            mensaje(boton,"Este patrocinador ya existe");
                        }else {
                            if (controladorPatrocinador.modificarPatrocinador(patrocinador)) {
                                refrescarTabla();
                                patrocinadores.setModel(controladorPatrocinador.mostrarNombrePatrocinador());
                                mensaje(boton,"Fila modificada correctamente", mensajeDeAcierto);
                                javax.swing.Timer t = new javax.swing.Timer(tiempoDeCierre, es -> frame.dispose());
                                t.setRepeats(false);
                                t.start();
                            }else {
                                mensaje(boton,"No se ha podido modificar al patrocinador");
                            }
                        }
                    }else {
                        mensaje(boton,"ERROR, email no valido (Tiene que tener: texto@texto.extensionMax(3))");
                    }
                }else {
                    mensaje(boton,"ERROR, telefono no valido. Tiene que: Empezar por 6, 7 o 9 y tener 9 o 12 digitos");
                }
            }else {
                mensaje(boton,"ERROR nombre no valido, Solo se valen letras y minimo 2");
            }
        };
        boton.addActionListener(accion);
        frame.add(panel);
        frame.setVisible(true);
    }

    // En esta ventana el usuario selecciona el nombre del arbitro que desea modificar
    public void ventanaModificarArbitro(){
        JDialog inicio = new JDialog((Frame) null, "Modificar arbitro", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel contrasena = new JPanel();
        JLabel texto = new JLabel("Arbitro:");
        arbitros = new JComboBox<>();
        arbitros.setModel(controladorArbitro.mostrarNombreArbitro());
        arbitros.setPreferredSize(new  Dimension(100, 25));
        contrasena.add(texto);
        contrasena.add(arbitros);
        ActionListener accion = e -> {
            String arbitro = (String) arbitros.getSelectedItem();
            ventanaConfirmarModificacionArbitro(controladorArbitro.encontrarArbitro(controladorArbitro.cualId(arbitro)));
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        arbitros.addActionListener(accion);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(contrasena, BorderLayout.CENTER);
        inicio.setVisible(true);
    }

    // En esta ventana el usuario selecciona el nombre del equipo que desea modificar
    public void ventanaModificarEquipo(){
        JDialog inicio = new JDialog((Frame) null, "Modificar Equipo", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaEquipos = new JPanel();
        JLabel texto = new JLabel("equipo:");
        equipos = new JComboBox<>();
        equipos.setModel(controladorEquipos.mostrarNombreEquipos());
        equipos.setPreferredSize(new  Dimension(100, 25));
        listaEquipos.add(texto);
        listaEquipos.add(equipos);
        ActionListener accion = e -> {
            String equipo = (String) equipos.getSelectedItem();
            ventanaConfirmarModificacionEquipo(controladorEquipos.encontrarEquipo(controladorEquipos.cualId(equipo)));
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        equipos.addActionListener(accion);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaEquipos, BorderLayout.CENTER);
        inicio.setVisible(true);
    }

    // En esta ventana el usuario selecciona la fecha y los nombres de los equipos del partido que desea modificar
    public void ventanaModificarPartido(){
        JDialog inicio = new JDialog((Frame) null, "Modificar Partido", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(580, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPartidos = new JPanel();
        JLabel texto = new JLabel("partido:");
        partidos = new JComboBox<>();
        partidos.setModel(new DefaultComboBoxModel<>(controladorPartidos.mostrarPartidosEliminarOModificar()));
        partidos.setPreferredSize(new  Dimension(280, 30));
        listaPartidos.add(texto);
        listaPartidos.add(partidos);
        ActionListener accion = e -> {
            PartidoItem seleccionado = (PartidoItem) partidos.getSelectedItem();

            if (seleccionado != null) {
                ventanaConfirmarModificacionPartido(seleccionado.getPartido());
            }
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        partidos.addActionListener(accion);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPartidos, BorderLayout.CENTER);
        inicio.setVisible(true);
    }

    // En esta ventana el usuario selecciona el nombre del patrocinador y equipo que forman parte de un patrocinio que desea modificar
    public void ventanaModificarPatrocinio(){
        JDialog inicio = new JDialog((Frame) null, "Modificar Patrocinio", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(500, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPatrocinios = new JPanel();
        JLabel texto = new JLabel("patrocinios:");
        patrocinios = new JComboBox<>();
        patrocinios.setModel(new DefaultComboBoxModel<>(controladorPatrocinios.mostrarPatrociniosNombre()));
        patrocinios.setPreferredSize(new  Dimension(150, 25));
        JButton confirmar = new JButton("Confirmar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        listaPatrocinios.add(texto);
        listaPatrocinios.add(patrocinios);
        ActionListener accion = e -> {
            PatrocinioItem patrocinio = (PatrocinioItem) patrocinios.getSelectedItem();
            ventanaConfirmarModificacionPatrocinio(controladorPatrocinador.encontrarPatrocinador(patrocinio.getPatrocinio().getId_patrocinador()), controladorEquipos.encontrarEquipo(patrocinio.getPatrocinio().getId_equipo()));
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        confirmar.addActionListener(accion);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPatrocinios, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
    }

    // En esta ventana el usuario selecciona el nombre del patrocinador que desea modificar
    public void ventanaModificarPatrocinador(){
        JDialog inicio = new JDialog((Frame) null, "Modificar Patrocinador", true);
        inicio.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        inicio.setSize(500, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel listaPatrocinadores = new JPanel();
        JLabel texto = new JLabel("patrocinador:");
        patrocinadores = new JComboBox<>();
        patrocinadores.setModel(controladorPatrocinador.mostrarNombrePatrocinador());
        patrocinadores.setPreferredSize(new  Dimension(100, 25));
        JButton confirmar = new JButton("Confirmar");
        JPanel botones = new JPanel();
        botones.add(confirmar);
        listaPatrocinadores.add(texto);
        listaPatrocinadores.add(patrocinadores);
        ActionListener accion = e -> {
            String patrocinador = (String) patrocinadores.getSelectedItem();
            ventanaConfirmarModificacionPatrocinador(controladorPatrocinador.encontrarPatrocinador(controladorPatrocinador.cualId(patrocinador)));
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        confirmar.addActionListener(accion);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(listaPatrocinadores, BorderLayout.CENTER);
        inicio.add(botones, BorderLayout.SOUTH);
        inicio.setVisible(true);
    }

    // ventana donde el usuario puede cambiar su contraseña introduciendo la anterior y la nueva que quiere introducir
    public void ventanaCambioPassword() {
        JFrame frame = new JFrame("Cambiar contraseña");
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());

        JLabel username = new JLabel("Usuario:");
        JComboBox<String> usuarios = new JComboBox<>(comprobacion.mostrarUsuarios());
        usuarios.setSelectedItem("administrador");
        panel.add(username, configurarConstraints(0, 0));

        JLabel texto = new JLabel("Contraseña antigua:");
        javax.swing.JPasswordField contrasenaAntigua = new JPasswordField(15);
        panel.add(texto, configurarConstraints(0, 1));

        JPasswordField contrasenaNueva = new JPasswordField(15);
        panel.add(new JLabel("Contraseña nueva:"), configurarConstraints(0, 2));
        panel.add(usuarios, configurarConstraints(1, 0));
        panel.add(contrasenaNueva, configurarConstraints(1, 2));
        panel.add(contrasenaAntigua, configurarConstraints(1, 1));
        JButton confirmar = new JButton("Aceptar");
        ActionListener accion = e -> {
            String user = (String) usuarios.getSelectedItem();
            String password = new String(contrasenaAntigua.getPassword());
            String nueva = new String(contrasenaNueva.getPassword());

            if (comprobacion.cambiarContrasena(user, password, nueva)) {
                mensaje(confirmar, "La contraseña se ha cambiado correctamente");
                frame.dispose();
                ventanaDeLogeo();
            }else {
                mensaje(confirmar,"Contraseña Incorrecta", 500);
            }

        };

        GridBagConstraints gbcBoton = configurarConstraints(1, 3);
        gbcBoton.anchor = GridBagConstraints.CENTER;

        panel.add(confirmar, gbcBoton);

        frame.add(panel);
        frame.setVisible(true);
        confirmar.addActionListener(accion);
    }
// Ventana inicial donde introduce sus credenciales para entrar al programa
    public void ventanaDeLogeo(){
        JFrame inicio = new JFrame("Inicio de Sesión");
        inicio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicio.setSize(400, 250);
        inicio.setLayout(new BorderLayout());
        inicio.setLocationRelativeTo(null);
        JPanel contrasena = new JPanel();
        JLabel username = new JLabel("Usuario:");
        JComboBox<String> usuarios = new JComboBox<>(comprobacion.mostrarUsuarios());
        usuarios.setSelectedItem("administrador");
        JLabel texto = new JLabel("Contraseña");
        JPasswordField usuario = new JPasswordField();
        usuario.setPreferredSize(new  Dimension(100, 25));
        contrasena.add(username);
        contrasena.add(usuarios);
        contrasena.add(texto);
        contrasena.add(usuario);
        ActionListener accion = e -> {
            String user = (String) usuarios.getSelectedItem();
            String password = new String(usuario.getPassword());

            if (comprobacion.verificacionInicioSesion(user, password)) {
                paginaPrincipal();
                inicio.setVisible(false);
            }else {
                mensaje(usuario,"Contraseña Incorrecta", 500);
            }

        };
        ActionListener accion2 = e -> {
            ventanaCambioPassword();
            inicio.setVisible(false);
        };
        JPanel espacio = new JPanel();
        espacio.setPreferredSize(new Dimension(100, 80));
        JButton cambiar = new JButton("Cambiar contraseña");
        JButton iniciar = new JButton("Comenzar");
        JPanel cContrasena = new JPanel();
        cContrasena.add(cambiar);
        cContrasena.add(iniciar);
        iniciar.addActionListener(accion);
        usuario.addActionListener(accion);
        cambiar.addActionListener(accion2);
        inicio.add(espacio, BorderLayout.NORTH);
        inicio.add(contrasena, BorderLayout.CENTER);
        inicio.add(cContrasena, BorderLayout.SOUTH);
        inicio.setVisible(true);

    }
    public void refrescarTabla() {

        if (nTabla == tablaArbitro) {
            tabla.setModel(controladorArbitro.mostrarArbitro());
        }

        if (nTabla == tablaEquipos) {
            tabla.setModel(controladorEquipos.mostrarEquipos());
        }

        if (nTabla == tablaPartidos) {
            tabla.setModel(controladorPartidos.mostrarPartidos());
        }

        if (nTabla == tablaPatrocinador) {
            tabla.setModel(controladorPatrocinador.mostrarPatrocinador());
        }

        if (nTabla == tablaPatrocinios) {
            tabla.setModel(controladorPatrocinios.mostrarPatrocinios());
        }

        tabla.revalidate();
        tabla.repaint();
    }
    // Página principal del programa donde puede seleccionar la tabla que desea ver o administrar
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
        tabla = new JTable();
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
        tabla.setModel(controladorPartidos.mostrarPartidos());
        fondo.setVisible(true);
        ActionListener listaArbitro = e -> {
            nTabla = tablaArbitro;
            tabla.setModel(controladorArbitro.mostrarArbitro());
        };
        arbitros.addActionListener(listaArbitro);
        ActionListener listaequipos = e -> {
            nTabla = tablaEquipos;
            tabla.setModel(controladorEquipos.mostrarEquipos());
        };
        equipos.addActionListener(listaequipos);
        ActionListener listaPartidos = e -> {
            nTabla = tablaPartidos;
            tabla.setModel(controladorPartidos.mostrarPartidos());
        };
        partidos.addActionListener(listaPartidos);
        ActionListener listaPatrocinios = e -> {
            nTabla = tablaPatrocinios;
            tabla.setModel(controladorPatrocinios.mostrarPatrocinios());
        };
        patrocinios.addActionListener(listaPatrocinios);
        ActionListener listaPatrocinador = e -> {
            nTabla = tablaPatrocinador;
            tabla.setModel(controladorPatrocinador.mostrarPatrocinador());
        };
        patrocinadores.addActionListener(listaPatrocinador);

        ActionListener anade = e -> {
            if (nTabla == tablaArbitro) {
                ventanaArbitro();
            }
            if (nTabla == tablaEquipos) {
                ventanaEquipos();
            }
            if (nTabla == tablaPartidos) {
                ventanaPartidos();
            }
            if (nTabla == tablaPatrocinador) {

                ventanaPatrocinador();
            }
            if (nTabla == tablaPatrocinios) {
                ventanaPatrocinios();
            }

        };
        anadir.addActionListener(anade);
        ActionListener elimina = e -> {
            if (nTabla == tablaArbitro) {
                ventanaEliminarArbitro();
            }
            if (nTabla == tablaEquipos) {
                ventanaEliminarEquipo();
            }
            if (nTabla == tablaPartidos) {
                ventanaEliminarPartido();
            }
            if (nTabla == tablaPatrocinador) {
                ventanaEliminarPatrocinador();
            }
            if (nTabla == tablaPatrocinios) {
                ventanaEliminarPatrocinio();
            }

        };
        eliminar.addActionListener(elimina);
        ActionListener modifica = e -> {
            if (nTabla == tablaArbitro) {
                ventanaModificarArbitro();
            }
            if (nTabla == tablaEquipos) {
                ventanaModificarEquipo();
            }
            if (nTabla == tablaPartidos) {
                ventanaModificarPartido();
            }
            if (nTabla == tablaPatrocinador) {
                ventanaModificarPatrocinador();
            }
            if (nTabla == tablaPatrocinios) {
                ventanaModificarPatrocinio();
            }
        };
        modificar.addActionListener(modifica);
        ActionListener imprimirDocumento = e -> {
            if (nTabla == tablaArbitro) {
                if (controladorArbitro.imprimirArbitros()) {
                    mensaje(documento,"Se ha creado el documento arbitros correctamente", mensajeDeAcierto);
                }
            }
            if (nTabla == tablaEquipos) {
                if (controladorEquipos.imprimirEquipos()) {
                    mensaje(documento,"Se ha creado el documento equipos correctamente", mensajeDeAcierto);
                }
            }
            if (nTabla == tablaPartidos) {
                if (controladorPartidos.imprimirPartidos()) {
                    mensaje(documento,"Se ha creado el documento partidos correctamente", mensajeDeAcierto);
                }
            }
            if (nTabla == tablaPatrocinador) {
                if (controladorPatrocinador.imprimirPatrocinadores()) {
                    mensaje(documento,"Se ha creado el documento patrocinadores correctamente", mensajeDeAcierto);
                }
            }
            if (nTabla == tablaPatrocinios) {
                if (controladorPatrocinios.imprimirPatrocinios()) {
                    mensaje(documento,"Se ha creado el documento patrocinios correctamente", mensajeDeAcierto);
                }
            }
        };
        documento.addActionListener(imprimirDocumento);
    }
}
