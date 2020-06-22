package com.trantri.tdt_music.Model.music;

import com.trantri.tdt_music.Model.BaiHatYeuThich;

/**
 * Created by TranTien
 * Date 06/21/2020.
 */
public class InformationMusic {
    private BaiHatYeuThich baiHatYeuThich;
    private int durationTime;
    private int position;

    @Override
    public String toString() {
        return "InformationMusic{" +
                "baiHatYeuThich=" + baiHatYeuThich +
                ", durationTime=" + durationTime +
                ", position=" + position +
                '}';
    }

    public BaiHatYeuThich getBaiHatYeuThich() {
        return baiHatYeuThich;
    }

    public void setBaiHatYeuThich(BaiHatYeuThich baiHatYeuThich) {
        this.baiHatYeuThich = baiHatYeuThich;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public InformationMusic() {
    }

    public InformationMusic(BaiHatYeuThich baiHatYeuThich, int durationTime, int position) {
        this.baiHatYeuThich = baiHatYeuThich;
        this.durationTime = durationTime;
        this.position = position;
    }
}
