/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mx.itson.equipo_2.views;

import javax.swing.JFrame;

/**
 *
 * @author skyro
 */
public class MainTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setVisible(true);
        
        frame.add(new DispararView());
        
        frame.setVisible(true);
    }
    
}
