package com.trantri.tdt_music.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.Model.Playlist;
import com.trantri.tdt_music.data.Constraint;

/**
 * Created by TranTien
 * Date 06/27/2020.
 */
@Database(entities = {PlayListUser.class, BaiHatYeuThich.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final Object sLock = new Object();

    private static AppDatabase mInstance;

    public abstract BaiHatYeuThichDao mBaihatBaiHatYeuThichDao();

    public abstract PlayListUserDao mPlayListUserDao();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (mInstance == null) {
                mInstance = Room.databaseBuilder(context, AppDatabase.class, Constraint.Database.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .addMigrations(MIGRATION_0_1)
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return mInstance;
        }
    }

    public void destroyDatabase() {
        mInstance = null;
    }

    private static final Migration MIGRATION_0_1 = new Migration(0, 1) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // do something when update version
        }
    };

}
