package org.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private String ipServidor;
    private int porta;
    private static ObjectOutputStream out;
    private static PrintWriter writer;
    private Socket socketServer;
    private boolean isDisconnected = false;
    private List<String> listOfIps = new ArrayList<String>();

    public Client(String ipServidor, int porta) {
        this.ipServidor = ipServidor;
        this.porta = porta;
    }

    public String getIpServidor() {
        return ipServidor;
    }

    public void setIpServidor(String ipServidor) {
        this.ipServidor = ipServidor;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public void sendIpToServer(String ip) {
        try {
            Mensagem mensagemObj = new Mensagem(ip);

            out.writeObject(mensagemObj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isDisconnected() {
        return isDisconnected;
    }

    public void setDisconnected(boolean disconnected) {
        isDisconnected = disconnected;
    }

    public Socket getSocket() {
        return socketServer;
    }

    public void disconnect() {
        isDisconnected = true;
        try {
            if (socketServer != null && !socketServer.isClosed()) {
                socketServer.close();
                System.out.println("Socket fechado ao desconectar.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getListOfIps() {
        return listOfIps;
    }

    public void setListOfIps(List<String> listOfIps) {
        this.listOfIps = listOfIps;
    }

    public void sendMenssageToServidor(String message) {
        try {
            writer.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listenMenssageFromServer() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socketServer.getInputStream()));
            String message;
            while ((message = reader.readLine()) != null) {
                String ipPattern =  "^([0-9]{1,3}\\.){3}[0-9]{1,3}$";
                if(message.matches(ipPattern)) {
                    if(!listOfIps.contains(message)) {
                        listOfIps.add(message);
                        System.out.println("Lista de ips: " + listOfIps.toArray().length);
                    }
                }

                System.out.println(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            socketServer = new Socket(ipServidor, porta);
            out = new ObjectOutputStream(socketServer.getOutputStream());
            writer = new PrintWriter(socketServer.getOutputStream(), true);

            InetAddress localIp = InetAddress.getLocalHost();
            sendIpToServer(localIp.getHostAddress());

            new Thread(() -> listenMenssageFromServer()).start();

            // Verifique a condição de desconexão de forma apropriada, sem fechar o socketServer logo após abrir.
            while (!isDisconnected) {

            }

            socketServer.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
