package com.example.credapptest.models;

public class CardType {
    public int[] format;
    public int length = 0;
    public String regex = "";
    public int resource;

    public CardType(int[] format, int length, String regex, int resource) {
        this.format = format;
        this.length = length;
        this.regex = regex;
        this.resource = resource;
    }
}
