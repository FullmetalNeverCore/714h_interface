package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;


public class MWSC extends WebSocketClient  {

    public MWSC(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Opened connection");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println(String.format("Closed connection, %s , %s",code,reason));
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

}
