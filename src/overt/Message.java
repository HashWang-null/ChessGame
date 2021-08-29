package overt;

import java.util.HashMap;

public class Message {
    //定义的消息类型，类别为int型
    public static final int ServerMessage   = 0;
    public static final int ClientMessage   = 1;
    public static final int ServerReply     = 2;
    public static final int ClientReply     = 3;

    /*
     *@description
     *  messageType int 取已经定义的消息类型
     *  messageTitle String 消息名称
     *  info Properties 消息携带的参数
     *@extends null
     */
    private int          messageType;
    private String       messageTitle;
    private HashMap<String, String> info;

    public Message (int messageType, String messageTitle, HashMap<String, String> info) {
        this.messageType    = messageType;
        this.messageTitle   = messageTitle;
        this.info           = info;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public HashMap<String, String> getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", messageTitle='" + messageTitle + '\'' +
                ", info=" + info +
                '}';
    }
}
