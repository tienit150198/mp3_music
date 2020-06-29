package com.trantri.tdt_music.data.local;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trantri.tdt_music.Model.BaiHatYeuThich;
import com.trantri.tdt_music.data.Constraint;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by TranTien
 * Date 06/27/2020.
 */

@Entity(tableName = Constraint.Database.NAME_PLAY_LIST)
public class PlayListUser {
    @PrimaryKey
    @NonNull
    private String name;
    @Ignore
    private List<BaiHatYeuThich> listBaiHatYeuThich;

    @ColumnInfo(name = "listBaiHatYeuThich")
    private String strListBaiHatYeuThich;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getStrListBaiHatYeuThich() {
        return strListBaiHatYeuThich;
    }

    public void setStrListBaiHatYeuThich(String strListBaiHatYeuThich) {
        this.strListBaiHatYeuThich = strListBaiHatYeuThich;
    }

    public void setStrListBaiHatYeuThich(List<BaiHatYeuThich> listBaiHatYeuThich) {
        this.strListBaiHatYeuThich = new Gson().toJson(listBaiHatYeuThich);
    }

    public List<BaiHatYeuThich> getListBaiHatYeuThich() {
        Type type = new TypeToken<List<BaiHatYeuThich>>() {
        }.getType();

        return new Gson().fromJson(getStrListBaiHatYeuThich(), type);
    }

    public void setListBaiHatYeuThich(List<BaiHatYeuThich> listBaiHatYeuThich) {
        this.strListBaiHatYeuThich = new Gson().toJson(listBaiHatYeuThich);
        this.listBaiHatYeuThich = listBaiHatYeuThich;
    }


    public PlayListUser(@NonNull String name, List<BaiHatYeuThich> listBaiHatYeuThich, String strListBaiHatYeuThich) {
        this.name = name;
        this.listBaiHatYeuThich = listBaiHatYeuThich;
        this.strListBaiHatYeuThich = strListBaiHatYeuThich;
    }

    @Override
    public String toString() {
        return "PlayListUser{" +
                "name='" + name + '\'' +
                ", listBaiHatYeuThich=" + listBaiHatYeuThich +
                ", strListBaiHatYeuThich='" + strListBaiHatYeuThich + '\'' +
                '}';
    }

    public PlayListUser() {
        name = "";
    }
}
