package com.trantri.tdt_music.Model;

/**
 * Created by TranTien
 * Date 06/17/2020.
 */
public class MessageEventBus {
    public String message;
    public Object action;

    public MessageEventBus(String message, Object action) {
        this.message = message;
        this.action = action;
    }
}
