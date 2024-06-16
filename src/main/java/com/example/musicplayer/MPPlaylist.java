package com.example.musicplayer;

import java.util.ArrayList;

public class MPPlaylist
{
    private String playlistName;
    private ArrayList<MPSong> songList;
    private int songNumber;
    private String paz;

    public MPPlaylist()
    {
        playlistName = "Новый плейлист";
        songList = new ArrayList<>();
        songNumber = 0;
        paz = "";
    }

    public String getPlaylistName()
    {
        return playlistName;
    }

    public ArrayList<MPSong> getSongList()
    {
        return songList;
    }

    public int getSongNumber()
    {
        return songNumber;
    }

    public String getPaz()
    {
        return paz;
    }

    public void setPlaylistName(String a)
    {
        playlistName = a;
    }

    public void setPaz(String a) {
        paz = a;
    }

    public void addSong(MPSong a)
    {
        if (songList.contains(a))
            System.out.println("Такая песня уже есть в плейлисте");
        else
        {
            songList.add(a);
            songNumber++;
        }
    }

    public void removeSong(MPSong a)
    {
        if (songList.contains(a))
        {
            songList.remove(a);
            songNumber--;
        }
        else
            System.out.println("Такой песни нет в плейлисте");
    }

    public void printPlayList()
    {
        for (int i=0; i<songNumber; i++)
        {
            System.out.println(i+1 + ". " + songList.get(i).getAuthor() + " - " + songList.get(i).getSongName() + "| Длительность: " + songList.get(i).getDuration()/60 + ":" + songList.get(i).getDuration()%60);
        }
        if (songList.isEmpty())
            System.out.println("В плейлисте нет песен");
    }
}
