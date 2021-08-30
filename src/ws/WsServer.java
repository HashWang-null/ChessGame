package ws;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * @author lonelyinnovator
 * @date 2021/8/30
 */
public class WsServer extends WebSocketServer {
    public WsServer(int port) {
        super(new InetSocketAddress(port));
    }

    public WsServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(org.java_websocket.WebSocket conn, ClientHandshake handshake) {
        // ws连接的时候触发的代码，onOpen中我们不做任何操作

    }

    @Override
    public void onClose(org.java_websocket.WebSocket conn, int code, String reason, boolean remote) {
        //断开连接时候触发代码
        userLeave(conn);
        System.out.println(reason);
    }

    @Override
    public void onMessage(org.java_websocket.WebSocket conn, String message) {
        String msg = "收到信息：" + message;
        System.out.println(msg);
        userJoin(conn, message);//用户加入
        WsUtil.sendMessageToUser(conn, msg);
    }


    @Override
    public void onError(org.java_websocket.WebSocket conn, Exception ex) {
        //错误时候触发的代码
        System.out.println("on error");
        ex.printStackTrace();
    }

    /**
     * 去除掉失效的websocket链接
     *
     * @param conn
     */
    private void userLeave(org.java_websocket.WebSocket conn) {
        WsUtil.removeUser(conn);
    }

    /**
     * 将websocket加入用户池
     *
     * @param conn
     * @param userName
     */
    private void userJoin(org.java_websocket.WebSocket conn, String userName) {
        WsUtil.addUser(userName, conn);
    }

}
