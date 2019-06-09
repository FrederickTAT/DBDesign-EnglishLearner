import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Notes{
    private ArrayList<Note> notes = new ArrayList<>();
    private DBConnector dbc;
    public Notes(){
        freshNotes();
    }
    public void freshNotes(){
        try {
            dbc = DBConnector.getConnection();
            notes.clear();
            ResultSet rs = dbc.executeQuery("SELECT word_id,word,date FROM note natural join word;");
            while (rs.next()) {
                notes.add(new Note(rs.getInt("word_id"),rs.getString("word"),rs.getTimestamp("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addNote(String word){
        dbc.execute(String.format(
                "insert into note(word_id) values (" +
                "(select word_id from word where word = '%s'));",word));
    }
    public void deleteNote(String word){
        dbc.execute(String.format(
                "delete from note " +
                "where word_id = " +
                "(select word_id " +
                "from word " +
                "where word = '%s');",word
        ));
    }
    public String toJson(){
        System.out.println(JSON.toJSONString(notes));
        return JSON.toJSONString(notes);
    }

}

class Note{
    private int word_id;
    private Timestamp date;
    private String word;
    public Note(int word_id,String word,Timestamp date){
        this.word_id = word_id;
        this.date = date;
        this.word = word;
    }

    @Override
    public String toString() {
        return word_id + " " + word + " " + date.toString();
    }

    public String getWord() {
        return word;
    }

    public int getWord_id() {
        return word_id;
    }

    public String getDate() {
        return date.toString().substring(0,19);
    }
}