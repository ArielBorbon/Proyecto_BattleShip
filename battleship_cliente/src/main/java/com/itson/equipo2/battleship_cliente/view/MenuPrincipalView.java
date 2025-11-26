
package com.itson.equipo2.battleship_cliente.view;

import com.itson.equipo2.battleship_cliente.controllers.VistaController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPrincipalView extends JPanel {

    private final Color COLOR_FONDO = new Color(83, 111, 137);
    private final Color COLOR_BOTON = new Color(75, 75, 75);
    private final Color COLOR_TEXTO = new Color(255, 255, 255);
    private final Font FUENTE_BOTON = new Font("Segoe UI Black", 0, 18);
    private final Font FUENTE_TITULO = new Font("Segoe UI Black", 1, 48);

    public MenuPrincipalView(VistaController viewController) {
        initComponents(viewController);
    }

    private void initComponents(VistaController viewController) {
        setBackground(COLOR_FONDO);
        setLayout(null);
        setPreferredSize(new Dimension(1280, 720));
        setMaximumSize(new Dimension(1280, 720));
        setMinimumSize(new Dimension(1280, 720));

     
        JLabel lblTitulo = new JLabel("BATTLESHIP");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(440, 100, 400, 60);
        add(lblTitulo);

     
        try {
            ImageIcon shipIcon = new ImageIcon(getClass().getResource("/images/ship.png"));
            JLabel lblShipImage = new JLabel(shipIcon);
            lblShipImage.setBounds(490, 200, 300, 200); 
            add(lblShipImage);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen del barco: " + e.getMessage());
        }

       
        JButton btnJugar = new JButton("Jugar");
        btnJugar.setBackground(COLOR_BOTON);
        btnJugar.setFont(FUENTE_BOTON);
        btnJugar.setForeground(COLOR_TEXTO);
        btnJugar.setBorder(null);
        btnJugar.setFocusPainted(false);
        btnJugar.setBounds(562, 450, 156, 41);
        btnJugar.addActionListener(e -> viewController.cambiarPantalla("registro"));
        add(btnJugar);

        
        JButton btnSalir = new JButton("Salir");
        btnSalir.setBackground(COLOR_BOTON);
        btnSalir.setFont(FUENTE_BOTON);
        btnSalir.setForeground(COLOR_TEXTO);
        btnSalir.setBorder(null);
        btnSalir.setFocusPainted(false);
        btnSalir.setBounds(562, 510, 156, 41);
        btnSalir.addActionListener(e -> System.exit(0));
        add(btnSalir);
    }
}