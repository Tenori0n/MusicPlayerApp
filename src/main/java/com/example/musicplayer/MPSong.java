package com.example.musicplayer;

public class MPSong {
    private String songName;
    private String author;
    private int duration;
    private String paz;

    public MPSong() {
        songName = "Без названия";
        author = "Неизвестен";
        duration = 0;
        paz = "";
    }

    public String getSongName() {
        return songName;
    }

    public String getAuthor() {
        return author;
    }

    public int getDuration()
    {
        return duration;
    }

    public String getPaz()
    {
        return paz;
    }

    public void setSongName(String a)
    {
        songName = a;
    }

    public void setAuthor(String a)
    {
        author = a;
    }

    public void setDuration(int a)
    {
        duration = a;
    }

    public void setPaz(String a)
    {
        paz = a;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof MPSong))
            return false;
        MPSong other = (MPSong)o;
        return (this.getSongName().equals(other.getSongName())) && (this.getAuthor().equals(other.getAuthor())) && (this.getPaz().equals(other.getPaz()));
    }
}
