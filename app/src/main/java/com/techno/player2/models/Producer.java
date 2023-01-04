package com.techno.player2.models;


public class Producer {
    public Producer(String name,  String link) {
        this.link = link;
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    String link;
    String name;

}