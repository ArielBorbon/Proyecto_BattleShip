package com.itson.equipo2.battleship_cliente.view.util;

import mx.itson.equipo_2.common.enums.TipoNave;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TestPreviewNaves {

    public static void main(String[] args) {
        // Crear una ventana simple
        JFrame frame = new JFrame("Previsualización de Flota - Java 2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Panel personalizado para pintar las pruebas
        JPanel panelPreview = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                
                // Fondo tipo "Agua" para ver el contraste real
                g2.setColor(new Color(60, 100, 140)); 
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Tamaño de celda simulado (como en tu juego)
                int cellSize = 57;
                int startX = 50;
                int startY = 50;

                // Títulos
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                g2.drawString("Tu Flota (Color Azul/Verde)", startX, startY - 20);
                g2.drawString("Flota Enemiga (Color Rojo)", startX + 400, startY - 20);

                // --- COLUMNA 1: TUS NAVES (Verde/Azul) ---
                Color miColor = new Color(100, 200, 100); // Un verde bonito
                int y = startY;
                
                for (TipoNave tipo : TipoNave.values()) {
                    // 1. Crear la imagen
                    BufferedImage img = BarcoImageFactory.createImagenBarco(tipo, cellSize, miColor);
                    
                    // 2. Dibujarla
                    g2.drawImage(img, startX, y, null);
                    
                    // (Opcional) Dibujar rejilla para ver alineación
                    g2.setColor(new Color(255, 255, 255, 50));
                    g2.drawRect(startX, y, cellSize * tipo.getTamanio(), cellSize);
                    
                    // Texto descriptivo
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    g2.drawString(tipo.name() + " (" + tipo.getTamanio() + " celdas)", startX, y - 5);
                    
                    y += cellSize + 30; // Espacio para la siguiente nave
                }

                // --- COLUMNA 2: NAVES ENEMIGAS (Rojo) ---
                Color enemigoColor = new Color(200, 80, 80); // Rojo suave
                y = startY;
                int xEnemigo = startX + 400;

                for (TipoNave tipo : TipoNave.values()) {
                    BufferedImage img = BarcoImageFactory.createImagenBarco(tipo, cellSize, enemigoColor);
                    g2.drawImage(img, xEnemigo, y, null);
                    
                    // Rejilla
                    g2.setColor(new Color(255, 255, 255, 50));
                    g2.drawRect(xEnemigo, y, cellSize * tipo.getTamanio(), cellSize);
                    
                    y += cellSize + 30;
                }
            }
        };

        frame.add(panelPreview);
        frame.setVisible(true);
    }
}