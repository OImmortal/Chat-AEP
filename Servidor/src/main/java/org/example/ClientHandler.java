package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{

    private Socket socket;
    private PrintWriter out;
    private String ip;
    private String ipConnected;
    private List<String> sentMessages = new ArrayList<String>();

    public ClientHandler(Socket socket, PrintWriter out, String ip) {
        this.socket = socket;
        this.out = out;
        this.ip = ip;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public String getIp() {
        return ip;
    }

    public String getIpConnected() {
        return ipConnected;
    }

    public void setIpConnected(String ipConnected) {
        this.ipConnected = ipConnected;
    }

    @Override
    public void run() {
        try {
            String message;
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            do {
                message = reader.readLine();
                if(message != null) {
                    String ipPattern =  "^([0-9]{1,3}\\.){3}[0-9]{1,3}$";
                    if(message.matches(ipPattern)) {
                        Servidor.connectChats(this.ip, message);
                    }

                    if(Servidor.clientIsConnectedOnChat(this.ip)) {

                        if(message.equals("desconectar")) {
                            if(Servidor.clientIsConnectedOnChat(this.ip)) {
                                Servidor.disconnectChats(this.ip);
                            }
                        }

                        if(!message.equals("desconectar") && !message.matches(ipPattern) && !message.equals("sair")) {
                            this.sentMessages.add(message);
                            Servidor.sendMenssageToClient(message, this.ipConnected);
                        }

                        if(message.equals("sair")) {
                            socket.close();
                            out.close();
                            reader.close();
                        }
                    } else if(!message.matches(ipPattern)) {
                        System.out.println("Failed");
                    }
                }

            } while(message != null);

            reader.close();
            socket.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
