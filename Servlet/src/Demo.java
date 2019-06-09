import com.alibaba.fastjson.JSON;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Demo implements Servlet  {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        DBConnector.getConnection();
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (httpServletRequest.getMethod().equals("GET")) {
            doGet(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException{
        //request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String function = request.getParameter("function");
        if(function.equals("search")) {
            Word word = new Word(request.getParameter("word"));
            if (word.getWord() == null) {
                writer.write("{}");
            } else {
                System.out.println(word.toJson());
                writer.write(word.toJson());
            }
        }
        if(function.equals("note")){
            Notes notes = new Notes();
            String item = request.getParameter("item");

            if(item.equals("get")){
                writer.write(notes.toJson());
            }else {
                String word = request.getParameter("word");
                if (item.equals("add")) {
                    notes.addNote(word);
                }
                if (item.equals("delete")) {
                    notes.deleteNote(word);
                }
            }
        }
        if(function.equals("random")) {
            DBConnector dbc = DBConnector.getConnection();

            String levels = request.getParameter("levels");
            ArrayList randomWords = new ArrayList();

            String sql = String.format(
                    "select word,translation " +
                            "from translation " +
                            "natural join translate " +
                            "natural join word " +
                            "%s" +
                            "order by rand() " +
                            "limit 4;",
                    levels.equals("0")?"":String.format("where levels = '%s' ","level_" + levels));
            try {
                System.out.println("正在执行语句:" + sql);
                ResultSet rs = dbc.executeQuery(sql);
                while(rs.next()){
                    HashMap map = new HashMap();
                    map.put("word",rs.getString("word"));
                    map.put("translation", rs.getString("translation"));
                    randomWords.add(map);
                }
                rs.close();
                writer.write(JSON.toJSONString(randomWords));
            }catch (SQLException e){
                e.printStackTrace();
            }

        }
    }
}
