/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.Review;
import db.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
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
        
        Integer global_value = Integer.parseInt(req.getParameter("global"));
        Integer food = Integer.parseInt(req.getParameter("food"));
        Integer service = Integer.parseInt(req.getParameter("service"));
        Integer value_for_money = Integer.parseInt(req.getParameter("money"));
        Integer atmosphere = Integer.parseInt(req.getParameter("atmosphere"));
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        Integer id_restaurant = Integer.parseInt(req.getParameter("id_restaurant"));
        Integer id_creator = ((User)(session.getAttribute("user"))).getId();
        
        String return_address = req.getParameter("return_address");
        
        RequestDispatcher rd = req.getRequestDispatcher("/PhotoUpload");
        rd.include(req, resp);
        
        Integer id_photo = (Integer)req.getAttribute("photo_id");
        
        String message;
        try {
            manager.insertReview(global_value, food, service, value_for_money, atmosphere, title, description, id_restaurant, id_creator, id_photo);
        } catch (SQLException ex) {
            message = "Errore nella sottomissione della recensione";
            throw new ServletException(ex);

        }
        
        rd = req.getRequestDispatcher(return_address);
        rd.forward(req, resp);
        

    }
}
