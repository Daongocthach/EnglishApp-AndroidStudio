package com.group15.finalprojectenglishapp.ui.home;

public class Word {
    String word;
    String type;
    String meaning;

    public Word(String word, String type, String meaning) {
        this.word = word;
        this.type = type;
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
