package dao.interfaces;

import dao.objects.MP3;

import java.util.Collection;
import java.util.List;

public interface MP3Dao {

    void insert(MP3 mp3);

    void insert(Collection<MP3> mp3Collection);

    void delete(int id);

    MP3 getMP3ByID(int id);

    List<MP3> getMP3ListByName(String name);

    List<MP3> getMP3ListByAuthor(String author);

}
