package com.itson.equipo2.battleship_cliente.view.util;

import mx.itson.equipo_2.common.enums.TipoNave;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BarcoImageFactory {

    public static BufferedImage createImagenBarco(TipoNave tipo, int cellSize, Color colorBase) {
        int width = cellSize * tipo.getTamanio();
        int height = cellSize;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        // Calidad alta de renderizado
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int margin = 8;
        int wReal = width - (margin * 2);
        int hReal = height - (margin * 2);

        // Si el color viene nulo, usamos gris
        if (colorBase == null) {
            colorBase = Color.GRAY;
        }

        Color colorCubierta = colorBase;
        Color colorDetalle = colorBase.darker();
        Color colorBorde = new Color(30, 30, 30);

        // Dibujo base
        g2.setColor(colorCubierta);

        switch (tipo) {
            case BARCO: // Tamaño 1
                g2.fillOval(margin, margin, wReal, hReal);
                g2.setColor(colorBorde);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(margin, margin, wReal, hReal);
                g2.setColor(colorDetalle);
                g2.fillOval(margin + wReal / 3, margin + hReal / 3, wReal / 3, hReal / 3);
                break;

            case SUBMARINO: // Tamaño 2
                g2.fillRoundRect(margin, margin + 5, wReal, hReal - 10, 40, 40);
                g2.setColor(colorBorde);
                g2.drawRoundRect(margin, margin + 5, wReal, hReal - 10, 40, 40);
                g2.setColor(colorDetalle);
                g2.drawRect(margin + wReal / 2 - 10, margin, 20, 15);
                break;

            case CRUCERO: // Tamaño 3
                Polygon p = new Polygon();
                p.addPoint(margin, margin);
                p.addPoint(width - margin - 30, margin);
                p.addPoint(width - margin, height / 2);
                p.addPoint(width - margin - 30, height - margin);
                p.addPoint(margin, height - margin);
                g2.fillPolygon(p);
                g2.setColor(colorBorde);
                g2.drawPolygon(p);
                break;

            case PORTA_AVIONES: // Tamaño 4
                g2.fillRect(margin, margin, wReal, hReal);
                g2.setColor(colorBorde);
                g2.drawRect(margin, margin, wReal, hReal);
                g2.setColor(new Color(255, 255, 255, 128));
                Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0);
                g2.setStroke(dashed);
                g2.drawLine(margin + 10, height / 2, width - margin - 10, height / 2);
                break;
        }
        g2.dispose();
        return img;
    }
}
