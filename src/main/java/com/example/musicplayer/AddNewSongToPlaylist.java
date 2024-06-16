package com.example.musicplayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AddNewSongToPlaylist {
    public MP MPlayer;
    public MPPlaylist destination;
    @FXML
    private VBox SongsToAdd;
    @FXML
    private AnchorPane window;
    @FXML
    public void onLoad() {
        SongsToAdd.getChildren().clear();
        MPlayer.getPlaylist().getFirst().getSongList().forEach(x->
                {
                    Button newBut = new Button(x.getAuthor() + " - " + x.getSongName());
                    newBut.setMinHeight(25);
                    newBut.setMinWidth(322);
                    newBut.setOnAction(event -> {try {addToPlaylist(x);} catch (IOException e) {
                        try {
                            throw e;
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }});
                    newBut.setTooltip(new Tooltip(x.getAuthor() + " - " + x.getSongName()));
                    SongsToAdd.getChildren().add(newBut);
                }
        );
    }
    public void addToPlaylist(MPSong a) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (destination.getSongList().contains(a)) {
            alert.setContentText("Такая песня уже есть в плейлисте!");
            alert.show();
        }
        else {
            destination.addSong(a);
            try {
                Files.writeString(Paths.get(destination.getPaz()), a.getPaz() + "\n", StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw e;
            }
        }
        FXMLLoader b = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        b.load();
        HelloController c = b.getController();
        c.musicPlayer = MPlayer;
        Stage exit = (Stage) window.getScene().getWindow();
        exit.close();
    }
}
