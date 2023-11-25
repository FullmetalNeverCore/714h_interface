package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Rectangle;
import javafx.application.Platform;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.*;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundPosition;


import java.awt.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.List;
import java.util.ArrayList;


import javax.xml.crypto.Data;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;





public class ExampleFX extends Application{

    private Stage primaryStage;

    private Config config = Config.getInstance();

    private String msgbuff;

    private CurrentSetup cs = CurrentSetup.getInstance();

    private DataExchange de = new DataExchange();

    private Map<String,String> chars;

    private ExecutorService executorService;

    private ScheduledExecutorService SexecutorService;

    private String bg;

    private boolean hidenoimg;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("714h");

        this.hidenoimg = true;

        this.executorService = Executors.newFixedThreadPool(4);

        this.SexecutorService = Executors.newScheduledThreadPool(4);


        enterIP("192.168.8.111").ifPresent(ip -> config.setIP(ip));

        this.chars = de.cl();

        System.out.println(config.getIP());  //checking ip

        login(); //login page

        System.out.println("Setting gpt3 as default engine");
        cs.setEngine("gpt3");

        primaryStage.show();



    }

    @Override
    public void stop() {
        System.out.println("Shutting down...");
        for (ExecutorService x : new ArrayList<ExecutorService>(Arrays.asList(this.executorService,this.SexecutorService))){
            if (x != null) {
                x.shutdown();
                try {
                    if (!x.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                        x.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    x.shutdownNow();
                }
            }
        }
    }

    public static Optional<String> enterIP(String ip){
        TextInputDialog dialog = new TextInputDialog(ip); // Default IP
        dialog.setTitle("Server IP Input");
        dialog.setHeaderText("Enter the Server IP Address");
        dialog.setContentText("IP:");

        Optional<String> result = dialog.showAndWait();

        return result;
    }

    private void login() {
        BorderPane layout = new BorderPane();
        FlowPane imageLayout = new FlowPane(10, 10);


        Label titleLabel = new Label("MIKOSHI");
        Label titledownLabel = new Label("God in his heaven,all right with the world.");
        titledownLabel.setStyle("-fx-font-size: 20px; -fx-padding: 20 0 200 0;");
        titleLabel.setStyle("-fx-font-size: 80px; -fx-padding: 20 0 200 0;"); // Adjust font size and padding as needed
        VBox topContainer = new VBox(titleLabel,titledownLabel);
        topContainer.setAlignment(Pos.CENTER);

        layout.setTop(topContainer); // Set the top container with the title


        for (Map.Entry<String, String> entry : chars.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().equals("false") && !entry.getValue().isEmpty()) {

                String imageUrl = entry.getValue();
                String charName = entry.getKey();


                if (this.hidenoimg && imageUrl.equals("https://i.imgur.com/aiHxqKf.png")){continue;}

                executorService.submit(() -> {
                    Image image = new Image(imageUrl, 200, 300, true, true, true);
                    // Width, Height, Preserve Ratio, Smooth, Background Loading
                    Platform.runLater(() -> {
                        ImageView imgurl = new ImageView(image);
                        imgurl.setFitWidth(200);
                        imgurl.setFitHeight(300);
                        imgurl.setPreserveRatio(false);
                        imgurl.setOnMouseClicked(event -> changeGUI(charName, imageUrl));

                        Label label = new Label(charName);
                        VBox vb = new VBox(5, imgurl, label);
                        vb.setAlignment(Pos.CENTER);

                        imageLayout.getChildren().add(vb);
                    });
                });
            }
        }

        layout.setCenter(imageLayout);
        Button myButton = new Button("Change host");
        myButton.setOnAction(e -> enterIP(config.getIP()).ifPresent(ip -> config.setIP(ip)));
        Button btnHide = new Button("Filter");
        btnHide.setOnAction(e -> {{
            this.hidenoimg = !this.hidenoimg;
            System.out.println("Shutting down previous threads...");
            for (ExecutorService x : new ArrayList<ExecutorService>(Arrays.asList(this.executorService,this.SexecutorService))){
                if (x != null) {
                    x.shutdown();
                    try {
                        if (!x.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                            x.shutdownNow();
                        }
                    } catch (InterruptedException y) {
                        x.shutdownNow();
                    }
                }
            }
            this.executorService = Executors.newFixedThreadPool(4);
            this.SexecutorService = Executors.newScheduledThreadPool(4);
            login();
        }});
        ToolBar toolBar = new ToolBar(myButton);
        toolBar.getItems().add(btnHide);
        VBox topLayout = new VBox(toolBar,topContainer);
        layout.setTop(topLayout);



        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        Scene scene = new Scene(scrollPane, 800, 600);
        scrollPane.getStyleClass().add("overlay");
        scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());
        primaryStage.setScene(scene);
    }


    private void SendData(String text){
        System.out.println("SENDING_DATA...");
        de.jsoCreate(text, cs.getEngine(), "null","chat","type","null","chat_exchange");
    }

    private void sendnn(float d1,float d2,float d3){
        cs.setRnd(d1);
        cs.setFpen(d2);
        cs.setPpen(d3);
        Map<String,String> xyz = new HashMap<>(){{
           put("rnd",Float.toString(d1));
           put("fpen",Float.toString(d2));
           put("ppen",Float.toString(d3));
        }};
        for (String x : Arrays.asList("rnd","fpen","ppen")){

            de.jsoCreate(xyz.get(x),x,"null","val","type","null","nn_vals");
        }
    }

    private void changeGUI(String name, String img) {
        //N
        cs.setEng(name);
        de.jsoCreate("N","toor",String.format("%s",cs.getEng()),"username","password","char","verify_credentials");


        //background

        Image backgroundImage = new Image(img);

        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

        Background background = new Background(backgroundImg);

        MenuButton menuButton = new MenuButton("Engine");

        MenuItem option1 = new MenuItem("gpt3");
        option1.setOnAction(e->cs.setEngine("gpt3"));

        MenuItem option2 = new MenuItem("gpt3.5-turbo");
        option2.setOnAction(e->cs.setEngine("gpt3.5-turbo"));

        menuButton.getItems().addAll(option1, option2);

        Button sendButton = new Button("Send");

        sendButton.getStyleClass().add("button-large");

        Button hostButton = new Button("Change host");
        hostButton.setOnAction(e -> enterIP(config.getIP()).ifPresent(ip -> config.setIP(ip)));


        TextArea nonEditableTextArea = new TextArea();
        nonEditableTextArea.setEditable(false);
        nonEditableTextArea.setPrefSize(800, 400);
        nonEditableTextArea.setMinSize(500, 400);
        nonEditableTextArea.setOpacity(0.5);



        SexecutorService.scheduleAtFixedRate(() -> {
            Object tval = de.msgbuff().get("text"); // Get buffer
            StringBuilder tempms = new StringBuilder();
            if (tval instanceof List){
                List<?> list = (List<?>) tval;
                for (Object x : list){
                    if (x instanceof String){
                        tempms.append((String) x).append(" ");
                    }
                }
            }
            String combinedString = tempms.toString();
            String[] splitArray = combinedString.split(String.format("%s:",name));
            StringBuffer msgchat = new StringBuffer();
            if(splitArray.length != 0){
                for (int x = 0;x < splitArray.length;x++){
                    if (x == splitArray.length-1){
                        msgchat.append(String.format("\n%s:%s",name,splitArray[x]));
                    }
                    else{
                        msgchat.append(splitArray[x]);
                    }
                }
            }
            else{
                msgchat.append(splitArray[0]);
            }
            nonEditableTextArea.setText(msgchat.toString());
        }, 0, 5, TimeUnit.SECONDS);


        ScrollPane scrollPane = new ScrollPane();
        Label label = new Label(String.format("Engram : %s",name));

        Button el = new Button("Engram Library");
        el.setOnAction(e -> login());
        ToolBar toolBar = new ToolBar(el);

        toolBar.getItems().add(hostButton);

        ImageView backgroundImageView = new ImageView(new Image(img));
        backgroundImageView.setFitWidth(600);
        backgroundImageView.setFitHeight(800);
        backgroundImageView.setPreserveRatio(true);


        TextArea editableTextArea = new TextArea();
        editableTextArea.setPrefSize(800, 200);
        editableTextArea.setMinSize(400, 200);
        editableTextArea.setOpacity(0.5);


        VBox nnval = new VBox(10);

        List<TextArea> textAreas = new ArrayList<TextArea>();

        for (int x = 0;x<3;x++){
            TextArea nnvals = new TextArea();
            nnvals.setText("1.0");
            nnvals.setPrefSize(1, 1);
            nnvals.setOpacity(0.5);

            nnval.getChildren().add(nnvals);
            textAreas.add(nnvals);

        }
        Button snn = new Button("Change NNvals");
        snn.setOnAction(e -> sendnn(Float.parseFloat(textAreas.get(0).getText()), Float.parseFloat(textAreas.get(1).getText()), Float.parseFloat(textAreas.get(2).getText())));

        nnval.getChildren().add(snn);


        VBox vbox = new VBox(10);


        VBox imageBox = new VBox(10);

        imageBox.getChildren().addAll(label,backgroundImageView);
        imageBox.setAlignment(Pos.CENTER);

        sendButton.setOnAction(e -> {
            String tx = editableTextArea.getText();
            SendData(tx);
        });


        vbox.getChildren().addAll(nonEditableTextArea, editableTextArea,nnval);
        vbox.getChildren().add(sendButton);
        vbox.getChildren().add(menuButton);
        vbox.setAlignment(Pos.CENTER);


        StackPane leftPane = new StackPane();
        leftPane.setPrefWidth(2000);
        leftPane.getChildren().add(vbox);

        leftPane.setAlignment(Pos.CENTER);

        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.getChildren().addAll(leftPane, imageBox);

        BorderPane root = new BorderPane();
        root.setTop(toolBar);
        root.setCenter(hbox);

        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);


        primaryStage.getScene().setRoot(scrollPane);

    }

}
