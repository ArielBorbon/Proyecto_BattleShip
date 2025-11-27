/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import mx.itson.equipo_2.common.enums.EstadoNave;
import mx.itson.equipo_2.common.enums.TipoNave;

/**
 *
 * @author sonic
 */
public class MarcadorView extends JDialog {

    private final JPanel panelContenidoPropio;
    private final JPanel panelContenidoEnemigo;

    private final Color COLOR_FONDO = new Color(83, 111, 137);
    private final Color COLOR_CELDA_NAVE = Color.LIGHT_GRAY;
    private final Color COLOR_BORDE_CELDA = Color.GRAY;
    private final Color COLOR_SEPARADOR = Color.WHITE;

    // Colores de estado (Bolitas)
    private final Color COLOR_INTACTO = Color.GREEN;
    private final Color COLOR_AVERIA = Color.YELLOW;
    private final Color COLOR_HUNDIDO = Color.RED;

    /**
     * Constructor de la ventana MarcadorView.
     * Inicializa la interfaz grafica con paneles para mostrar las flotas propia y enemiga,
     * incluyendo títulos, paneles de contenido y un botón para volver a la partida.
     *
     * @param parent El JFrame padre sobre el cual se muestra esta ventana.
     */
    public MarcadorView(JFrame parent) {
        super(parent, "Marcador de Flota", false);
        setSize(1100, 600);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_FONDO);
        setContentPane(mainPanel);

        // TÍTULOS
        JPanel panelTitulos = new JPanel(new GridLayout(1, 2));
        panelTitulos.setBackground(COLOR_FONDO);
        panelTitulos.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        panelTitulos.add(crearTitulo("Flota Propia"));
        panelTitulos.add(crearTitulo("Flota Enemiga"));
        mainPanel.add(panelTitulos, BorderLayout.NORTH);

        // PANELES DE NAVES 
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setBackground(COLOR_FONDO);

        panelContenidoPropio = crearPanelFlota();
        panelContenidoEnemigo = crearPanelFlota();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.49;
        centerContainer.add(crearScroll(panelContenidoPropio), gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.49;
        gbc.insets = new Insets(0, 0, 0, 0);
        centerContainer.add(crearScroll(panelContenidoEnemigo), gbc);

        mainPanel.add(centerContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(COLOR_FONDO);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton btnVolver = new JButton("Volver a la partida");
        btnVolver.setBackground(new Color(60, 60, 60));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVolver.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnVolver.addActionListener(e -> this.setVisible(false));

        bottomPanel.add(btnVolver);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Crea un panel para contener los componentes graficos de una flota.
     * Utiliza un FlowLayout centrado con margenes para organizar las naves.
     *
     * @return Un JPanel configurado para mostrar naves.
     */
    private JPanel crearPanelFlota() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        p.setBackground(COLOR_FONDO);
        return p;
    }

    /**
     * Actualiza la visualizacion de la flota propia con el estado actual de las naves.
     * Limpia el panel y vuelve a renderizar las naves basandose en el mapa proporcionado.
     *
     * @param estadoMisNaves Un mapa que asocia tipos de naves con listas de sus estados actuales.
     */
    public void actualizarFlotaPropia(Map<TipoNave, List<EstadoNave>> estadoMisNaves) {
        panelContenidoPropio.removeAll();
        renderizarNaves(panelContenidoPropio, estadoMisNaves);
        panelContenidoPropio.revalidate();
        panelContenidoPropio.repaint();
    }
    
    /**
     * Actualiza la visualizacion de la flota enemiga con el estado actual de las naves.
     * Limpia el panel y vuelve a renderizar las naves basandose en el mapa proporcionado.
     *
     * @param estadoNavesEnemigas Un mapa que asocia tipos de naves con listas de sus estados actuales.
     */
    public void actualizarFlotaEnemiga(Map<TipoNave, List<EstadoNave>> estadoNavesEnemigas) {
        panelContenidoEnemigo.removeAll();
        renderizarNaves(panelContenidoEnemigo, estadoNavesEnemigas);
        panelContenidoEnemigo.revalidate();
        panelContenidoEnemigo.repaint();
    }

    /**
     * Dibuja las naves asegurando que aparezcan TODAS las ranuras disponibles,
     * esten posicionadas o no.
     * 
     * @param panel El JPanel donde se agregaran los componentes graficos de las naves.
     * @param mapaNaves Un mapa que asocia tipos de naves con listas de sus estados.
     */
    private void renderizarNaves(JPanel panel, Map<TipoNave, List<EstadoNave>> mapaNaves) {
        TipoNave[] ordenVisual = {
            TipoNave.PORTA_AVIONES,
            TipoNave.CRUCERO,
            TipoNave.SUBMARINO,
            TipoNave.BARCO
        };

        for (TipoNave tipo : ordenVisual) {
            int cantidadTotal = tipo.getCantidadInicial();

            List<EstadoNave> estadosActuales = mapaNaves.get(tipo);

            for (int i = 0; i < cantidadTotal; i++) {
                EstadoNave estado;

                if (estadosActuales != null && i < estadosActuales.size()) {
                    estado = estadosActuales.get(i);
                } else {
                    estado = EstadoNave.SIN_DANIOS;
                }

                panel.add(new NaveGraficaComponent(tipo.getTamanio(), estado));
            }
        }
    }

    /**
     * Crea un titulo para el panel de una flota
     *
     * @param text El texto del título.
     * @return Un JLabel configurado como titulo.
     */
    private JLabel crearTitulo(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI Black", Font.BOLD, 24));
        lbl.setForeground(Color.BLACK);
        return lbl;
    }

     /**
     * Crea un JScrollPane para contener un panel de vista.
     *
     * @param view El JPanel que se colocará dentro del JScrollPane.
     * @return Un JScrollPane configurado.
     */
    private JScrollPane crearScroll(JPanel view) {
        JScrollPane scroll = new JScrollPane(view);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COLOR_FONDO);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    /**
     * Componente gráfico personalizado que representa una nave en el marcador.
     * Dibuja una bolita de estado y un conjunto de celdas que representan el cuerpo de la nave.
     */
    private class NaveGraficaComponent extends JComponent {

        //Número de celdas que conforman la nave
        private final int celdasNave;
        
        //Estado actual de la nave
        private final EstadoNave estado;

        // Dimensiones de una celda
        private final int ANCHO_CELDA = 25;
        private final int ALTO_CELDA = 25;
        private final int DIAMETRO_BOLA = 20;
        private final int GAP = 10;

        // altura maxima: 4 celdas, para que todas las naves se alineen a partir de esta altura
        private final int MAX_CELDAS_VISUALES = 4;

        /**
         * Constructor del componente gráfico de la nave.
         *
         * @param celdasNave Número de celdas que tiene la nave.
         * @param estado Estado actual de la nave.
         */
        public NaveGraficaComponent(int celdasNave, EstadoNave estado) {
            this.celdasNave = celdasNave;
            this.estado = estado;

            int anchoTotal = ANCHO_CELDA + 4;
            // Reservamos espacio vertical como si fuera el barco más grande
            int alturaTotal = DIAMETRO_BOLA + GAP + (MAX_CELDAS_VISUALES * ALTO_CELDA);

            setPreferredSize(new Dimension(anchoTotal, alturaTotal));
            setOpaque(false);
        }

         /**
         * Dibuja una bolita de color segun el estado y las celdas del cuerpo de la nave.
         *
         * @param g El contexto grafico para dibujar.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int centerX = getWidth() / 2;

            //DIBUJAR LA BOLITA
            Color colorBola;
            switch (estado) {
                case SIN_DANIOS ->
                    colorBola = COLOR_INTACTO;
                case AVERIADO ->
                    colorBola = COLOR_AVERIA;
                case HUNDIDO ->
                    colorBola = COLOR_HUNDIDO;
                default ->
                    colorBola = Color.GRAY;
            }

            int ballX = centerX - (DIAMETRO_BOLA / 2);
            int ballY = 0;

            g2.setColor(colorBola);
            g2.fillOval(ballX, ballY, DIAMETRO_BOLA, DIAMETRO_BOLA);

            //DIBUJAR EL CUERPO
            int startY = DIAMETRO_BOLA + GAP;
            int rectX = centerX - (ANCHO_CELDA / 2);

            for (int i = 0; i < celdasNave; i++) {
                int currentY = startY + (i * ALTO_CELDA);

                g2.setColor(COLOR_CELDA_NAVE);
                g2.fillRect(rectX, currentY, ANCHO_CELDA, ALTO_CELDA);

                g2.setColor(COLOR_BORDE_CELDA);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRect(rectX, currentY, ANCHO_CELDA, ALTO_CELDA);
            }

            // Si esta hundido, oscurecemos el cuerpo
            if (estado == EstadoNave.HUNDIDO) {
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRect(rectX, startY, ANCHO_CELDA, celdasNave * ALTO_CELDA);
            }

            g2.dispose();
        }
    }
}
