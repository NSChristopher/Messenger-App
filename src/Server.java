package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import View.ServerGUI;
import javafx.scene.control.TextField;

public class Server {
        
        private ServerSocket serverSocket;
        private Socket clientSocket;
        private BufferedReader in;
        private BufferedWriter out;
        //

        private Scanner scnr = new Scanner(System.in);


        public Server() {
            try {
                serverSocket = new ServerSocket(5000);
                clientSocket = serverSocket.accept();
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String messageToSend) {
             try {
                 out.write(messageToSend);
                 out.newLine();
                 out.flush();
             } catch (IOException e) {
                 e.printStackTrace();
                 System.out.println("error sending message.");
             }
        }

        public void receiveMessage(TextField chatbox) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(clientSocket.isConnected()) {
                        try {
                            String messageFrom = in.readLine();
                            ServerGUI.receiveMessage(messageFrom, chatbox);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("error receiving message.");
                            break;
                        }
                    }   
                }
            }).start();
        }
}
