package client;


import com.google.gson.Gson;
import message.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

public class ListenerThread extends Thread{
    static Socket socket = null;
    BufferedWriter bw = null;
    BufferedReader br = null;

    Client client;

    Gson gson = new Gson();

    ListenerThread(Client client) {
        this.client = client;
        initConnection();
    }

    public void initConnection() {
        ListenerThread.socket = Client.socket;
        try {
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String line = null;
        while (true) {
            try {
                line = br.readLine();
                System.out.println(line);
                if (line == null) {
                    break;
                }
                Message msg = gson.fromJson(line, Message.class);
                switch (msg.type) {
                    case "ServerReply":
                        doServerReply(msg);
                        break;
                    case "ClientCommand":
                        doClientCommand(msg);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doServerReply(Message msg) {
        try {
            switch (msg.title) {
                case "ConnectionFailed":
                    GameOptionPane.showServerError(msg.info.get("info"));
                    break;
                case "PartnerFailed":
                    GameOptionPane.showPartnerError(msg.info.get("info"));
                    break;
                case "MatchFailed":
//                    GameOptionPane.showPartnerError(msg.info.get("info"));
                    if (msg.info.get("bec") != null) {
                        client.gameFrame.rightPanel.setMatchingPartner();
                    }
                    break;
                case "MatchSuccessfully":
                    client.matchPartner(msg.info.get("address"), msg.info.get("position"));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doClientCommand(Message msg) {
        try {
            switch (msg.title) {
                case "DropChess":
                    dealDropChessCommand(msg);
                    break;
                case "StartSession":
                    dealStartSessionCommand(msg);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dealDropChessCommand(Message msg) {
        int cID = Integer.parseInt(msg.info.get("cid"));
        int index_x = Integer.parseInt(msg.info.get("x"));
        int index_y = Integer.parseInt(msg.info.get("y"));
        int color = Integer.parseInt(msg.info.get("color"));
        if (client.gameFrame.gamePanel.checkDropChess(index_x, index_y, color)) {
            client.gameFrame.gamePanel.dropChess(
                    new ChessBoard.ChessStep(color, new ChessBoard.ChessPoint(index_x, index_y)));

//            Message message = new Message(
//                    "ClientReply",
//                    "DealSuccessfully",
//                    new HashMap<String, String>() {
//                        {
//                            put("cid", String.valueOf(cID));
//                        }
//                    });
//            client.sendMessage(gson.toJson(message));
        } else {
//            Message message = new Message(
//                    "ClientReply",
//                    "DealFailed",
//                    new HashMap<String, String>() {
//                        {
//                            put("cid", String.valueOf(cID));
//                            put("info", "非法位置");
//                        }
//                    });
//            client.sendMessage(gson.toJson(message));
        }
    }

    private void dealStartSessionCommand(Message msg) {
        int color = Integer.parseInt(msg.info.get("color"));
        this.client.gameFrame.gamePanel.initSession(color);
//        Message message = new Message(
//                "ClientReply",
//                "StartSessionSuccessfully"
//        );
//        this.client.sendMessage(gson.toJson(message));
    }
}
