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

public class RemoveSongFromPlayer {
    public MP MPlayer;
    @FXML
    private VBox SongsToDelete;
    @FXML
    private AnchorPane window;
    @FXML
    public void onLoad() {
        SongsToDelete.getChildren().clear();
         MPlayer.getPlaylist().getFirst().getSongList().forEach(x->
                 {
                     Button newBut = new Button(x.getAuthor() + " - " + x.getSongName());
                     newBut.setMinHeight(25);
                     newBut.setMinWidth(322);
                     newBut.setOnAction(event -> {try {removeSong(x);} catch (IOException e) {
                         try {
                             throw e;
                         } catch (IOException ex) {
                             throw new RuntimeException(ex);
                         }
                     }});
                     newBut.setTooltip(new Tooltip(x.getAuthor() + " - " + x.getSongName()));
                     SongsToDelete.getChildren().add(newBut);
                 }
         );
    }
    public void removeSong(MPSong a) throws IOException
    {
        MPlayer.getPlaylist().forEach(x -> {
            if (x != MPlayer.getPlaylist().getFirst())
            {
                if (x.getSongList().contains(a)) {
                    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(System.getProperty("user.dir"), "Resources", "Playlists", "temp.txt"))) {
                        try (BufferedReader reader = Files.newBufferedReader(Paths.get(x.getPaz()))) {
                            String removeIt = a.getPaz();
                            String currentLine;
                            while ((currentLine = reader.readLine()) != null) {
                                if (currentLine.trim().equals(removeIt)) {
                                    continue;
                                }
                                writer.write(currentLine + System.lineSeparator());
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    try {
                        Files.delete(Paths.get(x.getPaz()));
                    }
                    catch (IOException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    try {
                        Files.move(Paths.get(System.getProperty("user.dir"), "Resources", "Playlists", "temp.txt"), Paths.get(x.getPaz()));
                    }
                    catch (IOException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    x.removeSong(a);
                }
            }
        });
        MPlayer.getPlaylist().getFirst().removeSong(a);
        try {
            Files.delete(Paths.get(a.getPaz()));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        FXMLLoader b = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        b.load();
        HelloController c = b.getController();
        c.musicPlayer = MPlayer;
        Stage exit = (Stage) window.getScene().getWindow();
        exit.close();
    }
}
