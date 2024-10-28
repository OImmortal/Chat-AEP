package org.example;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Cliente2 {
    public static void main(String[] args) {
        String ipServidor = "10.1.1.116";
        int porta = 52134;

        try (Socket socket = new Socket(ipServidor, porta);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Enviando o endereço IP do cliente
            InetAddress localIp = InetAddress.getLocalHost();
            Mensagem mensagemObj = new Mensagem("10.1.1.120");
            out.writeObject(mensagemObj);
            out.flush();

            String mensagem;

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

            do {
                mensagem = scanner.nextLine();
                writer.println(mensagem);
            } while (!mensagem.equalsIgnoreCase("sair"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
