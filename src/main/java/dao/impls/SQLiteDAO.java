package dao.impls;

import dao.interfaces.MP3Dao;
import dao.objects.MP3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component("sqliteDAO")
public class SQLiteDAO implements MP3Dao{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(MP3 mp3) {
        String sql = "insert into mp3 (name, author) VALUES (?, ?)";
        jdbcTemplate.update(sql, new Object[] {mp3.getName(), mp3.getAuthor()});
    }

    public void insert(Collection<MP3> mp3Collection) {
        for (MP3 mp3: mp3Collection) {
            insert(mp3);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM mp3 WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public MP3 getMP3ByID(int id) {
        return null;
    }

    public List<MP3> getMP3ListByName(String name) {
        return null;
    }

    public List<MP3> getMP3ListByAuthor(String author) {
        return null;
    }

    public void insertWithJDBC(MP3 mp3) {
        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:db/testdb.db";
            conn = DriverManager.getConnection(url, "", "");
        } catch (ClassNotFoundException e1){
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "insert into mp3 (name, author) VALUES (?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, mp3.getName());
            ps.setString(2, mp3.getName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e){

                }
            }
        }

    }

}
