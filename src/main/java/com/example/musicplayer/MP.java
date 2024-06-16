package com.example.musicplayer;

import javafx.scene.media.Media;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class MP {
    private ArrayList<MPPlaylist> playlists;
    private MPPlaylist currentPlaylist;
    private MPSong currentSong;

    public ArrayList<MPPlaylist> getPlaylist(){return playlists;}
    public MPPlaylist getCurrentPlaylist(){return currentPlaylist;}
    public MPSong getCurrentSong(){return currentSong;}
    public void setCurrentSong(MPSong a){currentSong = a;}
    public void setCurrentPlaylist(MPPlaylist a){currentPlaylist = a;}

    public MP() {
        playlists = new ArrayList<>();
        playlists.addFirst(new MPPlaylist());
        playlists.getFirst().setPlaylistName("Песни плеера");
        currentPlaylist = null;
        currentSong = null;
        try (Stream<Path> walk = Files.walk(Paths.get(System.getProperty("user.dir"), "Resources", "Songs"))) {
            List<Path> result = walk.filter(Files::isRegularFile).toList();
            result.forEach(x -> {
                String fileName = new File(x.toString()).getName();
                int dotIndex = fileName.lastIndexOf('.');
                String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
                if (extension.equals("mp3"))
                {
                    MPSong newSong = new MPSong();
                    if (fileName.contains(" -- "))
                    {
                        String[] a;
                        a = fileName.substring(0, fileName.length()-4).split(" -- ");
                        newSong.setAuthor(a[0]);
                        newSong.setSongName(a[1]);
                    }
                    newSong.setPaz(System.getProperty("user.dir") + "\\Resources" + "\\Songs\\" + fileName);
                    newSong.setDuration((int)(new Media(new File(newSong.getPaz()).toURI().toString()).getDuration().toSeconds()));
                    playlists.getFirst().addSong(newSong);
                }
            });
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        try (Stream<Path> walk = Files.walk(Paths.get(System.getProperty("user.dir"), "Resources", "Playlists"))) {
            List<Path> result = walk.filter(Files::isRegularFile).toList();
            result.forEach(x -> {
                String fileName = new File(x.toString()).getName();
                int dotIndex = fileName.lastIndexOf('.');
                String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
                if (extension.equals("txt"))
                {
                    MPPlaylist newPlaylist = new MPPlaylist();
                    newPlaylist.setPlaylistName(fileName.substring(0, fileName.length()-4));
                    newPlaylist.setPaz(System.getProperty("user.dir") + "\\Resources" + "\\Playlists\\" + fileName);
                    try (Stream<String> strim = Files.lines(Paths.get(newPlaylist.getPaz()))){
                        strim.forEach(y -> {
                            MPSong newSong = new MPSong();
                            playlists.getFirst().getSongList().forEach(z-> {
                                if (z.getPaz().equals(y)) {
                                    MPSong sameSong = z;
                                    newSong.setPaz(y);
                                    newSong.setSongName(sameSong.getSongName());
                                    newSong.setAuthor(sameSong.getAuthor());
                                    newSong.setDuration(sameSong.getDuration());
                                    newPlaylist.addSong(newSong);
                                }
                            });
                        });
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    playlists.add(newPlaylist);
                }
            });

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void Menu() {
        String userFolder = System.getProperty("user.dir");
        Path addSongPath = Paths.get(userFolder, "Resources", "NewSongs");
        Path playlistPath = Paths.get(userFolder, "Resources", "Playlists");
        Path songPath = Paths.get(userFolder, "Resources", "Songs");
        try {
            Files.createDirectories(addSongPath);
            Files.createDirectories(playlistPath);
            Files.createDirectories(songPath);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        Scanner input = new Scanner(System.in);
        boolean flag = true;
        int temp;
        while (flag) {
            System.out.print("------------------------------\n" +
                    "Меню\n" +
                    "0. Выйти из плеера\n" +
                    "1. Добавить песню в плеер\n" +
                    "2. Удалить песню из плеера\n" +
                    "3. Показать список всех песен\n" +
                    "4. Создать плейлист\n" +
                    "5. Удалить плейлист\n" +
                    "6. Показать список плейлистов\n" +
                    "7. Загрузить плейлист\n" +
                    "8. Запустить плейлист\n" +
                    "9. Добавить песню в плейлист\n" +
                    "10. Удалить песню из плейлиста\n" +
                    "11. Показать песни плейлиста\n" +
                    "12. Включить предыдущий трек\n" +
                    "13. Включить следующий трек\n" +
                    "14. Повторить текущий трек\n" +
                    "------------------------------\n");
            if (currentPlaylist != null)
                System.out.println("Текущий плейлист: " + currentPlaylist.getPlaylistName() + "Сейчас играет: " + currentSong.getAuthor() + " - " + currentSong.getSongName());
            temp = input.nextInt();
            switch (temp) {
                case 0:
                    flag = false;
                    break;
                case 1:
                    addSong();
                    break;
                case 2:
                    removeSong();
                    break;
                case 3:
                    showAllSongs();
                    break;
                case 4:
                    createPlaylist();
                    break;
                case 5:
                    removePlaylist();
                    break;
                case 6:
                    showAllPlaylists();
                    break;
                case 7:
                    System.out.println(temp);
                    break;
                case 8:
                    startPlayList();
                    break;
                case 9:
                    addSongToPlaylist();
                    break;
                case 10:
                    removeSongFromPlaylist();
                    break;
                case 11:
                    showPlaylistSongs();
                    break;
                case 12:
                    previousTrack();
                    break;
                case 13:
                    nextTrack();
                    break;
                case 14:
                    repeatTrack();
                    break;
                default:
                    System.out.println("Такого пункта нет в меню!");
                    break;
            }
            System.out.println("Для продолжения нажмите Enter...");
            input.nextLine();
            input.nextLine();
        }
    }

    public void addSong() {
        Scanner input = new Scanner(System.in);
        System.out.println("Для добавления песен положите их в папку MusicPlayer/Resources/NewSongs. Не забудьте удалить уже добавленные песни");
        System.out.println("После копирования файлов песен в папку нажмите Enter...");
        input.nextLine();
        try (Stream<Path> walk = Files.walk(Paths.get(System.getProperty("user.dir"), "Resources", "NewSongs"))) {
            List<Path> result = walk.filter(Files::isRegularFile).toList();
            result.forEach(x -> {
                String fileName = new File(x.toString()).getName();
                int dotIndex = fileName.lastIndexOf('.');
                String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
                if (extension.equals("mp3"))
                {
                    MPSong newSong = new MPSong();
                    String temp;
                    String fullName ="";
                    System.out.println("Текущий файл - " + fileName);
                    System.out.println("Введите автора песни: ");
                    temp = input.nextLine();
                    if (!temp.isBlank()) {
                        newSong.setAuthor(temp);
                    }
                    else
                    {
                        newSong.setAuthor("Неизвестен");
                    }
                    fullName+=newSong.getAuthor() + " -- ";
                    System.out.println("Введите название песни: ");
                    temp = input.nextLine();
                    if (!temp.isBlank()) {
                        newSong.setSongName(temp);
                    }
                    else
                    {
                        newSong.setSongName("Без названия");
                    }
                    fullName+= newSong.getSongName() + ".mp3";
                    System.out.println("Введите длительность песни: ");
                    while (true) {
                        int dur = input.nextInt();
                        input.nextLine();
                        if (dur == 0)
                            System.out.println("Песня не может быть длиной в 0 секунд");
                        else {
                            newSong.setDuration(dur);
                            break;
                        }
                    }
                    try {
                        Files.copy(Paths.get(System.getProperty("user.dir"), "Resources", "NewSongs", fileName), Paths.get(System.getProperty("user.dir"), "Resources", "Songs", fullName));
                    }
                    catch (IOException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    newSong.setPaz(System.getProperty("user.dir") + "\\Resources" + "\\Songs\\" + fullName);
                    playlists.getFirst().addSong(newSong);
                    System.out.println("Песня " + newSong.getAuthor() + " - " + newSong.getSongName() + " была успешно добавлена в плеер");
                }
            });
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void showAllSongs()
    {
        System.out.println("Список песен плеера:");
        playlists.getFirst().printPlayList();
    }

    public void removeSong()
    {
        int i;
        Scanner input = new Scanner(System.in);
        while (true) {
            showAllSongs();
            if (playlists.getFirst().getSongList().isEmpty())
                return;
            System.out.println("Введите номер песни, которую хотите удалить из плеера: ");
            i = input.nextInt();
            if (i <= 0 || i > playlists.getFirst().getSongNumber())
                System.out.println("Такой песни нет в плеере");
            else {
                MPSong temp = playlists.getFirst().getSongList().get(i - 1);
                if (currentSong == temp) {
                    currentPlaylist = null;
                    currentSong = null;
                }
                playlists.forEach(x -> {
                    if (x != playlists.getFirst())
                    {
                        if (x.getSongList().contains(temp)) {
                            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(System.getProperty("user.dir"), "Resources", "Playlists", "temp.txt"))) {
                                try (BufferedReader reader = Files.newBufferedReader(Paths.get(x.getPaz()))) {
                                    String removeIt = temp.getPaz();
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
                            x.removeSong(temp);
                        }
                    }
                });
                playlists.getFirst().removeSong(temp);
                try {
                    Files.delete(Paths.get(temp.getPaz()));
                }
                catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }
                break;
            }
        }
    }

    public void createPlaylist()
    {
        String temp;
        MPPlaylist newPlaylist = new MPPlaylist();
        Scanner input = new Scanner(System.in);
        System.out.println("Введите название плейлиста: ");
        temp = input.nextLine();
        if (!temp.isBlank())
            newPlaylist.setPlaylistName(temp);
        playlists.add(newPlaylist);
        newPlaylist.setPaz(System.getProperty("user.dir") + "\\Resources\\Playlists\\" + newPlaylist.getPlaylistName() + ".txt");
        try {
            Files.createFile(Paths.get(newPlaylist.getPaz()));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void showAllPlaylists()
    {
        System.out.println("Список плейлистов плеера:");
        for (int i = 1; i < playlists.size(); i++) {
            System.out.println(i + ". " + playlists.get(i).getPlaylistName());
        }
        if (playlists.size()==1)
            System.out.println("В плеере нет плейлистов");
    }

    public void removePlaylist()
    {
        int i;
        String temp;
        Scanner input = new Scanner(System.in);
        while (true) {
            showAllPlaylists();
            if (playlists.size() == 1)
                return;
            System.out.println("Введите номер плейлиста, которую хотите удалить из плеера: ");
            i = input.nextInt();
            if (i <= 0 || i > playlists.size())
                System.out.println("Такого плейлиста нет в плеере");
            else {
                if (currentPlaylist == playlists.get(i))
                {
                    currentSong = null;
                    currentPlaylist = null;
                }
                temp = playlists.get(i).getPlaylistName() + ".txt";
                try {
                    Files.delete(Paths.get(System.getProperty("user.dir"), "Resources", "Playlists", temp));
                }
                catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }
                playlists.remove(playlists.get(i));
                break;
            }
        }
    }

    public void startPlayList()
    {
        int i;
        Scanner input = new Scanner(System.in);
        while (true) {
            showAllPlaylists();
            if (playlists.size() == 1)
                return;
            System.out.println("Введите номер плейлиста, который хотите запустить: ");
            i = input.nextInt();
            if (i <= 0 || i > playlists.size())
                System.out.println("Такого плейлиста нет в плеере");
            else {
                currentPlaylist = playlists.get(i);
                currentSong = currentPlaylist.getSongList().getFirst();
                break;
            }
        }
    }

    public void addSongToPlaylist()
    {
        int i ,j;
        Scanner input = new Scanner(System.in);
        while (true) {
            showAllPlaylists();
            if (playlists.size() == 1)
                return;
            System.out.println("Введите номер плейлиста, в который вы хотите добавить песню: ");
            i = input.nextInt();
            if (i <= 0 || i > playlists.size())
                System.out.println("Такого плейлиста нет в плеере");
            else {
                while (true) {
                    showAllSongs();
                    System.out.println("Введите номер песни, которую вы хотите добавить в плейлист " + playlists.get(i).getPlaylistName() + " : ");
                    j = input.nextInt();
                    if (j <= 0 || i > playlists.size())
                        System.out.println("Такой песни нет в плеере");
                    else {
                        MPPlaylist tempPlaylist = playlists.get(i);
                        MPSong tempSong = playlists.getFirst().getSongList().get(j-1);
                        tempPlaylist.addSong(tempSong);
                        try {
                            Files.writeString(Paths.get(tempPlaylist.getPaz()), tempSong.getPaz()+"\n", StandardOpenOption.APPEND);
                        }
                        catch (IOException e)
                        {
                            System.out.println(e.getMessage());
                        }
                        System.out.println("Песня " + tempSong.getSongName() + " была добавлена в плейлист " + tempPlaylist.getPlaylistName());
                        break;
                    }
                }
                break;
            }
        }
    }

    public void removeSongFromPlaylist()
    {
        int i ,j;
        Scanner input = new Scanner(System.in);
        while (true) {
            showAllPlaylists();
            if (playlists.size() == 1)
                return;
            System.out.println("Введите номер плейлиста, в котором вы хотите удалить песню: ");
            i = input.nextInt();
            if (i <= 0 || i > playlists.size())
                System.out.println("Такого плейлиста нет в плеере");
            else {
                while (true) {
                    playlists.get(i).printPlayList();
                    System.out.println("Введите номер песни, которую вы хотите удалить из плейлиста " + playlists.get(i).getPlaylistName() + " : ");
                    j = input.nextInt();
                    if (j <= 0 || i > playlists.size())
                        System.out.println("Такой песни нет в плейлисте");
                    else {
                        MPPlaylist tempPlaylist = playlists.get(i);
                        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(System.getProperty("user.dir"), "Resources", "Playlists", "temp.txt"))) {
                            try (BufferedReader reader = Files.newBufferedReader(Paths.get(tempPlaylist.getPaz())))
                            {
                                String removeIt = tempPlaylist.getSongList().get(j-1).getPaz();
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
                                System.out.println(e.getMessage());
                            }
                        }
                        catch (IOException e)
                        {
                            System.out.println(e.getMessage());
                        }
                        try {
                            Files.delete(Paths.get(tempPlaylist.getPaz()));
                        }
                        catch (IOException e)
                        {
                            System.out.println(e.getMessage());
                        }
                        try {
                            Files.move(Paths.get(System.getProperty("user.dir"), "Resources", "Playlists", "temp.txt"), Paths.get(tempPlaylist.getPaz()));
                        }
                        catch (IOException e)
                        {
                            System.out.println(e.getMessage());
                        }
                        System.out.println("Песня " + tempPlaylist.getSongList().get(j-1).getSongName() + " была удалена из плейлиста " + playlists.get(i).getPlaylistName());
                        if (currentSong == tempPlaylist.getSongList().get(j-1) && currentPlaylist == tempPlaylist)
                        {
                            currentPlaylist = null;
                            currentSong = null;
                        }
                        tempPlaylist.removeSong(tempPlaylist.getSongList().get(j-1));
                        break;
                    }
                }
                break;
            }
        }
    }

    public void showPlaylistSongs()
    {
        int i;
        Scanner input = new Scanner(System.in);
        while (true) {
            showAllPlaylists();
            if (playlists.size() == 1)
                return;
            System.out.println("Введите номер плейлиста, песни которого вы хотите просмотреть: ");
            i = input.nextInt();
            if (i <= 0 || i > playlists.size())
                System.out.println("Такого плейлиста нет в плеере");
            else {
                playlists.get(i).printPlayList();
                break;
            }
        }
    }

    public void nextTrack()
    {
        int nextSongIndex = currentPlaylist.getSongList().indexOf(currentSong)+1;
        if (nextSongIndex > currentPlaylist.getSongList().size()-1)
        {
            nextSongIndex=0;
        }
        currentSong = currentPlaylist.getSongList().get(nextSongIndex);
    }

    public void previousTrack()
    {
        int nextSongIndex = currentPlaylist.getSongList().indexOf(currentSong)-1;
        if (nextSongIndex < 0)
        {
            nextSongIndex=currentPlaylist.getSongList().size()-1;
        }
        currentSong = currentPlaylist.getSongList().get(nextSongIndex);
    }

    public void repeatTrack()
    {
        int nextSongIndex = currentPlaylist.getSongList().indexOf(currentSong);
        currentSong = currentPlaylist.getSongList().get(nextSongIndex);
    }

}
