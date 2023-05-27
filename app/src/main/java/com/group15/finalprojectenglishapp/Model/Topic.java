package com.group15.finalprojectenglishapp.Model;

import java.io.Serializable;

public class Topic implements Serializable {
    private String name;
    private int image;
    private String text;
    public Topic() {
    }
    public Topic(String name, int image, String text) {
        this.name = name;
        this.image = image;
        this.text = text;
    }
    public String getName() {
        return name;
    }
    public void setName(String title) {
        this.name = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
