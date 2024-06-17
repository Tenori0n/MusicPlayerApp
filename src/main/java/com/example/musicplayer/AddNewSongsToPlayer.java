package com.example.musicplayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class AddNewSongsToPlayer {
    public MP MPlayer;
    public String fileName;
    @FXML
    private Pane window;

    @FXML
    protected void startAdding()
    {
        window.getChildren().clear();
        Label file = new Label("Файл: " + fileName);
        file.setLayoutX(14);
        file.setLayoutY(14);
        Label addAuthor = new Label("Введите автора песни:");
        addAuthor.setLayoutX(37);
        addAuthor.setLayoutY(42);
        Label addName = new Label("Введите название песни: ");
        addName.setLayoutX(32);
        addName.setLayoutY(100);
        TextField author = new TextField();
        author.setLayoutX(26);
        author.setLayoutY(69);
        TextField name = new TextField();
        name.setLayoutX(26);
        name.setLayoutY(124);
        Button okButton = new Button("OK");
        okButton.setLayoutX(84);
        okButton.setLayoutY(161);
        okButton.setOnAction(event -> {
            try {
                adding(author.getText(), name.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        window.getChildren().add(file);
        window.getChildren().add(addAuthor);
        window.getChildren().add(addName);
        window.getChildren().add(author);
        window.getChildren().add(name);
        window.getChildren().add(okButton);
    }
    @FXML
    public void adding(String author, String name) throws IOException
    {
        MPSong newSong = new MPSong();
        if (author.isEmpty())
        {
            newSong.setAuthor("Неизвестен");
        }
        else
        {
            newSong.setAuthor(author);
        }
        if (name.isEmpty())
        {
            newSong.setSongName("Без названия");
        }
        else
        {
            newSong.setSongName(name);
        }
        newSong.setPaz(System.getProperty("user.dir") + "\\Resources" + "\\Songs\\" + newSong.getAuthor() + " -- " + newSong.getSongName() + ".mp3");
        try {
            Files.copy(Paths.get(System.getProperty("user.dir"), "Resources", "NewSongs", fileName), Paths.get(System.getProperty("user.dir"), "Resources", "Songs", newSong.getAuthor() + " -- " + newSong.getSongName() + ".mp3"));
        }
        catch (IOException e)
        {
            throw e;
        }
        newSong.setDuration((int)(new Media(new File(newSong.getPaz()).toURI().toString()).getDuration().toSeconds()));
        FXMLLoader a = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        a.load();
        HelloController b = a.getController();
        MPlayer.getPlaylist().getFirst().addSong(newSong);
        b.musicPlayer = MPlayer;
        Stage exit = (Stage) window.getScene().getWindow();
        exit.close();
    }
}