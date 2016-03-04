package main;

import dao.impls.SQLiteDAO;
import dao.objects.MP3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {

    public static void main(String[] args) {

        MP3 mp3 = new MP3();
        mp3.setName("Song name 1");
        mp3.setAuthor("Song author 1");
        //new SQLiteDAO().insertWithJDBC(mp3);

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        SQLiteDAO sqLiteDAO = (SQLiteDAO) context.getBean("sqliteDAO");
        //sqLiteDAO.insert(mp3);

        sqLiteDAO.delete(5);
    }

}
