package holdServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "homePage", urlPatterns = {"/homePage"})
public class homepage extends HttpServlet {

    private Statement stmt = null;
    private PreparedStatement updateStmt = null;//inserts and updates;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/holdinfo?user=root&password=cmsc250";
            Connection connection = DriverManager.getConnection(url);
            stmt = connection.createStatement();
            updateStmt = connection.prepareStatement("INSERT into hold (item,patron) values (?,?)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usernameHome = null;
        response.setContentType("text/html;charset=UTF-8");
        int i = 0;

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>"
                    + "<head>"
                    + " <title> Home </title>"
                    + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
                    + "</head>"
                    + "<body>"
                    + "Welcome to the Home Page."
                    + "<p>"
                    + " Your  items are:" + "</p>");
            out.println("<p>" + "----------------------------" + "</p>");

            usernameHome = request.getParameter("username");

            if (usernameHome != null) {

                request.getSession().setAttribute("usernameHome", usernameHome);
                String selectingUser = "SELECT title FROM (holdinfo.hold join holdinfo.item on item=iditem) join holdinfo.patron on patron=idpatron where name=\'" + usernameHome + "\'";
                try {
                    ResultSet rset = stmt.executeQuery(selectingUser);

                    while (rset.next()) {//if you want to process all the results use while
                        i++;
                        out.println("<p>" + "Title:" + rset.getString(1) + "</p>");

                        out.println("<p>" + "---------------" + "</p>");
                    }
                } catch (Exception ex) {
                    System.out.println("Error!");
                    ex.printStackTrace();
                }
                out.println("<DIV style=\"position: absolute; top:600px; left:400px; width:200px; height:25px\">"
                        + " <form action=\"searchPage\" >"
                        + " <p> "
                        + " Search by title: <input type=\"text\" name=\"search\" >"
                        + " </p> "
                        + "<input TYPE=\"submit\" VALUE=\"Submit\">"
                        + "</form>"
                        + "</DIV>"
                        + "</body>"
                        + "</html>");

            }

            String callnumber = request.getParameter("callnumber");
        
            usernameHome = request.getSession().getAttribute("usernameHome").toString();

            String query = "SELECT iditem from holdinfo.item where callnumber=\"" + callnumber + "\"";
            String query2 = "SELECT idpatron from holdinfo.patron where name=\"" + usernameHome + "\"";
            int itemid = 0;
            int patronid = 0;
            try {
                ResultSet rset = stmt.executeQuery(query);

                if (rset.next()) {
                    itemid = rset.getInt(1);
                }
            } catch (Exception ex) {
                System.out.println("Error!");
                ex.printStackTrace();
            }

            try {
                ResultSet rset1 = stmt.executeQuery(query2);

                if (rset1.next()) {
                    patronid = rset1.getInt(1);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                updateStmt.setInt(1, itemid);
                updateStmt.setInt(2, patronid);
                updateStmt.execute();

            } catch (Exception ex) {
                System.out.println("Error!");
                ex.printStackTrace();
            }

           
        
            if (usernameHome != null) {
                String selectingUser1 = "SELECT title FROM (holdinfo.hold join holdinfo.item on item=iditem) join holdinfo.patron on patron=idpatron where name=\'" + usernameHome + "\'";
                try {
                    ResultSet rset = stmt.executeQuery(selectingUser1);
                    if (i < 2) {
                        while (rset.next()) {
                            out.println("<p>" + "Title:" + rset.getString(1) + "</p>");
                            out.println("<p>" + "---------------" + "</p>");
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error!");
                    ex.printStackTrace();
                }
                out.println("</body>"
                        + "</html>");
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
        // Add code to place the hold
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


