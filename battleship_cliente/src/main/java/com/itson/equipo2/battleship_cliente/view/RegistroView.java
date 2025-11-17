/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.equipo2.battleship_cliente.view;


import com.itson.equipo2.battleship_cliente.controllers.RegistroController;
import com.itson.equipo2.battleship_cliente.controllers.ViewController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import mx.itson.equipo_2.common.enums.ColorJugador;

public class RegistroView extends JPanel {

    
    private final Color COLOR_FONDO = new Color(83, 111, 137);
    private final Color COLOR_BOTON = new Color(75, 75, 75);
    private final Color COLOR_TEXTO = new Color(255, 255, 255);
    private final Font FUENTE_BOTON = new Font("Segoe UI Black", 0, 18);
    private final Font FUENTE_LABEL = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);

    private JTextField txtNombre;
    private JComboBox<ColorJugador> cmbColor;

    public RegistroView(ViewController viewController, RegistroController registroController) {
        initComponents(viewController, registroController);
    }

    private void initComponents(ViewController viewController, RegistroController registroController) {
        setBackground(COLOR_FONDO);
        setLayout(null);
        setPreferredSize(new Dimension(1280, 720));
        setMaximumSize(new Dimension(1280, 720));
        setMinimumSize(new Dimension(1280, 720));

        
        JLabel lblTitulo = new JLabel("Registrar Jugador:");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setBounds(490, 150, 300, 30);
        add(lblTitulo);

                try {
            ImageIcon shipIcon = new ImageIcon(getClass().getResource("/images/ship_small.png")); 
            JLabel lblShipImage = new JLabel(shipIcon);
            lblShipImage.setBounds(750, 230, 200, 133); 
            add(lblShipImage);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen del barco: " + e.getMessage());
        }

                JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(FUENTE_LABEL);
        lblNombre.setForeground(COLOR_TEXTO);
        lblNombre.setBounds(400, 240, 100, 30);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setFont(FUENTE_LABEL);
        txtNombre.setBounds(510, 240, 220, 30);
        add(txtNombre);

      
        JLabel lblColor = new JLabel("Color:");
        lblColor.setFont(FUENTE_LABEL);
        lblColor.setForeground(COLOR_TEXTO);
        lblColor.setBounds(400, 310, 100, 30);
        add(lblColor);

        cmbColor = new JComboBox<>(ColorJugador.values());
        cmbColor.setFont(FUENTE_LABEL);
        cmbColor.setBounds(510, 310, 220, 30);
        add(cmbColor);
        
        
        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBackground(COLOR_BOTON);
        btnConfirmar.setFont(FUENTE_BOTON);
        btnConfirmar.setForeground(COLOR_TEXTO);
        btnConfirmar.setBorder(null);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBounds(650, 450, 156, 41);
        btnConfirmar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            ColorJugador color = (ColorJugador) cmbColor.getSelectedItem();
            
          
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error: nombre invalido", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            } else {
              
                registroController.registrar(nombre, color);
            }
        });
        add(btnConfirmar);

  
        JButton btnVolver = new JButton("+ Volver");
        btnVolver.setBackground(COLOR_BOTON);
        btnVolver.setFont(FUENTE_BOTON);
        btnVolver.setForeground(COLOR_TEXTO);
        btnVolver.setBorder(null);
        btnVolver.setFocusPainted(false);
        btnVolver.setBounds(470, 450, 156, 41);
        btnVolver.addActionListener(e -> viewController.cambiarPantalla("menu")); 
        add(btnVolver);
    }
}
