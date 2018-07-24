package holdServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "searchPage", urlPatterns = {"/searchPage"})
public class searchpage extends HttpServlet {

    private Statement stmt = null;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/holdinfo?user=root&password=cmsc250";
            Connection connection = DriverManager.getConnection(url);
            stmt = connection.prepareStatement("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title> Search </title>"
                    + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
                    + "</head>"
                    + "<body>"
                    + "<p> Search Results:"
                    + "</p>");
            out.println("<p>" + "----------------------------" + "</p>");

            String searchinput = request.getParameter("search");
            if (searchinput != null) {
                request.getSession().setAttribute("searchinput", searchinput);
                
                String holdersearch = "SELECT title, callnumber FROM (holdinfo.hold join holdinfo.item on item=iditem) join holdinfo.patron on patron=idpatron where title=\'" + searchinput + "\'";
                try {
                    ResultSet rset;
                    rset = stmt.executeQuery(holdersearch);
                    while (rset.next()) {
                        out.println("<p>" + rset.getString(1) + "</p>");
                        out.println("<p>" + rset.getString(2) + "</p>");

                    }

                } catch (Exception ex) {
                    System.out.println("Error!");
                    ex.printStackTrace();
                }
                out.println("<DIV style=\"position: absolute; top:600px; left:400px; width:200px; height:25px\">");
                out.println("<form action=\"homePage\">"
                        + "<input type=\"text\" name=\"callnumber\" >");
                
                out.println("<input TYPE=\"submit\" VALUE=\"Place Hold\">");

                out.println("</body>"
                        + "</html>"
                        + "");
            }

        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
