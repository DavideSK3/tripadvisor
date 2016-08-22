/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.oreilly.servlet.MultipartRequest;
import db.DBManager;

import db.User;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gabriele
 */
public class ReviewServlet extends HttpServlet {

    
     private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application

        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        RequestDispatcher rd = req.getRequestDispatcher("/PhotoUpload");
        rd.include(req, resp);
        
        Integer id_photo = (Integer)req.getAttribute("photo_id");
        
        MultipartRequest multi = (MultipartRequest)req.getAttribute("multi");
        
        Integer global_value = multi.getParameter("global")!=null ? Integer.parseInt(multi.getParameter("global")) : null;
        Integer food = multi.getParameter("food")!= null ? Integer.parseInt(multi.getParameter("food")) : null;
        Integer service = multi.getParameter("service")!= null ? Integer.parseInt(multi.getParameter("service")) : null;
        Integer value_for_money = multi.getParameter("money")!= null ? Integer.parseInt(multi.getParameter("money")) : null;
        Integer atmosphere = multi.getParameter("atmosphere")!= null ? Integer.parseInt(multi.getParameter("atmosphere")) : null;
        String title = multi.getParameter("title");
        String description = multi.getParameter("description");
        Integer id_restaurant = Integer.parseInt(multi.getParameter("id_restaurant"));
        Integer id_creator = ((User)(session.getAttribute("user"))).getId();
        
        String return_address = multi.getParameter("return_address");
        
        
        try {
            boolean risp = manager.insertReview(global_value, food, service, value_for_money, atmosphere, title, description, id_restaurant, id_creator, id_photo);
            
            if(!risp){
                String message = "Hai già inserito una recensione per questo ristorante";
                rd = req.getRequestDispatcher("message.jsp");
                req.setAttribute("message", message);
                rd.forward(req, resp);
            }else{
                resp.sendRedirect(resp.encodeRedirectURL(return_address));
            }
            
            
        } catch (SQLException ex) {
            String message = "Errore nella sottomissione della recensione";
            rd = req.getRequestDispatcher("message.jsp");
            req.setAttribute("message", message);
            rd.forward(req, resp);
            throw new ServletException(ex);
        }
        
        
        

    }
}
