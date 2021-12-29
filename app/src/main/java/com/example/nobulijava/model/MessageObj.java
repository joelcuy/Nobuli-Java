package com.example.nobulijava.model;

public class MessageObj {
    private String text;
    private boolean isBot;
    private String intent;


    public MessageObj(String text, boolean isBot) {
        this.text = text;
        this.isBot = isBot;
    }

    public MessageObj(String text, boolean isBot, String intent) {
        this.text = text;
        this.isBot = isBot;
        this.intent = intent;
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

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }
}
