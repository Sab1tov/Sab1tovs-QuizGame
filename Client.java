package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Application {

    private Stage window;
    private final double W = 600., H = 600;
    private Pane root;

    private Socket socket;
    private DataOutputStream toServer;
    private DataInputStream fromServer;

    private void connectToServer() throws IOException {
        socket = new Socket("localhost", 2022);
        toServer = new DataOutputStream(socket.getOutputStream());
        fromServer = new DataInputStream(socket.getInputStream());
    }


    public Button kahootButton(String btnColor) {
        Button btn = new Button();
        btn.setMinWidth(W / 2. - 5);
        btn.setMinHeight(H / 2. - 5);
        btn.setStyle("-fx-background-color: " + btnColor);

        btn.setTextFill(Color.WHITE);
        btn.setWrapText(true);
        btn.setPadding(new Insets(10));

        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 15);
        btn.setFont(font);
        return btn;
    }

    public StackPane gamePin(String nickname) {
        StackPane stackPane = new StackPane();
        VBox vBox1 = new VBox(10);
        Button btnRed = kahootButton("red");
        Button btnBlue = kahootButton("blue");
        vBox1.getChildren().addAll(btnRed, btnBlue);
        VBox vBox2 = new VBox(10);
        Button btnOrange = kahootButton("orange");
        Button btnGreen = kahootButton("green");
        vBox2.getChildren().addAll(btnOrange, btnGreen);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(vBox1, vBox2);
        stackPane.getChildren().addAll(hBox);

        btnRed.setOnAction(e -> {
            try {
                toServer.writeUTF(nickname + ": Red");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnBlue.setOnAction(e -> {
            try {
                toServer.writeUTF(nickname + ": Blue");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnOrange.setOnAction(e -> {
            try {
                toServer.writeUTF(nickname + ": Orange");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnGreen.setOnAction(e -> {
            try {
                toServer.writeUTF(nickname + ": Green");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        return stackPane;
    }


    public StackPane nicknamePane() {
        StackPane stackPane = new StackPane();
        TextField tf = new TextField();
        tf.setPromptText("Enter username");
        tf.setMaxWidth(W / 3);
        tf.setMinHeight(40);
        tf.setAlignment(Pos.CENTER);
        Button btn = new Button("Enter");
        btn.setMaxWidth(W / 3);
        btn.setMinHeight(40);
        btn.setStyle("-fx-background-color:#333333");
        btn.setTextFill(Color.WHITE);
        btn.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16));
        VBox vBox = new VBox(10);
        vBox.setMaxWidth(W / 2);
        vBox.setMaxHeight(H / 2);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(tf, btn);

        stackPane.getChildren().addAll(vBox);
        stackPane.setStyle("-fx-background-color: #46178f");

        btn.setOnAction(e -> {
            try {
                toServer.writeUTF(tf.getText());
                window.setScene(new Scene(gamePin(tf.getText()), W, H));
                window.setTitle(tf.getText());
                String status = fromServer.readUTF();
                if (status.equals("Success!")) {

                }
                System.out.println(status);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        return stackPane;
    }


    public StackPane pinPane() {
        StackPane stackPane = new StackPane();
        TextField tf = new TextField();
        tf.setPromptText("Game PIN");
        tf.setMaxWidth(W / 3);
        tf.setMinHeight(40);
        tf.setAlignment(Pos.CENTER);
        Button btn = new Button("Enter");
        btn.setMaxWidth(W / 3);
        btn.setMinHeight(40);
        btn.setStyle("-fx-background-color:#333333");
        btn.setTextFill(Color.WHITE);
        btn.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16));
        VBox vBox = new VBox(10);
        vBox.setMaxWidth(W / 2);
        vBox.setMaxHeight(H / 2);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(tf, btn);

        stackPane.getChildren().addAll(vBox);
        stackPane.setStyle("-fx-background-color: #3e147f");

        btn.setOnAction(e -> {
            try {
                toServer.writeInt(Integer.parseInt(tf.getText()));
                String status = fromServer.readUTF();
                if (status.equals("DONE!")) {
                    window.setScene(new Scene(nicknamePane(), W, H));
                    window.setTitle("Enter Nickname");
                }
                System.out.println(status);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        return stackPane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        connectToServer();
        root = pinPane();
        primaryStage.setScene(new Scene(root, W, H));
        primaryStage.show();
        primaryStage.setTitle("Enter PIN!");
        root.requestFocus();
    }
}
