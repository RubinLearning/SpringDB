package dao.interfaces;

import dao.objects.Author;
import dao.objects.MP3;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MP3Dao {

    int insertMP3(MP3 mp3);

    int insertAuthor(Author author);

    int insertList(List<MP3> mp3List);

    void delete(int id);

    MP3 getMP3ByID(int id);

    List<MP3> getMP3ListByName(String name);

    List<MP3> getMP3ListByAuthor(String author);

    public int getMP3count();

    public Map<String, Integer> getStat();

}
