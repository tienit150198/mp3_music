package com.trantri.tdt_music.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.data.Constraint;

import java.util.List;

/**
 * Created by TranTien
 * Date 06/27/2020.
 */
@Dao
public interface BaiHatYeuThichDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBaiHatYeuThich(BaiHatYeuThich baiHatYeuThich);

    @Delete
    void deleteBaiHatYeuThich(BaiHatYeuThich baiHatYeuThich);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBaiHatYeuThich(BaiHatYeuThich baiHatYeuThich);

    @Query("SELECT * FROM " + Constraint.Database.NAME_BAI_HAT_YEU_THICH + " WHERE " + Constraint.Database.PK_BAI_HAT_YEU_THICH + " = :idBaiHat")
    BaiHatYeuThich getBaiHatYeuThich(int idBaiHat);

    @Query("SELECT * FROM " + Constraint.Database.NAME_BAI_HAT_YEU_THICH)
    LiveData<List<BaiHatYeuThich>> getAllBaiHatYeuThich();
}
