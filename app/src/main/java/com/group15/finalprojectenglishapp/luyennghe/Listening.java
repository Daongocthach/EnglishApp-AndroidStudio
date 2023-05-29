package com.group15.finalprojectenglishapp.luyennghe;

import java.io.Serializable;

public class Listening implements Serializable {
    private int id;
    private String topic;
    private String image;
    private String sentence;

    public Listening(int id, String topic, String image, String sentence) {
        this.id = id;
        this.topic = topic;
        this.image = image;
        this.sentence = sentence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

}
