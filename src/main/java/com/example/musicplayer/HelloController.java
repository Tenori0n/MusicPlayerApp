package com.example.musicplayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class HelloController {
    public MP musicPlayer = new MP();
    @FXML
    private VBox Playlists;
    @FXML
    private VBox PlaylistsSongs;
    @FXML
    private Button playbutton;
    @FXML
    private CheckBox repeat;
    @FXML
    private Button nextTrack;
    @FXML
    private Button prevTrack;

    public MediaPlayer Sound;

    public void initialize()
    {
        if (musicPlayer.getCurrentPlaylist() == null || musicPlayer.getCurrentSong() == null)
        {
            playbutton.setDisable(true);
            nextTrack.setDisable(true);
            prevTrack.setDisable(true);
            if (Sound != null)
                Sound.stop();
        }
        else
        {
            playbutton.setDisable(false);
            nextTrack.setDisable(false);
            prevTrack.setDisable(false);
        }
        Playlists.getChildren().clear();
        PlaylistsSongs.getChildren().clear();
        Button newBut = new Button("Новый плейлист");
        newBut.setMinHeight(25);
        newBut.setMinWidth(145);
        newBut.setOnAction(event -> {createNewPlaylist();});
        Playlists.getChildren().add(newBut);
        newBut = new Button("Удалить плейлист");
        newBut.setMinHeight(25);
        newBut.setMinWidth(145);
        newBut.setOnAction(event -> {deletePlaylist();});
        Playlists.getChildren().add(newBut);
        onLoad();
    }
    @FXML
    protected void addNewSongsToPlayer() throws IOException
    {
        try (Stream<Path> walk = Files.walk(Paths.get(System.getProperty("user.dir"), "Resources", "NewSongs"))) {
            List<Path> result = walk.filter(Files::isRegularFile).toList();
            result.forEach(x -> {
                String fileName = new File(x.toString()).getName();
                int dotIndex = fileName.lastIndexOf('.');
                String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
                if (extension.equals("mp3")) {
                    FXMLLoader temp = new FXMLLoader(getClass().getResource("addNewSongsToPlayer.fxml"));
                    Scene newScene;
                    try {
                        newScene = new Scene(temp.load(), 200, 200);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Stage newStage = new Stage();
                    newStage.setTitle("Добавить песни в плеер");
                    newStage.setScene(newScene);
                    newStage.initModality(Modality.APPLICATION_MODAL);
                    AddNewSongsToPlayer a = temp.getController();
                    a.MPlayer = musicPlayer;
                    a.fileName = fileName;
                    newStage.showAndWait();
                    initialize();
                }
            });
        }
        catch (IOException e) {
            throw e;
        }
    }
    @FXML
    protected void removeSongFromPlayer()
    {
        if (Sound != null)
        {
            Sound.stop();
            Sound.dispose();
            Sound = null;
            System.gc();
            musicPlayer.setCurrentSong(null);
            musicPlayer.setCurrentPlaylist(null);
        }
        FXMLLoader temp = new FXMLLoader(getClass().getResource("removeSongFromPlayer.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(temp.load(), 359, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage newStage = new Stage();
        newStage.setTitle("Удалить песни из плеера");
        newStage.setScene(newScene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        RemoveSongFromPlayer a = temp.getController();
        a.MPlayer = musicPlayer;
        newStage.showAndWait();
        initialize();
    }
    @FXML
    protected void addSongToPlaylist(MPPlaylist play)
    {
        FXMLLoader temp = new FXMLLoader(getClass().getResource("addNewSongToPlaylist.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(temp.load(), 359, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage newStage = new Stage();
        newStage.setTitle("Добавить песню в плейлист");
        newStage.setScene(newScene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        AddNewSongToPlaylist a = temp.getController();
        a.MPlayer = musicPlayer;
        a.destination = play;
        newStage.showAndWait();
        initialize();
        onPlaylistClick(play);
    }
    @FXML
    protected void removeSongFromPlaylist(MPPlaylist play)
    {
        FXMLLoader temp = new FXMLLoader(getClass().getResource("removeSongFromPlaylist.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(temp.load(), 359, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage newStage = new Stage();
        newStage.setTitle("Удалить песню из плейлиста");
        newStage.setScene(newScene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        RemoveSongFromPlaylist a = temp.getController();
        a.MPlayer = musicPlayer;
        a.destination = play;
        newStage.showAndWait();
        initialize();
        onPlaylistClick(play);
    }
    @FXML
    protected void createNewPlaylist()
    {
        FXMLLoader temp = new FXMLLoader(getClass().getResource("createPlaylist.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(temp.load(), 231, 116);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage newStage = new Stage();
        newStage.setTitle("Создать новый плейлист");
        newStage.setScene(newScene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        CreatePlaylist a = temp.getController();
        a.MPlayer = musicPlayer;
        newStage.showAndWait();
        initialize();
    }
    @FXML
    protected void deletePlaylist()
    {
        FXMLLoader temp = new FXMLLoader(getClass().getResource("deletePlaylist.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(temp.load(), 360, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage newStage = new Stage();
        newStage.setTitle("Удалить плейлист");
        newStage.setScene(newScene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        DeletePlaylist a = temp.getController();
        a.MPlayer = musicPlayer;
        newStage.showAndWait();
        initialize();
    }
    protected void playSong(MPSong a)
    {
        File temp = new File(a.getPaz());
        Media media = new Media(temp.toURI().toString());
        if (Sound != null)
            if (Sound.getStatus().equals(MediaPlayer.Status.PLAYING))
                Sound.stop();
        Sound = new MediaPlayer(media);
        playbutton.setOnAction(event -> {pauseSong(a);});
        playbutton.setDisable(false);
        nextTrack.setDisable(false);
        prevTrack.setDisable(false);
        ImageView pause = new ImageView(getClass().getResource("/img/pausebutton.png").toString());
        pause.setFitHeight(33);
        pause.setFitWidth(27);
        pause.setPickOnBounds(true);
        pause.setPreserveRatio(true);
        playbutton.setGraphic(pause);
        Sound.play();
        Sound.setOnEndOfMedia(() -> {
            if (repeat.isSelected())
                playSong(a);
            else
                nextSong();
        });
    }
    protected void pauseSong(MPSong a)
    {
        ImageView play = new ImageView(getClass().getResource("/img/playbutton.png").toString());
        play.setFitHeight(33);
        play.setFitWidth(27);
        play.setPickOnBounds(true);
        play.setPreserveRatio(true);
        playbutton.setGraphic(play);
        Sound.pause();
        playbutton.setOnAction((event -> {unpauseSong(a);}));
    }
    protected void unpauseSong(MPSong a)
    {
        ImageView pause = new ImageView(getClass().getResource("/img/pausebutton.png").toString());
        pause.setFitHeight(33);
        pause.setFitWidth(27);
        pause.setPickOnBounds(true);
        pause.setPreserveRatio(true);
        playbutton.setGraphic(pause);
        Sound.play();
        playbutton.setOnAction((event -> {pauseSong(a);}));
    }
    @FXML
    protected void nextSong()
    {
        MPSong temp = musicPlayer.getCurrentPlaylist().getSongList().getFirst();
        int i = 0;
        while (i < musicPlayer.getCurrentPlaylist().getSongList().size()) {
            temp = musicPlayer.getCurrentPlaylist().getSongList().get(i);
            if (temp.equals(musicPlayer.getCurrentSong()))
            {
                if (!musicPlayer.getCurrentSong().equals(musicPlayer.getCurrentPlaylist().getSongList().getLast()))
                    temp = musicPlayer.getCurrentPlaylist().getSongList().get(i+1);
                break;
            }
            i++;
        }
        if (!musicPlayer.getCurrentSong().equals(musicPlayer.getCurrentPlaylist().getSongList().getLast())) {
            musicPlayer.setCurrentSong(temp);
            playSong(musicPlayer.getCurrentSong());
        }
        else
        {
            Sound.stop();
            musicPlayer.setCurrentSong(null);
            playbutton.setDisable(true);
            nextTrack.setDisable(true);
            prevTrack.setDisable(true);
        }
    }
    @FXML
    protected void prevSong()
    {
        MPSong temp = musicPlayer.getCurrentPlaylist().getSongList().getFirst();
        int i = 0;
        while (i < musicPlayer.getCurrentPlaylist().getSongList().size()) {
            temp = musicPlayer.getCurrentPlaylist().getSongList().get(i);
            if (temp.equals(musicPlayer.getCurrentSong()))
            {
                if (!musicPlayer.getCurrentSong().equals(musicPlayer.getCurrentPlaylist().getSongList().getFirst()))
                    temp = musicPlayer.getCurrentPlaylist().getSongList().get(i-1);
                break;
            }
            i++;
        }
        musicPlayer.setCurrentSong(temp);
        playSong(temp);
    }
    @FXML
    protected void onPlaylistClick(MPPlaylist a) {
        PlaylistsSongs.getChildren().clear();
        Button temp = new Button("Добавить песню");
        temp.setMinWidth(145);
        temp.setMinHeight(25);
        temp.setOnAction(event -> {addSongToPlaylist(a);});
        PlaylistsSongs.getChildren().add(temp);
        temp = new Button("Удалить песню");
        temp.setMinWidth(145);
        temp.setMinHeight(25);
        temp.setOnAction(event -> {removeSongFromPlaylist(a);});
        PlaylistsSongs.getChildren().add(temp);
        a.getSongList().forEach(y ->
        {
            Button newBut = new Button(y.getAuthor() + " - " + y.getSongName());
            newBut.setMinHeight(25);
            newBut.setMinWidth(145);
            newBut.setOnAction(event -> {
                musicPlayer.setCurrentSong(y);
                musicPlayer.setCurrentPlaylist(a);
                playSong(musicPlayer.getCurrentSong());});
            newBut.setTooltip((new Tooltip(y.getAuthor() + " - " + y.getSongName())));
            PlaylistsSongs.getChildren().add(newBut);
        });
    }
    @FXML
    private void onLoad()
    {
        musicPlayer.getPlaylist().forEach(x -> {
            if (x.getPlaylistName().equals("Песни плеера"));
            else {
                Button newBut = new Button(x.getPlaylistName());
                newBut.setMinHeight(25);
                newBut.setMinWidth(145);
                newBut.setOnAction(event -> {onPlaylistClick(x);});
                newBut.setTooltip(new Tooltip(x.getPlaylistName()));
                Playlists.getChildren().add(newBut);
            }
    });
    }
}