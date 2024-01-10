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
import javafx.scene.Node;


import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundPosition;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.animation.StrokeTransition;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;



import java.awt.*;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.List;
import org.java_websocket.client.WebSocketClient;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Text;

import javax.xml.crypto.Data;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class ExampleFX extends Application{

    private class Effects{
        public <T extends Node> void fadein(T obj,int dur){
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds((int)dur), obj);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();
        }

        public TextArea nonedtab(int sizex, int sizey, double opac,boolean edit){
            TextArea serverInfo = new TextArea();
            serverInfo.setEditable(edit);
            serverInfo.setPrefSize(sizex, sizey);
            serverInfo.setOpacity(opac);
            serverInfo.setWrapText(true);
            serverInfo.setStyle("-fx-text-alignment: left;display: block;" +
                    "      margin-left: auto;" +
                    "      margin-right: auto;" +
                    "      width: 90%;" +
                    "      color:red;" +
                    "      font-size:10px;");
            return serverInfo;
        }


    }


    private class Misc{
        public static Optional<String> enterIP(String ip){
            TextInputDialog dialog = new TextInputDialog(ip); // Default IP
            dialog.setTitle("Server IP Input");
            dialog.setHeaderText("Enter the Server IP Address");
            dialog.setContentText("IP:");

            Optional<String> result = dialog.showAndWait();

            return result;
        }

        public Label greeting(){


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH");
            LocalDateTime now = LocalDateTime.now();


            Label greet = new Label();

            if (Integer.parseInt(dtf.format(now)) >= 0 && Integer.parseInt(dtf.format(now)) <= 5){greet = new Label("Staying late,huh?");}
            if (Integer.parseInt(dtf.format(now)) >= 5 && Integer.parseInt(dtf.format(now)) <= 10){greet = new Label("Good morning!");}
            if (Integer.parseInt(dtf.format(now)) >= 10 && Integer.parseInt(dtf.format(now)) <= 17){greet = new Label("Have a nice day!");}
            if (Integer.parseInt(dtf.format(now)) >= 17 && Integer.parseInt(dtf.format(now)) <= 0){greet = new Label("Good evening!");}

            greet.setStyle("-fx-font-size: 30px;");

            return greet;

        }

        public void SendData(DataExchange de,String text){
            logger.debug("SENDING_DATA...");
            de.jsoCreate(text, cs.getEngine(), "null","chat","type","null","chat_exchange");
        }

        public void sendnn(CurrentSetup cs,DataExchange de,float d1,float d2,float d3){
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
    }


    private static final Logger logger = LoggerFactory.getLogger(ExampleFX.class);

    private Stage primaryStage;

    private Config config = Config.getInstance();

    private static final Stuff stuff = new Stuff();

    private String msgbuff;

    private String server_data;

    private String filter;

    private Effects fx = new Effects();

    private Misc m = new Misc();

    private CurrentSetup cs = CurrentSetup.getInstance();

    private DataExchange de = new DataExchange();

    private Map<String,String> chars;

    private ExecutorService executorService;

    private ScheduledExecutorService SexecutorService;

    private String bg;

    private boolean hidenoimg;

    private StringBuilder pingstat;



    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("714h");

        logger.info("START");

        this.hidenoimg = true;

        this.executorService = Executors.newFixedThreadPool(4);

        this.filter = "";

        this.SexecutorService = Executors.newScheduledThreadPool(4);


        m.enterIP("192.168.8.136").ifPresent(ip -> config.setIP(ip));


        this.chars = de.cl();

        if(this.chars == null){Runtime.getRuntime().exit(0);} //close program if no server route.

        this.server_data = de.neofetch();



        logger.debug(config.getIP());  //checking ip

        this.pingstat = stuff.Ping();



        logger.debug("Setting gpt3 as default engine");
        cs.setEngine("gpt3");

        login(); //login page




    }



    @Override
    public void stop() {
        logger.info("STOP");
        stuff.StopThreads(new ArrayList<ExecutorService>(Arrays.asList(this.executorService,this.SexecutorService)));
    }




    private void login() {


        StackPane root = new StackPane();


        HBox search = new HBox();
        Pane spacer = new Pane();
        Pane spacer2 = new Pane();
        HBox topContainer = new HBox();


        TextArea searchbar = new TextArea();
        searchbar.setEditable(true);
        searchbar.setPrefSize(300, 75);
        searchbar.setOpacity(0.5);

        Button srch = new Button("Search");
        srch.setOnAction(e -> {
            this.filter = searchbar.getText();
            login();
        });


        search.getChildren().addAll(searchbar,srch);

        search.setAlignment(Pos.CENTER);

        root.getChildren().add(search);
        root.setAlignment(search, Pos.CENTER);

        BorderPane layout = new BorderPane();

        FlowPane imageLayout = new FlowPane(10, 10);
        imageLayout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        VBox forImage = new VBox();


        Label eng = new Label("ENGRAMS: ");
        eng.setStyle("-fx-font-size: 40px; -fx-padding: 20 0 200 0;");

        TextArea serverInfo = fx.nonedtab(500,250,0.5,false);

        TextArea pingInfo = fx.nonedtab(500,250,0.5,false);


        serverInfo.setText(this.server_data);

        pingInfo.setText(this.pingstat.toString());

        Button ping = new Button("RePing");

        ping.setOnAction(e -> {
            this.pingstat = stuff.Ping();
            pingInfo.setText(this.pingstat.toString());
        });

        search.getChildren().add(ping);


        Label greet = m.greeting();


        executorService.submit(()->{
            Image titleLabImage = new Image("https://i.imgur.com/zsk0v7O.png", 1024, 1024, true, true, true);

            titleLabImage.progressProperty().addListener((obs,oldProgress,newProgress)->{
                if(newProgress.doubleValue() == 1.0){
                    ImageView titleLab = new ImageView(titleLabImage);
                    titleLab.setOpacity(0.0);
                    fx.fadein(titleLab,10);
                    topContainer.setAlignment(Pos.CENTER);

                    topContainer.getChildren().add(pingInfo);

                    topContainer.getChildren().add(spacer2);

                    topContainer.getChildren().addAll(new VBox(5,titleLab,greet){{setAlignment(Pos.CENTER);}});


                    topContainer.getChildren().add(spacer);

                    topContainer.getChildren().add(serverInfo);
                }
            });
        });




        for (Map.Entry<String, String> entry : chars.entrySet()) {
            String imageUrl = entry.getValue();
            String charName = entry.getKey();
            if (stuff.containsAny(charName, filter) && entry.getValue() != null && !entry.getValue().equals("false") && !entry.getValue().isEmpty()) {

                if (this.hidenoimg && imageUrl.equals("https://i.imgur.com/aiHxqKf.png")) {
                    continue;
                }

                executorService.submit(() -> {
                    Image image = new Image(imageUrl, 200, 300, true, true, true);
                    // Width, Height, Preserve Ratio, Smooth, Background Loading
                    //checking progress of downloading image
                    image.progressProperty().addListener((obs, oldProgress, newProgress) -> {
                        if (newProgress.doubleValue() == 1.0) {
                            Platform.runLater(() -> {
                                ImageView imgurl = new ImageView(image);
                                imgurl.setFitWidth(200);
                                imgurl.setFitHeight(300);
                                imgurl.setPreserveRatio(false);
                                imgurl.setOpacity(0.0);

                                fx.fadein(imgurl,5);

                                imgurl.setOnMouseClicked(event -> changeGUI(charName, imageUrl));

                                Label label = new Label(charName);
                                label.setOnMouseClicked(event -> changeGUI(charName, imageUrl));
                                VBox vb = new VBox(5, imgurl, label);
                                vb.setAlignment(Pos.CENTER);

                                imageLayout.getChildren().add(vb);
                            });
                        }
                    });
                });
            }
        }




        layout.setTop(topContainer);

        forImage.setAlignment(Pos.CENTER);

        forImage.getChildren().addAll(eng,imageLayout);


        layout.setCenter(forImage);
        Button myButton = new Button("Change host");
        myButton.setOnAction(e -> m.enterIP(config.getIP()).ifPresent(ip -> config.setIP(ip)));
        Button btnHide = new Button("Filter");
        btnHide.setOnAction(e -> {{
            this.hidenoimg = !this.hidenoimg;
            stuff.StopThreads(new ArrayList<ExecutorService>(Arrays.asList(this.executorService,this.SexecutorService)));
            this.executorService = Executors.newFixedThreadPool(4);
            this.SexecutorService = Executors.newScheduledThreadPool(4);
            login();
        }});

        ToolBar toolBar = new ToolBar(myButton);
        toolBar.getItems().addAll(btnHide,search);
        VBox topLayout = new VBox(toolBar,topContainer);
        layout.setTop(topLayout);


        ScrollPane scrollPane = new ScrollPane();


        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);


        Scene scene = new Scene(scrollPane, 1600, 900);
        scrollPane.getStyleClass().add("overlay");

        scene.getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());

        fx.fadein(scene.getRoot(),10);


        primaryStage.setScene(scene);
        primaryStage.show();

    }



    private void changeGUI(String name, String img) {
        //N
        cs.setEng(name);
        de.jsoCreate("N","toor",String.format("%s",cs.getEng()),"username","password","char","verify_credentials");


        //background


        VBox imageBox = new VBox(10);
        Label label = new Label(String.format("Engram : %s | STATUS:CONNECTED",name));
        
        executorService.submit(()->{

            Image backgroundImage = new Image(img,600,800,true,true,true);


            backgroundImage.progressProperty().addListener((obs,oldProgress,newProgress)->{
                if(newProgress.doubleValue()==1.0){
                    ImageView backgroundImageView = new ImageView(new Image(img,600,800,true,true,true));

                    Platform.runLater(()->{
                        backgroundImageView.setFitWidth(600);
                        backgroundImageView.setFitHeight(800);
                        backgroundImageView.setPreserveRatio(true);
                        backgroundImageView.setOpacity(0.0);

                        fx.fadein(backgroundImageView,5);

                        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage,
                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

                        Background background = new Background(backgroundImg);

                        backgroundImageView.setFitWidth(600);
                        backgroundImageView.setFitHeight(800);
                        backgroundImageView.setPreserveRatio(true);

                        imageBox.getChildren().addAll(label,backgroundImageView);
                        imageBox.setAlignment(Pos.CENTER);

                    });
                }
            });

        });


        MenuButton menuButton = new MenuButton("Engine");

        HashMap<String,MenuItem> options = new HashMap<>(){{
            put("gpt3",new MenuItem("gpt3"));
            put("gpt3.5-turbo",new MenuItem("gpt3.5-turbo"));
            put("Mistral",new MenuItem("Mistral"));
        }};

        for (String x : options.keySet()){
            options.get(x).setOnAction(e->cs.setEngine(x));
            menuButton.getItems().add(options.get(x));
        }


        Button sendButton = new Button("Send");

        sendButton.getStyleClass().add("button-large");

        Button hostButton = new Button("Change host");
        hostButton.setOnAction(e -> m.enterIP(config.getIP()).ifPresent(ip -> config.setIP(ip)));


        TextArea nonEditableTextArea  = fx.nonedtab(800,400,0.5,false);



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


        Button el = new Button("Engram Library");
        el.setOnAction(e -> login());
        ToolBar toolBar = new ToolBar(el);

        toolBar.getItems().add(hostButton);

        TextArea editableTextArea  = fx.nonedtab(800,200,0.5,true);


        VBox nnval = new VBox(10);

        List<TextArea> textAreas = new ArrayList<TextArea>();

        for(String x : new ArrayList<>(Arrays.asList("Temperature","Presence Penalty","Frequency Penalty"))){
            TextArea nnvals  = fx.nonedtab(1,1,0.5,true);
            nnvals.setText("1.0");

            nnval.getChildren().addAll(new Label(x),nnvals);
            textAreas.add(nnvals);

        }

        Button snn = new Button("Change NNvals");
        snn.setOnAction(e -> m.sendnn(cs,de,Float.parseFloat(textAreas.get(0).getText()), Float.parseFloat(textAreas.get(1).getText()), Float.parseFloat(textAreas.get(2).getText())));



        VBox vbox = new VBox(10);



        sendButton.setOnAction(e -> {
            String tx = editableTextArea.getText();
            m.SendData(de,tx);
        });


        vbox.getChildren().addAll(new Label("Log:"),nonEditableTextArea, editableTextArea,nnval);
        vbox.getChildren().addAll(snn,sendButton);
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


