package com.itson.equipo2.battleship_cliente.view;

import com.itson.equipo2.battleship_cliente.models.CeldaModel;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.pattern.factory.ViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.mediator.GameMediator;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.pattern.observer.PartidaObserver;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.DisparoDTO;
import mx.itson.equipo_2.common.enums.EstadoCelda;
import mx.itson.equipo_2.common.enums.EstadoNave;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;
import static mx.itson.equipo_2.common.enums.ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class DispararView extends javax.swing.JPanel implements ViewFactory, PartidaObserver {

    private JButton[][] botonesPropio = new JButton[10][10];
    private JButton[][] botonesEnemigo = new JButton[10][10];

    private CoordenadaDTO coordSeleccionada;
    private JButton ultimaSeleccionada;
    private JugadorModel jugador;
    private Timer timerNotificacion;
    private GameMediator mediator;

    private TableroModel miTablero;
    private TableroModel tableroEnemigo;

    private PartidaModel partidaModel; // refeerencia local
    private MarcadorView marcadorView;
    
    private final Color COLOR_AGUA_IMPACTO = new Color(175, 238, 238);
    private final Color COLOR_DESTRUIDO = Color.BLACK;                 
    private final Color COLOR_DANIADO = Color.DARK_GRAY;               
    private final Color COLOR_MAR_OCULTO = new Color(50, 70, 100);     

    public DispararView() {
        initComponents();
        crearCeldas();

    }

    public void setModels(PartidaModel partidaModel, TableroModel miTablero, TableroModel tableroEnemigo) {
        this.partidaModel = partidaModel;


        if (this.partidaModel != null) {
            this.partidaModel.addObserver(this);
            System.out.println("DispararView: Suscrito a actualizaciones.");
            

            onChange(this.partidaModel); 
        }
    }

    @Override
    public void onChange(PartidaModel model) {
        this.partidaModel = model;

        actualizarLabelTurno();
        actualizarLabelTimer(model.getSegundosRestantes());

        TableroModel miTableroActual = model.getTableroPropio();
        if (miTableroActual != null) {
            Color colorJugador = Color.GRAY;
            if (model.getYo() != null && model.getYo().getColor() != null) {
                colorJugador = model.getYo().getColor().getColor();
            }
            repintarPropio(miTableroActual, colorJugador);
        }

        if (model.getEnemigo() != null) {
            TableroModel tableroEnemigoActual = model.getEnemigo().getTablero();
            if (tableroEnemigoActual != null) {
                repintarEnemigo(tableroEnemigoActual);
            }
        }
        
        actualizarDatosMarcador();

        revalidate();
        repaint();

        if (model.getEstado() == EstadoPartida.FINALIZADA) {
            if (btnDisparar.isEnabled()) { 
                String mensaje = "¡Partida terminada! Ganador: " + model.getTurnoDe();
                JOptionPane.showMessageDialog(this, mensaje);
                btnDisparar.setEnabled(false);
                btnRendirse.setEnabled(false);
            }
        }
    }

    /**
     * Método auxiliar que conecta el Modelo con la Vista del Marcador. Extrae
     * los mapas de estado y se los pasa a la ventana.
     */
    private void actualizarDatosMarcador() {
        if (marcadorView != null && partidaModel != null) {
            Map<TipoNave, List<EstadoNave>> misNaves = partidaModel.getEstadoMisNaves();
            Map<TipoNave, List<EstadoNave>> navesEnemigas = partidaModel.getEstadoNavesEnemigas();

            marcadorView.actualizarFlotaPropia(misNaves);
            marcadorView.actualizarFlotaEnemiga(navesEnemigas);
        }
    }

    private void actualizarLabelTurno() {
        if (partidaModel == null || partidaModel.getTurnoDe() == null || partidaModel.getYo() == null) {
            lblTurno.setText("Cargando partida...");
            return;
        }
        boolean esMiTurno = partidaModel.getTurnoDe().equals(partidaModel.getYo().getId());
        if (esMiTurno) {
            lblTurno.setText("¡ES TU TURNO!");
            lblTurno.setForeground(Color.GREEN);
            btnDisparar.setEnabled(true);
        } else {
            lblTurno.setText("Turno del enemigo...");
            lblTurno.setForeground(Color.RED);
            btnDisparar.setEnabled(false);
        }
    }

    private void actualizarLabelTimer(Integer segundos) {
        if (segundos != null) {
            lblTimer.setText(segundos + "s");
            if (segundos <= 10) {
                lblTimer.setForeground(Color.ORANGE);
            } else {
                lblTimer.setForeground(Color.BLACK);
            }
        } else {
            lblTimer.setText(30 + "s");
            lblTimer.setForeground(Color.BLACK);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTableroEnemigo = new JPanel();
        btnRendirse = new JButton();
        btnDisparar = new JButton();
        panelTableroPropio = new JPanel();
        lblTurno = new JLabel();
        lblTimer = new JLabel();
        btnMarcador = new JButton();
        lblResultado = new JLabel();

        setBackground(new Color(83, 111, 137));
        setMaximumSize(new Dimension(1280, 720));
        setMinimumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);

        panelTableroEnemigo.setBackground(new Color(82, 113, 177));
        panelTableroEnemigo.setMaximumSize(new Dimension(600, 600));
        panelTableroEnemigo.setMinimumSize(new Dimension(600, 600));
        panelTableroEnemigo.setLayout(new GridLayout(1, 0));
        panelTableroEnemigo.setLayout(new GridLayout(10, 10));
        add(panelTableroEnemigo);
        panelTableroEnemigo.setBounds(615, 56, 600, 600);

        btnRendirse.setBackground(new Color(75, 75, 75));
        btnRendirse.setFont(new Font("Segoe UI Black", 0, 18)); // NOI18N
        btnRendirse.setForeground(new Color(255, 255, 255));
        btnRendirse.setText("RENDIRSE");
        btnRendirse.setBorder(null);
        btnRendirse.setFocusPainted(false);
        btnRendirse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnRendirseActionPerformed(evt);
            }
        });
        add(btnRendirse);
        btnRendirse.setBounds(60, 650, 156, 41);

        btnDisparar.setBackground(new Color(75, 75, 75));
        btnDisparar.setFont(new Font("Segoe UI Black", 0, 18)); // NOI18N
        btnDisparar.setForeground(new Color(255, 255, 255));
        btnDisparar.setText("DISPARAR");
        btnDisparar.setBorder(null);
        btnDisparar.setFocusPainted(false);
        btnDisparar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDispararActionPerformed(evt);
            }
        });
        add(btnDisparar);
        btnDisparar.setBounds(260, 650, 156, 41);

        panelTableroPropio.setBackground(new Color(82, 113, 177));
        panelTableroPropio.setMaximumSize(new Dimension(250, 250));
        panelTableroPropio.setMinimumSize(new Dimension(250, 250));
        panelTableroPropio.setLayout(new GridLayout(10, 10));
        add(panelTableroPropio);
        panelTableroPropio.setBounds(130, 50, 250, 250);

        lblTurno.setFont(new Font("Segoe UI", 0, 24)); // NOI18N
        lblTurno.setHorizontalAlignment(SwingConstants.CENTER);
        lblTurno.setText(" ");
        add(lblTurno);
        lblTurno.setBounds(80, 320, 360, 50);

        lblTimer.setFont(new Font("Segoe UI", 0, 24)); // NOI18N
        lblTimer.setHorizontalAlignment(SwingConstants.CENTER);
        lblTimer.setText(" ");
        add(lblTimer);
        lblTimer.setBounds(130, 400, 250, 30);

        btnMarcador.setBackground(new Color(75, 75, 75));
        btnMarcador.setFont(new Font("Segoe UI Black", 0, 18)); // NOI18N
        btnMarcador.setForeground(new Color(255, 255, 255));
        btnMarcador.setText("MARCADOR");
        btnMarcador.setBorder(null);
        btnMarcador.setFocusPainted(false);
        btnMarcador.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnMarcadorActionPerformed(evt);
            }
        });
        add(btnMarcador);
        btnMarcador.setBounds(180, 480, 156, 41);

        lblResultado.setFont(new Font("Segoe UI", 0, 24)); // NOI18N
        lblResultado.setHorizontalAlignment(SwingConstants.CENTER);
        lblResultado.setText(" ");
        add(lblResultado);
        lblResultado.setBounds(100, 550, 330, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRendirseActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnRendirseActionPerformed
        int opcion = JOptionPane.showConfirmDialog(
            this, 
            "¿Estás seguro que deseas rendirte?", 
            "Confirmación", 
            JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            if (mediator != null) {
                mediator.abandonarPartida();
            }
        }
    }//GEN-LAST:event_btnRendirseActionPerformed

    private void btnDispararActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDispararActionPerformed
        if (coordSeleccionada == null) {
            mostrarError("Debes seleccionar una casilla para disparar.");
            return;
        }
        
        if (tableroEnemigo != null) {
            CeldaModel celda = tableroEnemigo.getCelda(coordSeleccionada.getFila(), coordSeleccionada.getColumna());
            if (celda.getEstado() == EstadoCelda.DISPARADA) {
                mostrarError("Esta casilla ya fue disparada.");
                return;
            }
        }

        if (mediator != null) {
            mediator.disparar(coordSeleccionada.getFila(), coordSeleccionada.getColumna());
        }
        
        this.coordSeleccionada = null;
        if (this.ultimaSeleccionada != null) {
            this.ultimaSeleccionada.setBorder(new LineBorder(Color.BLACK, 1));
            this.ultimaSeleccionada = null;
        }


    }//GEN-LAST:event_btnDispararActionPerformed

    private void btnMarcadorActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnMarcadorActionPerformed

        if (marcadorView == null) {
            JFrame frame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
            marcadorView = new MarcadorView(frame);
        }
        
        actualizarDatosMarcador();

        marcadorView.setVisible(true);
    }//GEN-LAST:event_btnMarcadorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnDisparar;
    private JButton btnMarcador;
    private JButton btnRendirse;
    private JLabel lblResultado;
    private JLabel lblTimer;
    private JLabel lblTurno;
    private JPanel panelTableroEnemigo;
    private JPanel panelTableroPropio;
    // End of variables declaration//GEN-END:variables

    private void crearCeldas() {
        botonesEnemigo = new JButton[10][10];
        botonesPropio = new JButton[10][10];
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                JButton celdaEnemigo = new JButton();
                celdaEnemigo.setBackground(COLOR_MAR_OCULTO);
                celdaEnemigo.setBorder(new LineBorder(Color.BLACK, 1));
                final int f = fila;
                final int c = col;
                celdaEnemigo.addActionListener(e -> {
                    if (tableroEnemigo != null) {
                        CeldaModel celdaModelo = tableroEnemigo.getCelda(f, c);
                        if (celdaModelo != null && celdaModelo.getEstado() == EstadoCelda.DISPARADA) {
                            mostrarError("Ya has disparado en esta casilla.");
                            return;
                        }
                    }
                    if (ultimaSeleccionada != null) {
                        ultimaSeleccionada.setBorder(new LineBorder(Color.BLACK, 1));
                    }
                    ultimaSeleccionada = celdaEnemigo;
                    celdaEnemigo.setBorder(new LineBorder(Color.GRAY, 3));
                    coordSeleccionada = new CoordenadaDTO(f, c);
                });
                panelTableroEnemigo.add(celdaEnemigo);
                botonesEnemigo[fila][col] = celdaEnemigo;
                JButton celdaPropio = new JButton();
                celdaPropio.setBackground(COLOR_MAR_OCULTO);
                celdaPropio.setBorder(new LineBorder(Color.BLACK, 1));
                panelTableroPropio.add(celdaPropio);
                botonesPropio[fila][col] = celdaPropio;
            }
        }
    }

    @Override
    public JPanel crear(ViewController control) {
        return this;
    }

    public void mostrarTableroPropio(TableroModel tablero) {
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                CeldaModel celda = tablero.getCelda(fila, col);

                if (celda.tieneNave()) {
                    botonesPropio[fila][col].setBackground(Color.DARK_GRAY);
                }
            }
        }
    }

    public void actualizarTimer(int segundosRestantes, String jugador) {
        lblTurno.setText("Turno de " + jugador + " - Tiempo: " + segundosRestantes + "s");
    }

    public void setJugador(JugadorModel jugador) {
        this.jugador = jugador;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.WARNING_MESSAGE);
    }

    public GameMediator getMediator() {
        return mediator;
    }

    public void setMediator(GameMediator mediator) {
        this.mediator = mediator;
    }

    public void setTableros(TableroModel miTablero, TableroModel tableroEnemigo) {
        this.miTablero = miTablero;
        this.tableroEnemigo = tableroEnemigo;

        mostrarTableroPropio(miTablero);
    }

   public void mostrarNotificacion(ResultadoDisparo resultado) {
        Color color;
        switch (resultado) {
            case AGUA -> color = Color.CYAN; // Texto azul claro
            case IMPACTO_SIN_HUNDIMIENTO -> color = Color.ORANGE;
            case IMPACTO_CON_HUNDIMIENTO -> color = Color.RED; // Texto rojo para la alerta
            default -> throw new AssertionError();
        }
        lblResultado.setText(resultado.getMensaje());
        lblResultado.setForeground(color);
        lblResultado.setVisible(true);

        if (timerNotificacion != null && timerNotificacion.isRunning()) {
            timerNotificacion.stop();
        }
        timerNotificacion = new javax.swing.Timer(2000, e -> {
            lblResultado.setText("");
            lblResultado.setBackground(new Color(0, 0, 0, 0));
            lblResultado.setVisible(false);
        });
        timerNotificacion.setRepeats(false);
        timerNotificacion.start();
    }

    public JLabel getLblResultado() {
        return lblResultado;
    }

    public void setLblResultado(JLabel lblResultado) {
        this.lblResultado = lblResultado;
    }

    private void repintarPropio(TableroModel tablero, Color colorUsuario) {
        if (tablero == null) return;
        
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {

                CeldaModel celda = tablero.getCelda(fila, col);
                JButton boton = botonesPropio[fila][col];

                if (celda.getEstado() == EstadoCelda.DISPARADA) {
                    if (celda.tieneNave()) {
                        boton.setBackground(COLOR_DESTRUIDO);
                    } else {
                        boton.setBackground(COLOR_AGUA_IMPACTO);
                    }
                } 
                
                else if (celda.tieneNave()) {
                    boton.setBackground(colorUsuario); 
                } 
                
                else {
                    boton.setBackground(COLOR_MAR_OCULTO); 
                }
            }
        }
    }

    public void repintarEnemigo(TableroModel tablero) {
        if (tablero == null) return;
        
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {

                CeldaModel celda = tablero.getCelda(fila, col);
                JButton boton = botonesEnemigo[fila][col];

                if (celda.getEstado() == EstadoCelda.NO_DISPARADA) {
                    boton.setBackground(COLOR_MAR_OCULTO);
                    boton.setEnabled(true);
                    continue;
                }

                EstadoNave estadoNave = celda.getEstadoNave();
                boton.setEnabled(false);

                if (estadoNave == null) {
                    boton.setBackground(COLOR_AGUA_IMPACTO);
                } else {
                    switch (estadoNave) {
                        case AVERIADO -> boton.setBackground(COLOR_DANIADO); 
                        case HUNDIDO -> boton.setBackground(COLOR_DESTRUIDO);
                        default -> boton.setBackground(Color.PINK);
                    }
                }
            }
        }
    }
}
