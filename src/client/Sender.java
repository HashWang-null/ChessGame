package client;


import com.google.gson.Gson;
import overt.Message;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

/**
 * 负责发送消息的类
 *
 */
public class Sender {
    private Gson gson = new Gson();
    private Socket socket = null;
    private BufferedWriter bw = null;

    public Sender(Socket socket) {
        try {
            this.socket = socket;
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----------------------------公开方法-----------------------------------------

    //发送请求匹配的消息
    public void sendRequestPartner() {
        Message message = new Message(
                Message.ServerMessage,
                "RequestPartner",
                null
        );
        sendMessage(gson.toJson(message));
    }

    //发送启动战局的消息，仅当身份为主持人（host）时使用
    public void sendStartSession(int color) {
        Message message = new Message(
                Message.ClientMessage,
                "StartSession",
                new HashMap<String, String>() {
                    {
                        put("color", String.valueOf(color));
                    }
                }
        );
        sendMessage(gson.toJson(message));
    }

    //发送落子操作的消息，需要color（颜色），index_x（X坐标），index_y（Y坐标）
    public void sendDropChess(int color, int index_x, int index_y) {
        Message message = new Message(
                Message.ClientMessage,
                "DropChess",
                new HashMap<String, String>() {
                    {
                        put("color", String.valueOf(color));
                        put("x", String.valueOf(index_x));
                        put("y", String.valueOf(index_y));
                    }
                }
        );
        sendMessage(gson.toJson(message));
    }

    //发送撤销落子的消息
    public void sendUndoChess() {
        Message message = new Message(
                Message.ClientMessage,
                "UndoChess",
                null
        );
        sendMessage(gson.toJson(message));
    }

    //发送投降的消息
    public void sendSurrender() {
        Message message = new Message(
                Message.ClientMessage,
                "Surrender",
                null
        );
        sendMessage(gson.toJson(message));
    }

    //发送聊天消息
    public void sendChat(String line) {
        Message message = new Message(
                Message.ClientMessage,
                "Chat",
                new HashMap<String, String>() {
                    {
                        put("content", line);
                    }
                }
        );
        sendMessage(gson.toJson(message));
    }

    //-----------------------------私有方法-----------------------------------------

    //发送消息
    private void sendMessage(String line) {
        try {
            System.out.println("发送：" + line);
            this.bw.write(line + "\n");
            this.bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //释放资源
    private void releaseSource() {
        try {
            if (this.bw != null) {
                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
