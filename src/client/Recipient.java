package client;

import com.google.gson.Gson;
import overt.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 监听消息的类
 * extends from Thread
 */
public class Recipient extends Thread{

    private Socket socket;
    private Kernel kernel;
    private BufferedReader br;
    private Gson gson = new Gson();

    public Recipient(Socket socket, Kernel kernel) {
        this.socket = socket;
        this.kernel = kernel;
        try {
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                //接受消息并解析
                String line = br.readLine();
                System.out.println("接收：" + line);
                if (line == null) {
                    break;
                }
                Message r_msg = gson.fromJson(line, Message.class);
                //获取消息类别并分别处理
                int type = r_msg.getMessageType();
                switch (type) {
                    case Message.ServerMessage:
                        parseServerMessage(r_msg);
                        break;
                    case Message.ClientMessage:
                        parseClientMessage(r_msg);
                        break;
                    case Message.ServerReply:
                        parseServerReply(r_msg);
                        break;
                    case Message.ClientReply:
                        parseClientReply(r_msg);
                        break;
                    default:
                        System.err.println("未知消息类型 = " + type);
                        break;
                }
            } catch (Exception ignored) {

            }
        }
    }

    //---------------------------------第一级消息处理-----------------------------

    //处理服务器消息（ServerMessage）
    private void parseServerMessage(Message msg) {

    }

    //处理客户端消息（ClientMessage）
    private void parseClientMessage(Message msg) {
        try {
            String title = msg.getMessageTitle();
            switch (title) {
                case "StartSession":
                    //开战局
                    doStartSession(msg);
                    break;
                case "DropChess":
                    doDropChess(msg);
                    //落子
                    break;
                case "UndoChess":
                    doUndoChess(msg);
                    //撤销
                    break;
                case "Surrender":
                    doSurrender(msg);
                    //认输
                    break;
                case "Chat":
                    doChat(msg);
                    //聊天
                    break;
                default:
                    System.err.println("未处理的Message：" + title);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //处理服务器回复（ServerReply）
    private void parseServerReply(Message msg) {
        try {
            String title = msg.getMessageTitle();
            switch (title) {
                case "ConnectionFailed":
                    //服务器连接错误
                    kernel.showServerError(String.valueOf(msg.getInfo().get("info")));
                    break;
                case "PartnerFailed":
                    //对手出现错误
                    kernel.showPartnerError(String.valueOf(msg.getInfo().get("info")));
                    break;
                case "MatchFailed":
                    //匹配出现错误
                    if ((String.valueOf(msg.getInfo().get("bec"))).equals("NoPartner")) {
                        //等待匹配
                        kernel.waiting_partner();
                    }
                    break;
                case "MatchSuccessfully":
                    //匹配成功
                    kernel.match_partner(
                            String.valueOf(msg.getInfo().get("address")),
                            String.valueOf(msg.getInfo().get("position")).equals("host"));
                    break;
                case "ConnectionSuccessfully":
                    break;
                default:
                    System.err.println("未处理的Message：" + title);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //处理客户端回复（ClientReply）
    private void parseClientReply(Message msg) {

    }

    //----------------------------------私有方法----------------------------------

    //处理新开战局
    private void doStartSession(Message msg) {
        int color = Integer.parseInt(msg.getInfo().get("color"));
        kernel.receiveNewSession(color);
    }

    //处理落子
    private void doDropChess(Message msg) {
        int color = Integer.parseInt(msg.getInfo().get("color"));
        int x = Integer.parseInt(msg.getInfo().get("x"));
        int y = Integer.parseInt(msg.getInfo().get("y"));
        kernel.p_dropChess(color, x, y);
    }

    //处理撤销
    private void doUndoChess(Message msg) {
        kernel.undoDropChess(false);
    }

    //处理投降
    private void doSurrender(Message msg) {

    }
    //处理聊天
    private void doChat(Message msg) {
        kernel.doChat(msg.getInfo().get("content"), false);
    }

    //释放资源
    private void releaseResources() {
        try {
            if (this.br != null) {
                this.br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
