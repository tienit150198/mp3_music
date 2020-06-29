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
public interface PlayListUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlaylist(PlayListUser playListUser);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlaylist(PlayListUser playListUser);

    @Delete
    void deletePlaylist(PlayListUser playListUser);

    @Query("SELECT * FROM " + Constraint.Database.NAME_PLAY_LIST + " WHERE " + Constraint.Database.PK_PLAY_LIST + " = :name")
    PlayListUser getPlaylist(String name);

    @Query("SELECT * FROM " + Constraint.Database.NAME_PLAY_LIST)
    LiveData<List<PlayListUser>> getAllPlaylist();
}
