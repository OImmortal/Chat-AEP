package org.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        String ipServidor = "10.1.1.116";
        int porta = 52134;

        try (Socket socket = new Socket(ipServidor, porta);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Enviando o endereço IP do cliente
            InetAddress localIp = InetAddress.getLocalHost();
            Mensagem mensagemObj = new Mensagem("10.1.1.150");
            out.writeObject(mensagemObj);
            out.flush();

            new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String mensagemString;
                    // Loop para verificar mensagens do servidor
                    while ((mensagemString = reader.readLine()) != null) {
                        System.out.println(mensagemString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            String mensagem;
            do {
                mensagem = scanner.nextLine();
                writer.println(mensagem);
            } while (!mensagem.equalsIgnoreCase("sair"));

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
