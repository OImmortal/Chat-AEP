package org.example;

import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Servidor {
    private int porta;
    private static Map<String, String> sentMessages = new HashMap<String, String>();
    private static List<ClientHandler> clientsConnected = new ArrayList<ClientHandler>();
    private static List<ClientsConnected> chatsConnected = new ArrayList<ClientsConnected>();

    public Servidor(int porta) {
        this.porta = porta;
    }

    public static void getMessageOfClient(String message, String ip) {
        sentMessages.put(ip, message);
        System.out.println(sentMessages.get(ip));
    }

    private static boolean ipConnected(String ip) {
        boolean hasClient = false;
        for (ClientHandler client : clientsConnected) {
            if(ip.equals(client.getIp())) {
                hasClient =  true;
                break;
            }
        }

        return hasClient;
    }

    private static boolean clientConnected(ClientHandler client) {
        return clientsConnected.contains(client);
    }

    private static ClientHandler getClientByIp(String ip) {
        for (ClientHandler client : clientsConnected) {
            if(Objects.equals(ip, client.getIp())) {
                return client;
            }
        }

        return null;
    }

    public static void connectChats(String ip1, String ip2) {
        ClientHandler client1 = getClientByIp(ip1);
        ClientHandler client2 = getClientByIp(ip2);

        if(clientIsConnectedWithOtherClient(client1.getIp(), client2.getIp())) {
            System.out.println("User already logged in");
        }

        if(client1 != null && client2 != null) {
            if(clientConnected(client1) && clientConnected(client2)) {
                client1.setIpConnected(ip2);
                client2.setIpConnected(ip1);
                ClientsConnected chatConnected = new ClientsConnected(client1.getIp(), client2.getIp());
                System.out.println("Users conneted");
                chatsConnected.add(chatConnected);
            }
        }

    }

    public static void disconnectChats(String ip) {
        ClientHandler client = getClientByIp(ip);
        ClientHandler clientConnected = getClientByIp(client.getIpConnected());

        client.setIpConnected(null);
        clientConnected.setIpConnected(null);

        Iterator<ClientsConnected> iterator = chatsConnected.iterator();
        while (iterator.hasNext()) {
            ClientsConnected e = iterator.next();
            if (Objects.equals(e.getIp1(), client.getIp()) || Objects.equals(e.getIp2(), client.getIp())) {
                iterator.remove();  // Remover de forma segura
                System.out.println("User disconnected from chat");
            }
        }
    }


    public static boolean clientIsConnectedOnChat(String ip) {
        for (var e : chatsConnected) {
            if(Objects.equals(e.getIp1(), ip) || Objects.equals(e.getIp2(), ip)) {
                return true;
            }
        }

        return false;
    }

    public static boolean clientIsConnectedWithOtherClient(String ip1, String ip2) {
        boolean hasClient = false;
        for(ClientsConnected chat : chatsConnected) {
            if ((chat.getIp1().equalsIgnoreCase(ip1) && chat.getIp2().equalsIgnoreCase(ip2)) ||
                    (chat.getIp1().equalsIgnoreCase(ip2) && chat.getIp2().equalsIgnoreCase(ip1))) {
                hasClient = true;
                break;
            }
        }
        return hasClient;
    }

    public static void sendMenssageToClient(String message, String ip) {
        try {
            ClientHandler client = getClientByIp(ip);
            // Envia mensagem para o cliente
            OutputStream output = client.getSocket().getOutputStream();
            PrintWriter writerServer = new PrintWriter(output, true);
            writerServer.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendIpToAllClients() {
        for (ClientHandler client : clientsConnected) {
            if(client.getSocket().isConnected()) {
                for (int i = 0;i < clientsConnected.size();i++) {
                    sendMenssageToClient(clientsConnected.get(i).getIp(), client.getIp());
                }
            }
        }
    }

    public void runServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(porta);
            System.out.println("Server connected, waiting for connections");

            while (true) {
                Socket socket = serverSocket.accept();

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Mensagem msg = (Mensagem) in.readObject();

                String ip = msg.getMensagem();
                System.out.println("Client - " + ip + " Connected");

                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                ClientHandler client = new ClientHandler(socket, writer, ip);

                new Thread(() -> {
                    while(true) {
                        if (!clientsConnected.isEmpty()) {
                            Iterator<ClientHandler> iterator = clientsConnected.iterator();
                            while (iterator.hasNext()) {
                                ClientHandler clientServer = iterator.next();
                                if (clientServer.getSocket().isClosed()) {
                                    System.out.println("Client - " + clientServer.getIp() + " disconnected");
                                    iterator.remove();
                                    break;// Remove o cliente da lista de forma segura
                                }
                            }
                        }
                    }
                }).start();

                clientsConnected.add(client);

                sendIpToAllClients();

                new Thread(client).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
