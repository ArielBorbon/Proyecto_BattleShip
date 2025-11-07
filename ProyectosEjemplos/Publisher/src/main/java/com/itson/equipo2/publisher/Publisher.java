/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.itson.equipo2.publisher;

import com.google.gson.Gson;
import java.util.Scanner;
import redis.clients.jedis.Jedis;

/**
 *
 * @author skyro
 */
public class Publisher {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("🟢 Publisher conectado a Redis");
            System.out.println("Escribe mensajes para enviar (escribe 'exit' para salir):");

            Gson gson = new Gson();

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Saliendo del publisher...");
                    break;
                }

                jedis.publish("canal_prueba", message);
                System.out.println("📤 Mensaje enviado: " + message);
            }
        }
    }
}
