package com.example.musicplayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreatePlaylist {
    public MP MPlayer;
    @FXML
    public AnchorPane window;
    @FXML
    TextField name;
    public void createPlaylist() throws IOException
    {
        String newname;
        MPPlaylist temp = new MPPlaylist();
        newname = name.getText();
        if (!newname.isBlank())
            temp.setPlaylistName(newname);
        temp.setPaz(System.getProperty("user.dir") + "\\Resources\\Playlists\\" + temp.getPlaylistName() + ".txt");
        MPlayer.getPlaylist().add(temp);
        try {
            Files.createFile(Paths.get(temp.getPaz()));
        }
        catch (IOException e)
        {
            throw e;
        }
        FXMLLoader a = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        a.load();
        HelloController b = a.getController();
        b.musicPlayer = MPlayer;
        Stage exit = (Stage) window.getScene().getWindow();
        exit.close();
    }
}
