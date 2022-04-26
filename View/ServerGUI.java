package View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import src.Server;

public class ServerGUI extends Application {

    private Server server = new Server();
    private GridPane rootPane = new GridPane();
    private GridPane interfacePane = new GridPane();
    private GridPane settingsPane = new GridPane();
    public TextField chatbox = new TextField();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ChatApp");

        createPanes();
        addNodes();
        
        server.receiveMessage(chatbox);

        // Create a scene
        Scene scene = new Scene(rootPane, 300, 600);

        // Set the scene in primary stage	
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createPanes() {
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setPadding(new Insets(5, 5, 5, 5));
        rootPane.setHgap(60);

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints rootcolumnConstraints = new ColumnConstraints(0, 0, Double.MAX_VALUE);
        rootcolumnConstraints.setHalignment(HPos.RIGHT);

        rootPane.getColumnConstraints().addAll(rootcolumnConstraints);
        
        interfacePane.setVgap(10);
        rootPane.add(interfacePane, 0, 0, 2, 1);
        rootPane.add(settingsPane, 0, 0, 2, 1);

        rootPane.getChildren().get(1).setDisable(true);
    }

    private void addNodes() {
        Label chatboxLabel = new Label("Chat Box");
        chatboxLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        interfacePane.add(chatboxLabel, 0,0,2,2);

        chatbox.setPrefHeight(300);
        chatbox.setPrefWidth(200);
        chatbox.setAlignment(Pos.TOP_LEFT);
        chatbox.setEditable(false);
        interfacePane.add(chatbox, 0, 2, 2, 8);

        TextField messageField = new TextField();
        messageField.setPrefWidth(200);
        interfacePane.add(messageField, 0, 13, 2, 1);

        Button sendBtn = new Button("Send");
        sendBtn.setPrefHeight(30);
        sendBtn.setDefaultButton(true);
        sendBtn.setPrefWidth(90);
        interfacePane.add(sendBtn, 0, 14, 2, 2);

        
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = messageField.getText();
                if(!messageToSend.isEmpty()) {
                    
                    chatbox.appendText("\n " + messageToSend);

                    server.sendMessage(messageToSend);
                    messageField.clear();
                }
            }
        });
    }

    public static void receiveMessage(String messageFrom, TextField chatbox) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatbox.appendText("\n Client: " + messageFrom);
            }
        });
    }
}
