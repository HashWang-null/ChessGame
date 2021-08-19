package client;


import com.google.gson.Gson;
import message.Message;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    Gson gson = new Gson();
    static Socket socket = null;
    BufferedReader br = null;
    BufferedWriter bw = null;

    GameFrame gameFrame = null;
    ListenerThread listener;

    boolean isMatched;
    boolean isConnected;
    String partnerInfo;
    String position;

    public void setFrame(GameFrame frame) {
        this.gameFrame = frame;
    }

    public void setListener(ListenerThread listener) {
        this.listener = listener;
    }

    Client () {
    }

    public void connectionToServer() {
        try {
            Client.socket = new Socket("101.132.73.96", 9876);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ListenerThread listener = new ListenerThread(this);
            listener.start();
        } catch (SocketException se) {
            GameOptionPane.showNetError();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String line) {
        if (socket == null || socket.isClosed()) {
            GameOptionPane.showNetError();
            return;
        }
        try {
            bw.write(line + "\n");
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestPartner() {
        try {
            Message msg = new Message(
                    "ServerCommand",
                    "RequestPartner"
            );
            sendMessage(gson.toJson(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void matchPartner(String partnerAddress, String position) {
        if (position.equals("guest")) {
            this.position = position;
            this.isMatched = true;
            this.gameFrame.rightPanel.setPartner(partnerAddress);
        } else if (position.equals("host")){
            this.position = position;
            this.isMatched = true;
            this.gameFrame.rightPanel.setPartner(partnerAddress);
        } else {
            throw new RuntimeException();
        }
    }
}
