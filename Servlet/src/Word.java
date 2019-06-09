
import com.alibaba.fastjson.JSON;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class Word {
    private String word = null;
    private String[] pronunciations;
    private String levels;
    private ArrayList<Translation> translations;
    private ArrayList<Sentence> sentences;
    private DBConnector dbc;

    public Word(String word) {
        if (word == null) {
            return;
        }
        try {
            dbc = DBConnector.getConnection();
            if (searchWord(word)) {
                this.word = word;
                this.levels = searchLevels(word);
                this.translations = searchTranslations(word);
                //获取pronunciations
                this.pronunciations = searchPronunciations(word);
                //获取sentences
                this.sentences = searchSentences(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean searchWord(String word) throws SQLException {
        ResultSet rs = dbc.executeQuery(String.format(
                "SELECT word_id " +
                "FROM word " +
                "where word='%s';", word));
        return rs.next();
    }

    public String searchLevels(String word) throws SQLException {
        ResultSet rs = dbc.executeQuery(String.format(
                "SELECT * " +
                "FROM word " +
                "where word='%s';", word));
        rs.next();
        return rs.getString("levels");
    }

    public ArrayList<Translation> searchTranslations(String word) throws SQLException {
        ResultSet rs = dbc.executeQuery(String.format(
                "SELECT DISTINCT property,translation " +
                        "FROM word " +
                        "natural join translate " +
                        "natural join translation " +
                        "where word='%s';", word));
        ArrayList<Translation> translations = new ArrayList<>();
        while (rs.next()) {
            String property = rs.getString("property");
            String translation = rs.getString("translation");
            //寻找相同词性
            for (Translation trans : translations) {
                System.out.println(trans.toString());
                if (property.equals(trans.getProperty())) { //该词性存在
                    trans.getTranslations().add(translation);
                    property = null;
                }
            }
            if (property != null) { //判断该词性不存在
                Translation trans = new Translation(property, translation);
                translations.add(trans);
            }
        }
        return translations;
    }

    public String[] searchPronunciations(String word) throws SQLException {
        String[] pronunciations = new String[2];
        ResultSet rs = dbc.executeQuery(String.format(
                "select p1.pronunciation, p2.pronunciation from " +
                "(select word_id,pronunciation from pronunciation,pronunce where en_pron_id = pron_id) p1, " +
                "(select word_id,pronunciation from pronunciation,pronunce where us_pron_id = pron_id) p2 " +
                "natural join word " +
                "where p1.word_id = p2.word_id and word = '%s' ", word));
        rs.next();
        pronunciations[0] = rs.getString(1);
        pronunciations[1] = rs.getString(2);
        return pronunciations;
    }

    public ArrayList<Sentence> searchSentences(String word) throws SQLException{
        ArrayList<Sentence> sentences = new ArrayList<Sentence>();
        ResultSet rs = dbc.executeQuery(String.format(
                "select sentence,translation " +
                "from sentence " +
                "natural join instance " +
                "natural join word " +
                "where word = '%s';", word));
        while (rs.next()) {
            sentences.add(new Sentence(rs.getString("sentence"), rs.getString("translation")));
        }
        return sentences;
    }




    public String getWord() {
        return word;
    }

    public String[] getPronunciations() {
        return pronunciations;
    }

    public String getLevels() {
        return levels;
    }

    public ArrayList<Translation> getTranslations() {
        return translations;
    }

    public String getTranslationsString() {
        String translations = "";
        for (Translation trans : this.translations) {
            translations += trans.toString() + '\n';
        }
        return translations.substring(0, translations.length() - 2);
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    public String getSentencesString() {
        String sent = "";
        for (Sentence sentence : this.sentences) {
            sent += sentence.toString() + '\n';
        }
        return sent.substring(0, sent.length() - 2);
    }

    public String toString() {
        return String.format("Word:%s\nPronunciation:%s\nTranslations:%s\nSentences:%s\n", word, getPronunciations()[0] + getPronunciations()[1], getTranslationsString(), getSentencesString());
    }

    public String toJson() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("word", word);
        map.put("levels", levels);
        map.put("pronunciations", pronunciations);
        map.put("translations", translations);
        map.put("sentences", sentences);
        return JSON.toJSONString(map);
    }
}

class Translation {
   private String property;
   private ArrayList<String> translations;

   public Translation(String property,String translation){
       this.property = property;
       this.translations = new ArrayList<>();
       this.translations.add(translation);
   }
   @Override
   public String toString() {
       String translation = property + " ";
       for (String s:translations) {
           translation += s + ";";
       }
       return translation;
   }

    public String getProperty() {
        return property;
    }

    public ArrayList<String> getTranslations() {
        return translations;
    }
}
 class Sentence {
    private String sentence;
    private String translation;
    public Sentence(String sentence,String translation){
        this.sentence = sentence;
        this.translation = translation;
    }
    @Override
    public String toString() {
        return sentence + '\n' + translation;
    }

     public String getSentence() {
         return sentence;
     }

     public String getTranslation() {
         return translation;
     }
 }
