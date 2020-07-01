package com.trantri.tdt_music.Model;

/**
 * Created by TranTien
 * Date 07/01/2020.
 */
public class Banner {
    private String image;
    private String name;

    @Override
    public String toString() {
        return "Banner{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Banner() {

    }

    public Banner(String image, String name) {
        this.image = image;
        this.name = name;
    }
}
