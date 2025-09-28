/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.equipo_2.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author skyro
 */
public class ServerHandler implements Runnable {
    
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServerHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while(true) {
                Object obj = in.readObject();
                if(obj instanceof DisparoDTO disparo) {
                    System.out.println("Disparo recibido: " + disparo);
                    // Aquí iría la lógica de la partida
                    ResultadoDTO res = new ResultadoDTO("Agua");
                    out.writeObject(res);
                    out.flush();
                }
            }
        } catch(Exception e) {
            System.out.println("Cliente desconectado.");
        }
    }
}
