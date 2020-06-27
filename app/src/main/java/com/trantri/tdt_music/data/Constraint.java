package com.trantri.tdt_music.data;

/**
 * Created by TranTien
 * Date 06/17/2020.
 */
public class Constraint {
    public interface EventBusAction {
        String PLAY = "PLAY";
        String PAUSE = "PAUSE";
        String RESUME = "RESUME";
        String NEXT = "NEXT";
        String PREVIOUS = "PREVIOUS";

        String COMPLETED = "COMPLETED";
        String PREPARED = "PREPARED";
        String LOOP = "LOOP";
        String SUFFIX = "SUFFIX";
        String SEEK = "SEEK";
        String FAIL = "SEEK";
    }

    public interface YoutubeVideo{
        String VIDEO_ID = "VIDEO_ID";
    }
    public interface SharedPref{
        String TIME_VIDEO = "TIME_VIDEO";
    }

    public interface Database{
        String DATABASE_NAME = "MUSIC";
        String NAME_BAI_HAT_YEU_THICH = "BAI_HAT_YEU_THICH";
        String NAME_PLAY_LIST = "PLAY_LIST";
        String PK_BAI_HAT_YEU_THICH = "idBaiHat";
        String PK_PLAY_LIST = "playlist_name";
    }
}
