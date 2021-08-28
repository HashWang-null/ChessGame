package client;


import com.google.gson.Gson;
import message.Message;

import java.util.HashMap;
import java.util.Vector;

public class Kernel {
    Client client;
    ListenerThread listener;
    GamePanel gamePanel;
    RightPanel rightPanel;
    SouthPanel southPanel;
    GameFrame gameFrame;

    private int CID = 0;
    private Gson gson = new Gson();
    private HashMap<Integer, Boolean> sent_msg_status = new HashMap<>();
    private Vector<String> summary = new Vector<>(100);

    public void setClient(Client client) {
        this.client = client;
    }

    public void setListener(ListenerThread listener) {
        this.listener = listener;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setRightPanel(RightPanel rightPanel) {
        this.rightPanel = rightPanel;
    }

    public void setSouthPanel(SouthPanel southPanel) {
        this.southPanel = southPanel;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public boolean isReady() {
        boolean
                c = (client     != null),
                l = (listener   != null),
                p = (gamePanel  != null),
                r = (rightPanel != null),
                s = (southPanel != null);
        return c && l && p && r && s;
    }

    private int getCID() {
        return ++ CID;
    }

    public void sendMessage(Message s_msg) {
        if (!isReady()) {
            return;
        }
        int CID = getCID();
        s_msg.setCID(CID);
        sent_msg_status.put(CID, Boolean.FALSE);
        client.sendMessage(gson.toJson(s_msg));
    }

    public void dealMessage(Message r_msg) {
        if (r_msg == null || !isReady()) {
            return;
        }
        int cid = r_msg.CID;
        String type = r_msg.type;
        switch (type) {
            case "ClientReply":
                dealClientReply(r_msg);
                break;
            case "ServerReply":
                dealServerReply(r_msg);
                break;
            default:
                break;
        }
    }

    private void dealClientReply(Message r_msg) {
        int cid = r_msg.CID;
        if (sent_msg_status.get(cid) == null) {
            throw new RuntimeException("收到未发送的ClientCommand的回复,CID" + cid);
        }
        sent_msg_status.replace(cid, true);
    }

    private void dealServerReply(Message r_msg) {

    }
}
