package com.trantri.tdt_music.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.trantri.tdt_music.Model.music.InformationMusic;
import com.trantri.tdt_music.data.Constraint;

import java.io.Serializable;

@Entity(tableName = Constraint.Database.NAME_BAI_HAT_YEU_THICH)
public class BaiHatYeuThich{
    @SerializedName("IdBaiHat")
    @Expose
    @ColumnInfo(name = "idBaiHat")
    private String idBaiHat;
    @SerializedName("TenBaiHat")
    @Expose
    @NonNull
    @PrimaryKey
    private String tenBaiHat;
    @SerializedName("HinhBaiHat")
    @Expose
    private String hinhBaiHat;
    @SerializedName("CaSi")
    @Expose
    private String caSi;
    @SerializedName("LinkBaiHat")
    @Expose
    private String linkBaiHat;
    @SerializedName("LuotThich")
    @Expose
    private String luotThich;

    boolean isLiked;


    public BaiHatYeuThich() {
        isLiked = false;
    }

    public BaiHatYeuThich(String idBaiHat, @NonNull String tenBaiHat, String hinhBaiHat, String caSi, String linkBaiHat, String luotThich, boolean isLiked) {
        this.idBaiHat = idBaiHat;
        this.tenBaiHat = tenBaiHat;
        this.hinhBaiHat = hinhBaiHat;
        this.caSi = caSi;
        this.linkBaiHat = linkBaiHat;
        this.luotThich = luotThich;
        this.isLiked = isLiked;
    }

    public BaiHatYeuThich(String idBaiHat, @NonNull String tenBaiHat, String hinhBaiHat, String caSi, String linkBaiHat, String luotThich) {
        this.idBaiHat = idBaiHat;
        this.tenBaiHat = tenBaiHat;
        this.hinhBaiHat = hinhBaiHat;
        this.caSi = caSi;
        this.linkBaiHat = linkBaiHat;
        this.luotThich = luotThich;
        this.isLiked = false;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }


    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getHinhBaiHat() {
        return hinhBaiHat;
    }

    public void setHinhBaiHat(String hinhBaiHat) {
        this.hinhBaiHat = hinhBaiHat;
    }

    public String getCaSi() {
        return caSi;
    }

    public void setCaSi(String caSi) {
        this.caSi = caSi;
    }

    public String getLinkBaiHat() {
        return linkBaiHat;
    }

    public void setLinkBaiHat(String linkBaiHat) {
        this.linkBaiHat = linkBaiHat;
    }

    public String getLuotThich() {
        return luotThich;
    }

    public void setLuotThich(String luotThich) {
        this.luotThich = luotThich;
    }

    @Override
    public String toString() {
        return "BaiHatYeuThich{" +
                "idBaiHat='" + idBaiHat + '\'' +
                ", tenBaiHat='" + tenBaiHat + '\'' +
                ", hinhBaiHat='" + hinhBaiHat + '\'' +
                ", caSi='" + caSi + '\'' +
                ", linkBaiHat='" + linkBaiHat + '\'' +
                ", luotThich='" + luotThich + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaiHatYeuThich)) return false;

        BaiHatYeuThich that = (BaiHatYeuThich) o;

        if (!idBaiHat.equals(that.idBaiHat)) return false;
        if (tenBaiHat != null ? !tenBaiHat.equals(that.tenBaiHat) : that.tenBaiHat != null)
            return false;
        if (hinhBaiHat != null ? !hinhBaiHat.equals(that.hinhBaiHat) : that.hinhBaiHat != null)
            return false;
        if (caSi != null ? !caSi.equals(that.caSi) : that.caSi != null) return false;
        if (linkBaiHat != null ? !linkBaiHat.equals(that.linkBaiHat) : that.linkBaiHat != null)
            return false;
        return luotThich != null ? luotThich.equals(that.luotThich) : that.luotThich == null;
    }

    @Override
    public int hashCode() {
        int result = idBaiHat.hashCode();
        result = 31 * result + (tenBaiHat != null ? tenBaiHat.hashCode() : 0);
        result = 31 * result + (hinhBaiHat != null ? hinhBaiHat.hashCode() : 0);
        result = 31 * result + (caSi != null ? caSi.hashCode() : 0);
        result = 31 * result + (linkBaiHat != null ? linkBaiHat.hashCode() : 0);
        result = 31 * result + (luotThich != null ? luotThich.hashCode() : 0);
        return result;
    }
}