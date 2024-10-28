package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("10.1.1.116", 52134);
        new Thread(() -> client.run()).start();

        SwingUtilities.invokeLater(() -> {
            ClientView clientView = new ClientView(client);
        });


    }
}
