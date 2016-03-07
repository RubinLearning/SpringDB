package dao.impls;

import dao.interfaces.MP3Dao;
import dao.objects.Author;
import dao.objects.MP3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component("sqliteDAO")
public class SQLiteDAO implements MP3Dao{

    private static final String mp3Table = "mp3";
    private static final String mp3View = "mp3_view";

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    //@Transactional
    @Override
    public int insertMP3(MP3 mp3) {

        //System.out.println(TransactionSynchronizationManager.isActualTransactionActive());

        int author_id = insertAuthor(mp3.getAuthor());

        String sqlInsertMP3 = "insert into " + mp3Table + " (author_id2, name) VALUES (:authorId, :mp3Name)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mp3Name", mp3.getName());
        params.addValue("authorId", author_id);

        return jdbcTemplate.update(sqlInsertMP3, params);
    }

    //@Transactional
    @Override
    public int insertAuthor(Author author) {

        //System.out.println(TransactionSynchronizationManager.isActualTransactionActive());

        String sqlInsertAuthor = "insert into author (name) VALUES (:authorName)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("authorName", author.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sqlInsertAuthor, params, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public int insertList(List<MP3> mp3List) {
//        String sql = "insert into mp3 (name, author) values (:name, :author)";
//        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(mp3List.toArray());
//        int[] updateCounts = jdbcTemplate.batchUpdate(sql, batch);
//        return updateCounts.length;

        int i=0;

        for (MP3 mp3: mp3List) {
            insertMP3(mp3);
            i++;
        }

        return i;
    }

    public void delete(int id) {
        String sql = "DELETE FROM mp3 WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(sql, params);
    }

    public MP3 getMP3ByID(int id) {
        String sql = "select * from " + mp3View + " where mp3_id=:mp3_id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mp3_id", id);

        return jdbcTemplate.queryForObject(sql, params, new MP3RowMapper());
    }

    public List<MP3> getMP3ListByName(String name) {
        String sql = "select * from " + mp3View + " where upper(mp3_name) like :mp3_name";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mp3_name", "%" + name.toUpperCase() + "%");

        return jdbcTemplate.query(sql, params, new MP3RowMapper());
    }

    public List<MP3> getMP3ListByAuthor(String author) {
        String sql = "select * from " + mp3View + " where upper(author_name) like :mp3_author";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mp3_author", "%" + author.toUpperCase() + "%");

        return jdbcTemplate.query(sql, params, new MP3RowMapper());
    }

    public Map<String, Integer> getStat() {
        String sql = "select author_name, count(*) as count from " + mp3View + " group by author_name";

        return jdbcTemplate.query(sql, new ResultSetExtractor<Map<String, Integer>>() {

            public Map<String, Integer> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Map<String, Integer> map = new TreeMap<>();
                while (resultSet.next()){
                    String author = resultSet.getString("author_name");
                    int count = resultSet.getInt("count");
                    map.put(author, count);
                }
                return map;
            }

        });
    }

    public int getMP3count() {
        String sql = "select count(*) from " + mp3Table;
        return jdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
    }

    private static final class MP3RowMapper implements RowMapper<MP3> {
        public MP3 mapRow(ResultSet resultSet, int i) throws SQLException {

            Author author = new Author();
            author.setId(resultSet.getInt("author_id"));
            author.setName("author_name");

            MP3 mp3 = new MP3();
            mp3.setId(resultSet.getInt("mp3_id"));
            mp3.setName(resultSet.getString("mp3_name"));
            mp3.setAuthor(author);
            return mp3;
        }
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
