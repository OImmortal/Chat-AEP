package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ClientView extends JFrame {
    private Client client;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private List<String> ipsAdded = new ArrayList<String>();
    private List<JButton> users = new ArrayList<JButton>();

    public ClientView(Client client) {
        this.client = client;
        execute();
    }

    public void execute() {
        setTitle("Chat");
        setSize(400, 400);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new GridLayout(ipsAdded.size(), 1));

        setResizable(false);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                client.disconnect();

                dispose();
                System.exit(0);
            }
        });


        new Thread(() -> {
            while (true) {
                for (String ips : client.getListOfIps()) {
                    if (!ipsAdded.contains(ips)) {
                        ipsAdded.add(ips);
                        JButton button = new JButton(ips);
                        users.add(button);
                        SwingUtilities.invokeLater(() -> {
                            add(button); // Adiciona o botão na interface gráfica
                            revalidate(); // Atualiza o layout
                            repaint(); // Redesenha a interface
                        });
                    }
                }
                try {
                    Thread.sleep(1000); // Pausa o loop para reduzir a carga na CPU
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                for (int i = 0; i < users.size(); i++) {
                    JButton userButton = users.get(i);
                    if (userButton.getActionListeners().length == 0) {  // Verifica se o listener já foi adicionado
                        String ip = userButton.getText();
                        userButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.out.println("Enviado");
                                client.sendMenssageToServidor(ip);
                            }
                        });
                    }
                }
                try {
                    Thread.sleep(1000); // Evita que o loop rode sem pausas
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        setVisible(true);
    }

    private void chatPage() {
        setTitle("Chat");
        setSize(400, 400);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                client.disconnect();

                dispose();
                System.exit(0);
            }
        });

        chatArea = new JTextArea();
        chatArea.setEditable(false);  // Para impedir a edição direta na área de chat
        JScrollPane scrollPane = new JScrollPane(chatArea);

        // Caixa de texto para digitar mensagens
        messageField = new JTextField();

        // Botão de envio
        sendButton = new JButton("Enviar");

        // Painel na parte inferior com a caixa de mensagem e o botão
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        // Adicionando os componentes ao JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Evento de envio de mensagem
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                client.sendMenssageToServidor(message);
            }
        });

        // Também envia a mensagem quando o usuário pressiona Enter
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                client.sendMenssageToServidor(message);
            }
        });
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.trim().isEmpty()) {
            chatArea.append("Você: " + message + "\n");  // Adiciona a mensagem ao chat
            messageField.setText("");  // Limpa a caixa de texto
        }
    }

}
