package com.example.demo;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static com.example.demo.Quiz.loadFromFile;



public class QuizMaker extends Application {
    Media media = new Media(new File("C:/Users/asasi/IdeaProjects/demo1/src/main/java/img/resources/kahoot_ost.mp3").toURI().toString());
    MediaPlayer mp = new MediaPlayer(media);
    private String[] answers;
    private Stage window;
    private final double W = 700, H = 500;
    private ArrayList<Question> questions;
    private boolean shuffleCheck;
    private static int generatePin() {
        return 10000 + (int) (Math.random() * 65536);
    }

    public StackPane server() throws Exception {

        StackPane sP = new StackPane();

        sP.setStyle("-fx-background-color: purple");

        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        int pin = generatePin();
        Label label = new Label("PIN:\n" + pin);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.REGULAR, 26));
        label.setAlignment(Pos.CENTER);
        label.setMinWidth(600);
        Button btnStart = new Button("START");
        btnStart.setVisible(false);
        btnStart.setAlignment(Pos.TOP_CENTER);
        btnStart.setOnAction(event ->{
            window.setScene(new Scene(currentQuestion(0), W, H));
        });
        VBox hB = new VBox();
        hB.getChildren().addAll(label,btnStart);
        borderPane.setTop(hB);
        new Thread(() -> {
            try {
                ServerSocket server = new ServerSocket(2022);
                int clientNum = 0;
                for(;;){
                    try {
                        System.out.println("Waiting For Some Clients");
                        Socket socket = server.accept();
                        System.out.println(clientNum + " Client has connected!");
                        new Thread(() -> {
                            try {
                                DataInputStream fromClient = new DataInputStream(socket.getInputStream());
                                DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
                                while (true) {
                                    int clientPin = fromClient.readInt();
                                    if (clientPin != pin) {
                                        toClient.writeUTF("Invalid PIN!!!");
                                    } else {
                                        toClient.writeUTF("READY!");
                                        String nick = fromClient.readUTF();
                                        System.out.println(nick);
                                        Label nickLbl = new Label(nick);
                                        nickLbl.setTextFill(Color.WHITE);
                                        nickLbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 20));

                                        Platform.runLater(() -> {
                                                    Button bts= new Button();
                                                    bts.setOpacity(0.5);
                                                    bts.setText(nickLbl.getText());
                                                    hBox.getChildren().add(bts);
                                                }

                                        );
                                        Platform.runLater(() -> {
                                            hB.getChildren().addAll(label,btnStart);
                                            borderPane.setTop(hB);
                                                }

                                        );
                                        btnStart.setVisible(true);
                                    }

                                    String clientChoice = fromClient.readUTF();
                                    System.out.println(clientChoice);
                                }

                            } catch (IOException e) {

                            }

                        }).start();
                        clientNum++;

                    } catch (IOException e) {

                    }
                }
            } catch (IOException ignore) {

            }

        }).start();
        sP.getChildren().addAll(borderPane, hBox);
        try{
            window.setScene(new Scene(currentQuestion(0), W, H));
        }catch(Exception e){
            e.printStackTrace();
        }
        return sP;
    }
    public RadioButton kahootButton(String text, String color) {
        Font font = Font.font("Montserrat", FontWeight.BOLD, 18);
        RadioButton button = new RadioButton(text);
        button.setMinWidth(W / 2);
        button.setMinHeight(100);
        button.setStyle("-fx-background-color: " + color);
        button.setTextFill(Color.WHITE);
        button.setFont(font);

        return button;
    }

    public BorderPane currentQuestion(int ind) {

        mp.setCycleCount(-1);
        mp.play();

        BorderPane borderPane = new BorderPane();

        Label lbl = new Label((ind + 1) + ") " + questions.get(ind).getDescription());
        lbl.setFont(Font.font("Montserrat", FontWeight.BOLD, 22));

        lbl.setWrapText(true);

        Button btnNext = new Button(">");
        Button btnPrev = new Button("<");
        btnNext.setFont(Font.font("Montserrat", FontWeight.BOLD, 22));
        btnPrev.setFont(Font.font("Montserrat", FontWeight.BOLD, 22));

        btnNext.setMinWidth(50.);
        btnNext.setMinHeight(50.);

        btnPrev.setMinWidth(50.);
        btnPrev.setMinHeight(50.);

        if (questions.get(ind) instanceof Test test) {

            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream("C:\\Users\\asasi\\IdeaProjects\\demo1\\src\\main\\java\\img\\resources\\img\\logo.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assert fileInputStream != null;
            Image image = new Image(fileInputStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(W / 2);
            imageView.setFitHeight(H / 2);

            RadioButton btnRed = kahootButton(test.getOptionAt(0), "red");
            RadioButton btnBlue = kahootButton(test.getOptionAt(1), "blue");
            RadioButton btnGreen = kahootButton(test.getOptionAt(2), "green");
            RadioButton btnOrange = kahootButton(test.getOptionAt(3), "orange");
            ToggleGroup tg = new ToggleGroup();
            btnRed.setToggleGroup(tg);
            btnBlue.setToggleGroup(tg);
            btnGreen.setToggleGroup(tg);
            btnOrange.setToggleGroup(tg);

            if (test.getOptionAt(0).equals(answers[ind])) {
                btnRed.fire();
            }
            if (test.getOptionAt(1).equals(answers[ind])) {
                btnBlue.fire();
            }
            if (test.getOptionAt(2).equals(answers[ind])) {
                btnGreen.fire();
            }
            if (test.getOptionAt(3).equals(answers[ind])) {
                btnOrange.fire();
            }

            btnRed.setOnAction(e -> answers[ind] = test.getOptionAt(0));

            btnBlue.setOnAction(e -> answers[ind] = test.getOptionAt(1));

            btnGreen.setOnAction(e -> answers[ind] = test.getOptionAt(2));

            btnOrange.setOnAction(e -> answers[ind] = test.getOptionAt(3));

            VBox vBox1 = new VBox(3);
            vBox1.getChildren().addAll(btnBlue, btnRed);
            vBox1.setMinWidth(W / 2);


            VBox vBox2 = new VBox(3);

            vBox2.getChildren().addAll(btnGreen, btnOrange);
            vBox2.setMinWidth(W / 2);
            HBox hBox = new HBox(3);

            hBox.setMinWidth(W);
            hBox.getChildren().addAll(vBox1, vBox2);

            lbl.setAlignment(Pos.TOP_CENTER);
            lbl.setMinWidth(W);

            borderPane.setTop(new StackPane(new VBox(new StackPane(lbl),new StackPane(timer()))));

            hBox.setAlignment(Pos.TOP_CENTER);
            borderPane.setCenter(imageView);
            borderPane.setBottom(new StackPane(hBox));
            borderPane.setRight(new StackPane(btnNext));
            borderPane.setLeft(new StackPane(btnPrev));

            if (ind == 0) {
                btnPrev.setVisible(false);
            }
            if (ind == questions.size() - 1) {
                btnNext.setVisible(false);

                Button endGame = new Button("✔");
                endGame.setMinWidth(50.);
                endGame.setMinHeight(50.);
                borderPane.setRight(new StackPane(endGame));
                endGame.setOnAction(e -> window.setScene(new Scene(result(), W, H)));
            }

            btnNext.setOnAction(e -> window.setScene(new Scene(currentQuestion(ind + 1), W, H)));

            btnPrev.setOnAction(e -> window.setScene(new Scene(currentQuestion(ind - 1), W, H)));

        } else {
            TextField textField = new TextField();
            textField.setText(answers[ind]);
            textField.minWidth(40);
            textField.minHeight(20);
            Label label = new Label("Type your answer here:");
            label.setFont(Font.font("Montserrat", FontWeight.BOLD, 14));

            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream("C:\\Users\\asasi\\IdeaProjects\\demo1\\src\\main\\java\\img\\resources\\img\\fillin.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assert fileInputStream != null;
            Image image = new Image(fileInputStream);
            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(W / 2);
            imageView.setFitHeight(H / 2);


            lbl.setAlignment(Pos.TOP_CENTER);
            lbl.setMinWidth(W);

            BorderPane bP = new BorderPane();
            VBox vbox1 = new VBox();
            label.setAlignment(Pos.CENTER);
            vbox1.getChildren().addAll(label,textField);
            bP.setCenter(vbox1);

            borderPane.setCenter(new StackPane(imageView));
            borderPane.setTop(new StackPane(lbl));
            borderPane.setBottom(new StackPane(bP));
            borderPane.setRight(new StackPane(btnNext));
            borderPane.setLeft(new StackPane(btnPrev));


            if (ind == 0) {
                btnPrev.setVisible(false);
            }
            if (ind == questions.size() - 1) {
                btnNext.setVisible(false);

                Button endGame = new Button("✔");
                endGame.setMinWidth(50.);
                endGame.setMinHeight(50.);
                borderPane.setRight(new StackPane(endGame));
                endGame.setOnAction(e -> {
                    answers[ind] = textField.getText();
                    window.setScene(new Scene(result(), W, H));
                });
            }

            btnNext.setOnAction(e -> {
                answers[ind] = textField.getText();
                window.setScene(new Scene(currentQuestion(ind + 1), W, H));
            });

            btnPrev.setOnAction(e -> {
                answers[ind] = textField.getText();
                window.setScene(new Scene(currentQuestion(ind - 1), W, H));
            });
        }
        if (ind == 0) {
            btnPrev.setVisible(false);
        }
        if (ind == questions.size() - 1) {
            btnNext.setVisible(false);
        }
        return borderPane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setScene(new Scene(chooseFile(), W, H));
        window.setTitle("Kahoot");
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\asasi\\IdeaProjects\\demo1\\src\\main\\java\\img\\resources\\img\\kahooticon.png");
        Image ico = new Image(fileInputStream);
        window.getIcons().add(ico);
        window.show();
    }

    public StackPane chooseFile() throws FileNotFoundException {
        StackPane sP = new StackPane();
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\asasi\\IdeaProjects\\demo1\\src\\main\\java\\img\\resources\\img\\background.jpg");
        Image image = new Image(fileInputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(W);
        imageView.setFitHeight(H);
        BorderPane bP = new BorderPane();
        bP.getChildren().addAll(imageView);
        sP.getChildren().addAll(bP);

        Button btnChoose = new Button("Choose a file");
        RadioButton btnShuffle = new RadioButton("Shuffle questions");
        Button btnExit = new Button("Exit");
        btnExit.setMinWidth(250);
        btnExit.setMinHeight(50);
        btnShuffle.setMinWidth(250);
        btnShuffle.setMinHeight(50);
        btnChoose.setMinWidth(250);
        btnChoose.setMinHeight(50);
        btnExit.setFont(Font.font("Montserrat", FontWeight.BOLD, 22));
        btnExit.setStyle("-fx-background-color: white");
        btnExit.setOpacity(0.5);
        btnChoose.setFont(Font.font("Montserrat", FontWeight.BOLD, 22));
        btnChoose.setStyle("-fx-background-color: white");
        btnChoose.setOpacity(0.5);
        btnChoose.setTextFill(Color.BLACK);
        btnShuffle.setFont(Font.font("Montserrat", FontWeight.BOLD, 22));
        btnShuffle.setStyle("-fx-background-color: white");
        btnShuffle.setOpacity(0.5);
        btnShuffle.setTextFill(Color.BLACK);

        VBox vbox1 = new VBox();
        vbox1.getChildren().addAll(btnChoose, btnShuffle, btnExit);
        vbox1.setAlignment(Pos.CENTER);
        sP.getChildren().addAll(vbox1);
        btnExit.setOnAction(event -> window.close());
        btnShuffle.setOnAction(event -> shuffleCheck = !shuffleCheck);
        btnChoose.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(window);
            try {
                questions = (ArrayList<Question>) loadFromFile(file.getName(), shuffleCheck);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            answers = new String[questions.size()];
            try {
                window.setScene(new Scene(server(), W, H));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return sP;
    }
    public StackPane result() {
        StackPane sP = new StackPane();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("C:\\Users\\asasi\\IdeaProjects\\demo1\\src\\main\\java\\img\\resources\\img\\result.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fileInputStream != null;
        Image image = new Image(fileInputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(W/2);
        imageView.setFitHeight(H/2);
        int score = 0;
        for (int i = 0; i < questions.size(); i++){
            if(questions.get(i).getAnswer().equals(answers[i]))score++;
        }

        Label res = new Label("Your result:");
        res.setFont(Font.font("Montserrat", FontWeight.BOLD,22));

        Label percent = new Label((float)(score*100)/(float)(questions.size()) + "%");
        Label number = new Label(score+"/"+questions.size()+" correct");
        Button closeButton = new Button("Close quiz");
        closeButton.setPrefSize(400,80);
        closeButton.setStyle("-fx-background-color:red");
        Button showButton = new Button("Show answer");
        showButton.setPrefSize(400, 80);
        showButton.setStyle("-fx-background-color:blue");
        VBox resBox = new VBox(res);


        imageView.fitWidthProperty().bind(imageView.fitHeightProperty().divide(0.5761));
        resBox.getChildren().addAll(percent,number,showButton,closeButton,imageView);
        resBox.setAlignment(Pos.TOP_CENTER);
        sP.getChildren().add(resBox);
        closeButton.setOnAction(event -> window.close());
        showButton.setOnAction(event -> window.setScene(new Scene(showAnswer(),W,H)));
        return sP;
    }
    public StackPane showAnswer() {
        VBox vbox1 = new VBox(2);
        Label yourLabel = new Label("Your answers:");
        yourLabel.setFont(Font.font("Montserrat", FontWeight.BOLD,22));
        vbox1.getChildren().add(yourLabel);
        for (int i = 0; i < questions.size(); i++) {
            Label ll = new Label(answers[i]);
            ll.setFont(Font.font("Montserrat", FontWeight.BOLD, 14));
            vbox1.getChildren().addAll(ll);
        }

        VBox vbox2 = new VBox(2);
        Label correctLabel = new Label("Correct answers:");
        correctLabel.setFont(Font.font("Montserrat", FontWeight.BOLD,22));
        vbox2.getChildren().add(correctLabel);
        for (Question question : questions) {
            Label ll = new Label(question.getAnswer());
            ll.setFont(Font.font("Montserrat", FontWeight.BOLD, 14));
            vbox2.getChildren().addAll(ll);
        }
        HBox hbox = new HBox(100);
        hbox.getChildren().addAll(vbox1, vbox2);
        hbox.setAlignment(Pos.CENTER);
        Button showButton = new Button("Back");
        showButton.setPrefSize(400, 60);
        showButton.setStyle("-fx-background-color:blue");
        VBox vboxMain = new VBox();
        vboxMain.getChildren().addAll(hbox, showButton);
        vboxMain.setAlignment(Pos.CENTER);
        showButton.setOnAction(event -> window.setScene(new Scene(result(),W,H)));
        StackPane sP = new StackPane();
        sP.getChildren().add(vboxMain);
        return sP;
    }
    HBox timer(){
        int[] seconds = new int[1];
        int[] minutes = new int[1];
        Text time = new Text();
        KeyFrame key = new KeyFrame(Duration.seconds(1), event -> {
            int sec = seconds[0];
            sec++;
            if(sec == 60){
                minutes[0]++;
                sec = 0;
            }
            seconds[0] = sec;
            time.setText(((minutes[0] >= 10) ? String.valueOf(minutes[0]) : "0" + minutes[0]) + ":" +
                    ((seconds[0] >= 10) ? String.valueOf(seconds[0]) : "0" + seconds[0]));
        });
        Timeline line = new Timeline(key);
        line.setCycleCount(Timeline.INDEFINITE);
        line.play();
        HBox timeBox = new HBox(time);
        timeBox.setAlignment(Pos.CENTER);
        return timeBox;
    }
}