package com.trantri.tdt_music.data.local;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trantri.tdt_music.Model.BaiHatYeuThich;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by TranTien
 * Date 06/27/2020.
 */

@Entity
public class PlayList {
    @PrimaryKey
    @ColumnInfo (name = "playlist_name")
    private String name;
    @Embedded
    private List<BaiHatYeuThich> listBaiHatYeuThich;

    private String strListBaiHatYeuThich;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BaiHatYeuThich> getStrListBaiHatYeuThich() {
        Type baseTyle = new TypeToken<List<BaiHatYeuThich>>() {
        }.getType();
        return new Gson().fromJson(strListBaiHatYeuThich, baseTyle);
    }

    public void setStrListBaiHatYeuThich(List<BaiHatYeuThich> listBaiHatYeuThich) {
        this.strListBaiHatYeuThich = new Gson().toJson(listBaiHatYeuThich);
    }

    public List<BaiHatYeuThich> getListBaiHatYeuThich() {
        return listBaiHatYeuThich;
    }

    public void setListBaiHatYeuThich(List<BaiHatYeuThich> listBaiHatYeuThich) {
        this.listBaiHatYeuThich = listBaiHatYeuThich;
    }

    public PlayList() {
    }

    public PlayList(String name, List<BaiHatYeuThich> listBaiHatYeuThich) {
        this.name = name;
        this.listBaiHatYeuThich = listBaiHatYeuThich;
    }
}
