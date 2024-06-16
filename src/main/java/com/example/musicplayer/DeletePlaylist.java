package com.example.musicplayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeletePlaylist {
    MP MPlayer;
    @FXML
    private VBox PlaylistsToDelete;
    @FXML
    public AnchorPane window;

    public void removePlaylist(MPPlaylist a) throws IOException
    {
        String temp = a.getPlaylistName() + ".txt";
        try {
            Files.delete(Paths.get(System.getProperty("user.dir"), "Resources", "Playlists", temp));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        MPlayer.getPlaylist().remove(a);
        FXMLLoader b = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        b.load();
        HelloController c = b.getController();
        c.musicPlayer = MPlayer;
        Stage exit = (Stage) window.getScene().getWindow();
        exit.close();
    }
    @FXML
    public void onLoad() {
        PlaylistsToDelete.getChildren().clear();
        MPlayer.getPlaylist().forEach(x->
            {
                if (x != MPlayer.getPlaylist().getFirst()) {
                    Button newBut = new Button(x.getPlaylistName());
                    newBut.setMinHeight(25);
                    newBut.setMinWidth(322);
                    newBut.setOnAction(event -> {
                        try {
                            removePlaylist(x);
                        } catch (IOException e) {
                            try {
                                throw e;
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                    newBut.setTooltip(new Tooltip(x.getPlaylistName()));
                    PlaylistsToDelete.getChildren().add(newBut);
                }
            }
        );
    }
}
