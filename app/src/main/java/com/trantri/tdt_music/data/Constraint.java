package com.trantri.tdt_music.data;

/**
 * Created by TranTien
 * Date 06/17/2020.
 */
public class Constraint {
    public interface EventBusAction {
        String PLAY = "PLAY";
        String PAUSE = "PAUSE";
        String NEXT = "NEXT";
        String PREVIOUS = "PREVIOUS";
    }

}
