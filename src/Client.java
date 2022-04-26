package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    //
    private Scanner scnr = new Scanner(System.in);

    public Client() {
        OpenSocket();
    }

    public void OpenSocket() {
        try {
            clientSocket = new Socket("192.168.0.166", 5000);
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread senderThread = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    while(true) {
                        msg = scnr.nextLine();
                        out.println(msg);
                        out.flush();
                    }
                }
            });
            senderThread.start();

            Thread receiverThread = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try {
                        msg = in.readLine();

                        while(msg!=null) {
                            //
                            System.out.println("Server : " + msg);
                            msg = in.readLine();
                        }
                        //
                        System.out.println("Lost connection to server");

                        out.close();
                        clientSocket.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                }
                }
            });
            receiverThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
