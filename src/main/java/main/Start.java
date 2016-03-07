package main;

import dao.impls.SQLiteDAO;
import dao.interfaces.MP3Dao;
import dao.objects.Author;
import dao.objects.MP3;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Start {

    public static void main(String[] args) {

        Author author = new Author();
        author.setName("Author 9");

        MP3 mp3 = new MP3();
        mp3.setName("Song name 9");
        mp3.setAuthor(author);

//        List<MP3> list = new ArrayList<>();
//        list.add(mp3);
//        mp3 = new MP3();
//        mp3.setName("Song name 7");
//        mp3.setAuthor("Song author 7");
//        list.add(mp3);
        //new SQLiteDAO().insertWithJDBC(mp3);

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        MP3Dao sqLiteDAO = (MP3Dao) context.getBean("sqliteDAO");
//        System.out.println(sqLiteDAO.insertList(list));

        System.out.println(sqLiteDAO.insertMP3(mp3));

        //sqLiteDAO.delete(5);
        //List<MP3> mp3List = sqLiteDAO.getMP3ListByName("Sunrise");
        //for (MP3 song: mp3List) {
        //    System.out.println(song.getName());
        //}

//        Map<String, Integer> map = sqLiteDAO.getStat();
//        System.out.println(map);
    }

}
