package com.trantri.tdt_music.Model;

import java.util.List;

/**
 * Created by TranTien
 * Date 07/01/2020.
 */
public class SubPlaylist {
    private String name;
    private List<Category> category;

    @Override
    public String toString() {
        return "SubPlaylist{" +
                "name='" + name + '\'' +
                ", category=" + category +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public SubPlaylist(String name, List<Category> category) {
        this.name = name;
        this.category = category;
    }

    public SubPlaylist() {
    }
}
