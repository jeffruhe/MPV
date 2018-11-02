package com.example.hara.learninguimusicapp;

public class Song {
    private long id;
    private String name,
            artist;

    public Song(long id, String name, String artist) {
        this.id = id;
        this.name = name;
        this.artist = artist;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
