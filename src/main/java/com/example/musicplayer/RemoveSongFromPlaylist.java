package com.example.musicplayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RemoveSongFromPlaylist {
    public MP MPlayer;
    public MPPlaylist destination;
    @FXML
    private VBox SongsToRemove;
    @FXML
    public AnchorPane window;
    @FXML
    public void onLoad() {
        SongsToRemove.getChildren().clear();
        destination.getSongList().forEach(x->
                {
                    Button newBut = new Button(x.getAuthor() + " - " + x.getSongName());
                    newBut.setMinHeight(25);
                    newBut.setMinWidth(322);
                    newBut.setOnAction(event -> {try {removeFromPlaylist(x);} catch (IOException e) {
                        try {
                            throw e;
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }});
                    newBut.setTooltip(new Tooltip(x.getAuthor() + " - " + x.getSongName()));
                    SongsToRemove.getChildren().add(newBut);
                }
        );
    }
    @FXML
    protected void removeFromPlaylist(MPSong song) throws IOException
    {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(System.getProperty("user.dir"), "Resources", "Playlists", "temp.txt"))) {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(destination.getPaz())))
            {
                String removeIt = song.getPaz();
                String currentLine;
                while ((currentLine = reader.readLine()) != null)
                {
                    if (currentLine.trim().equals(removeIt))
                    {
                        continue;
                    }
                    writer.write(currentLine + System.lineSeparator());
                }
            }
            catch (IOException e)
            {
                throw e;
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        try {
            Files.delete(Paths.get(destination.getPaz()));
        }
        catch (IOException e)
        {
            throw e;
        }
        try {
            Files.move(Paths.get(System.getProperty("user.dir"), "Resources", "Playlists", "temp.txt"), Paths.get(destination.getPaz()));
        }
        catch (IOException e)
        {
            throw e;
        }
        destination.removeSong(song);
        FXMLLoader a = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        a.load();
        HelloController b = a.getController();
        if (song.equals(MPlayer.getCurrentSong()))
        {
            MPlayer.setCurrentPlaylist(null);
            MPlayer.setCurrentSong(null);
        }
        b.musicPlayer = MPlayer;
        Stage exit = (Stage) window.getScene().getWindow();
        exit.close();
    }
}
