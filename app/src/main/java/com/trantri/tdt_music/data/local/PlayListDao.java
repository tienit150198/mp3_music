package com.trantri.tdt_music.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.trantri.tdt_music.data.Constraint;

import java.util.List;

/**
 * Created by TranTien
 * Date 06/27/2020.
 */
@Dao
public interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlaylist(PlayList playList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlaylist(PlayList playList);

    @Delete
    void deletePlaylist(PlayList playList);

    @Query("SELECT * FROM " + Constraint.Database.NAME_PLAY_LIST + " WHERE " + Constraint.Database.PK_PLAY_LIST + " = :name")
    PlayList getPlaylist(String name);

    @Query("SELECT * FROM " + Constraint.Database.PK_PLAY_LIST)
    LiveData<List<PlayList>> getAllPlaylist();
}
