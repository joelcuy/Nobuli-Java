package com.example.nobulijava.model;

public class MessageObj {
    private String text;
    private boolean isBot;
    //TODO add time or maybe picture


    public MessageObj(String text, boolean isBot) {
        this.text = text;
        this.isBot = isBot;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }
}
