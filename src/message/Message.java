package message;


import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Message {
    public int CID;
    public String type;
    public String title;
    public HashMap<String, String> info;

    public Message (String type, String title) {
        this.title = title;
        this.type = type;
        this.info = null;
    }

    public Message (String type, String title, HashMap<String, String> info) {
        this.title = title;
        this.type = type;
        this.info = info;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
