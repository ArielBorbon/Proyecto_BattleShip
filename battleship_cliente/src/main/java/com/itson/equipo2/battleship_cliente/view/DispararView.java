package com.itson.equipo2.battleship_cliente.view;

import com.itson.equipo2.battleship_cliente.models.CeldaModel;
import com.itson.equipo2.battleship_cliente.models.JugadorModel;
import com.itson.equipo2.battleship_cliente.models.PartidaModel;
import com.itson.equipo2.battleship_cliente.models.TableroModel;
import com.itson.equipo2.battleship_cliente.pattern.factory.ViewFactory;
import com.itson.equipo2.battleship_cliente.pattern.mediator.GameMediator;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import com.itson.equipo2.battleship_cliente.pattern.observer.PartidaObserver;
import com.itson.equipo2.battleship_cliente.pattern.observer.TableroObserver;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import mx.itson.equipo_2.common.dto.CoordenadaDTO;
import mx.itson.equipo_2.common.dto.DisparoDTO;
import mx.itson.equipo_2.common.enums.EstadoCelda;
import mx.itson.equipo_2.common.enums.EstadoPartida;
import mx.itson.equipo_2.common.enums.ResultadoDisparo;

/**
 *
 * @author Ariel Eduardo Borbon Izaguirre 00000252116 Sebastián Bórquez Huerta
 * 00000252115 Alberto Jiménez García 00000252595 José Eduardo Aguilar García
 * 00000252049 José Luis Islas Molina 00000252574
 */
public class DispararView extends javax.swing.JPanel implements ViewFactory, TableroObserver, PartidaObserver {

    private JButton[][] botonesPropio = new JButton[10][10];
    private JButton[][] botonesEnemigo = new JButton[10][10];

    private CoordenadaDTO coordSeleccionada;
    private JButton ultimaSeleccionada;
    private Color colorOriginalUltima;

    private TableroModel miTablero;
    private TableroModel tableroEnemigo;

    private PartidaModel partidaModel; // refeerencia local

    private Timer timerNotificacion;

    private JugadorModel jugador;
    private GameMediator mediator;

    /**
     * Creates new form DispararView
     */
    // public DispararView() {
    //  initComponents();
    //   crearCeldas();
    //}
    public DispararView() {
        initComponents();
        crearCeldas();
//        
//        // Conectar esta vista a los modelos (Singleton)
//        try {
//            this.partidaModel = PartidaModel.getInstance();
//            this.partidaModel.addObserver(this);            //PROBBLEMA
//            
//            // Asignar tableros
//            this.miTablero = partidaModel.getEnemigo().getTablero();
//            this.tableroEnemigo = partidaModel.getEnemigo().getTablero();
//            
//            if (this.miTablero != null) this.miTablero.addObserver(this);
//            if (this.tableroEnemigo != null) this.tableroEnemigo.addObserver(this);
//
//            System.out.println("DispararView conectada y observando modelos.");
//            actualizarLabelTurno(); // Actualizar estado inicial
//            
//        } catch (Exception e) {
//            System.err.println("Error fatal al conectar DispararView: " + e.getMessage());
//            e.printStackTrace();
//            mostrarError("Error al iniciar la vista de juego.");
//        }
    }

    public void setModels(PartidaModel partidaModel, TableroModel miTablero, TableroModel tableroEnemigo) {
        this.partidaModel = partidaModel;
        this.miTablero = miTablero;
        this.tableroEnemigo = tableroEnemigo;

        // Registrar esta VISTA como observadora de los MODELOS
        this.partidaModel.addObserver(this);
        this.miTablero.addObserver(this);
        this.tableroEnemigo.addObserver(this);

        System.out.println("DispararView: Modelos inyectados y observando.");
        actualizarLabelTurno(); // Actualizar estado inicial

        // Mostrar el tablero propio mockeado
        mostrarTableroPropio(this.miTablero);

    

    //////////////////////////////////////////////////
    }
    
    
    
    // --- MÉTODO CLAVE DEL OBSERVADOR DE PARTIDA ---
    @Override
    public void onChange(PartidaModel model) {
        System.out.println("Observer notificado: Repintando vista.");
        this.partidaModel = model; // Asegurarse de tener el modelo más reciente

        // Actualizar el label de turno
        actualizarLabelTurno();
        actualizarLabelTimer(model.getSegundosRestantes()); // <-- AÑADIR

        // Repintar ambos tableros
        repaint();

        if (model.getEstado() == EstadoPartida.FINALIZADA) {
            JOptionPane.showMessageDialog(this, "¡Partida terminada! Ganador: " + model.getTurnoDe());
            btnDisparar.setEnabled(false);
            btnRendirse.setEnabled(false);
        }
    }

    // --- NUEVO MÉTODO ---
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
            // Cambiar color si queda poco tiempo
            if (segundos <= 10) {
                lblTimer.setForeground(Color.ORANGE);
            } else {
                lblTimer.setForeground(Color.BLACK); // O el color que prefieras
            }
        } else {
            // Estado inicial antes de que llegue el primer tick
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
        lblTurno.setText("...");
        add(lblTurno);
        lblTurno.setBounds(100, 320, 360, 50);

        lblTimer.setFont(new Font("Segoe UI", 0, 24)); // NOI18N
        lblTimer.setHorizontalAlignment(SwingConstants.CENTER);
        lblTimer.setText("...");
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
        lblResultado.setText("...");
        add(lblResultado);
        lblResultado.setBounds(100, 550, 330, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRendirseActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnRendirseActionPerformed
        int opcion = JOptionPane.showConfirmDialog(this, "¿Estás seguro que deseas rendirte?", "Confirmación", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            System.out.println("El jugador se rindió");
        }
    }//GEN-LAST:event_btnRendirseActionPerformed

    private void btnDispararActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDispararActionPerformed
        if (coordSeleccionada == null) {
            mostrarError("Debes seleccionar una casilla para disparar.");
            return;
        }
        if (mediator != null) {
            mediator.disparar(coordSeleccionada.getColumna(), coordSeleccionada.getFila());
            System.out.println("1");
        }

        // --- INICIO DE LA CORRECCIÓN ---
        // Una vez que el disparo es enviado, reseteamos la selección
        // para que no se pueda volver a presionar "Disparar" con la misma coordenada.
        // 1. Limpiar la coordenada guardada
        this.coordSeleccionada = null;

        // 2. Limpiar la referencia al botón y quitarle el borde
        if (this.ultimaSeleccionada != null) {
            // Restaurar el borde original (negro)
            this.ultimaSeleccionada.setBorder(new LineBorder(Color.BLACK, 1));
            this.ultimaSeleccionada = null;
        }
        // --- FIN DE LA CORRECCIÓN ---


    }//GEN-LAST:event_btnDispararActionPerformed

    private void btnMarcadorActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnMarcadorActionPerformed
        // TODO add your handling code here:
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
                celdaEnemigo.setBackground(new Color(50, 70, 100));
                celdaEnemigo.setBorder(new LineBorder(Color.BLACK, 1));

                final int f = fila;
                final int c = col;

                celdaEnemigo.addActionListener(e -> {

                    // --- INICIO DE CORRECCIÓN 1 ---
                    // 1. Validar si la celda ya fue disparada ANTES de seleccionarla
                    if (tableroEnemigo != null) {
                        CeldaModel celdaModelo = tableroEnemigo.getCelda(f, c);
                        if (celdaModelo != null && celdaModelo.getEstado() == EstadoCelda.DISPARADA) {
                            mostrarError("Ya has disparado en esta casilla.");
                            return; // No permitir seleccionar
                        }
                    }
                    // --- FIN DE CORRECCIÓN 1 ---

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
                celdaPropio.setEnabled(false);
                celdaPropio.setBackground(new Color(50, 70, 100));
                celdaPropio.setBorder(new LineBorder(Color.BLACK, 1));

                panelTableroPropio.add(celdaPropio);
                botonesPropio[fila][col] = celdaPropio;
            }
        }
    }

    public void actualizarCeldaEnemigo(DisparoDTO dto) {

        if (dto.getResultado() == ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO) {

            for (CoordenadaDTO coord : dto.getCoordenadasBarcoHundido()) {
                JButton boton = botonesEnemigo[coord.getFila()][coord.getColumna()];
                boton.setBackground(Color.RED);
                boton.setEnabled(false); // <-- CORRECCIÓN 1.A: Deshabilitar botón

            }

        } else {

            CoordenadaDTO c = dto.getCoordenada();
            JButton boton = botonesEnemigo[c.getFila()][c.getColumna()];
            Color nuevoColor = switch (dto.getResultado()) {
                case AGUA ->
                    Color.BLUE;
                case IMPACTO_SIN_HUNDIMIENTO ->
                    Color.ORANGE;
                default ->
                    boton.getBackground(); //no deberia pasar pero pues por si acaso
            };
            boton.setBackground(nuevoColor);
            boton.setEnabled(false); // <-- CORRECCIÓN 1.A: Deshabilitar botón
        }
    }

    public void actualizarCeldaPropia(DisparoDTO dto) {

        if (dto.getResultado() == ResultadoDisparo.IMPACTO_CON_HUNDIMIENTO) {

            for (CoordenadaDTO coord : dto.getCoordenadasBarcoHundido()) {
                JButton boton = botonesPropio[coord.getFila()][coord.getColumna()];
                boton.setBackground(Color.BLACK);
                // boton.setEnabled(false);
            }

        } else {
            CoordenadaDTO c = dto.getCoordenada();
            JButton boton = botonesPropio[c.getFila()][c.getColumna()];
            switch (dto.getResultado()) {
                case AGUA ->
                    boton.setBackground(Color.CYAN);
                case IMPACTO_SIN_HUNDIMIENTO ->
                    boton.setBackground(Color.MAGENTA);

            }
            // boton.setEnabled(false);
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

                if (celda.isTieneNave()) {
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

// --- MÉTODO CLAVE DEL OBSERVADOR DE TABLERO ---
    @Override
    public void onDisparo(TableroModel tableroAfectado, DisparoDTO disparo) {
        // La lógica del handler ya actualizó el modelo.
        // El handler llamó a tablero.actualizarCelda(), que llamó a this.onDisparo

        // Ahora, actualizamos la VISTA (los botones)
        if (tableroAfectado == this.tableroEnemigo) {
            System.out.println("Repintando celda enemiga");
            actualizarCeldaEnemigo(disparo);

            String msg = "Resultado: " + disparo.getResultado();
            Color color = Color.WHITE;
            Color fondo = new Color(0, 0, 0, 150); // Negro semitransparente

            if (disparo.getResultado() == ResultadoDisparo.AGUA) {
                msg = "¡AGUA! Fallaste el tiro.";
                fondo = new Color(0, 0, 255, 150);
            } else {
                msg = "¡IMPACTO! Diste en el blanco.";
                fondo = new Color(255, 0, 0, 150);
            }

            mostrarNotificacion(msg, color, fondo);
//            JOptionPane.showMessageDialog(this, "Resultado del disparo: " + disparo.getResultado());
        } else if (tableroAfectado == this.miTablero) {
            System.out.println("Repintando celda propia");
            actualizarCeldaPropia(disparo);
            mostrarNotificacion("¡Te han disparado!", Color.YELLOW, Color.RED);
        }
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

    public void mostrarNotificacion(String mensaje, Color colorTexto, Color colorFondo) {
        // 1. Configurar el mensaje
        lblResultado.setText(mensaje);
        lblResultado.setForeground(colorTexto);
        lblResultado.setBackground(colorFondo);
        lblResultado.setVisible(true);

        // 2. Si ya había un timer corriendo, lo reiniciamos
        if (timerNotificacion != null && timerNotificacion.isRunning()) {
            timerNotificacion.stop();
        }

        // 3. Crear un timer para borrar el mensaje en 3 segundos (3000 ms)
        timerNotificacion = new javax.swing.Timer(3000, e -> {
            lblResultado.setText("");
            lblResultado.setBackground(new Color(0, 0, 0, 0)); // Hacer transparente
            lblResultado.setVisible(false);
        });

        timerNotificacion.setRepeats(false); // Solo se ejecuta una vez
        timerNotificacion.start();
    }

}
