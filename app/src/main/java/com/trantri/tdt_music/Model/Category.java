package com.trantri.tdt_music.Model;

import androidx.annotation.DrawableRes;

/**
 * Created by TranTien
 * Date 07/01/2020.
 */
public class Category {
    private int iconId;
    private String name;

    @Override
    public String toString() {
        return "Category{" +
                "iconId=" + iconId +
                ", name='" + name + '\'' +
                '}';
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category(@DrawableRes int iconId, String name) {
        this.iconId = iconId;
        this.name = name;
    }

    public Category() {
    }
}
