/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.Restaurant;
import db.User;
import db.User.USER_TYPE;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ProfileServlet extends HttpServlet {

     private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    /**
     * Si occupa di ottenere i ristoranti posseduti da un ristoratore e passarli come attributi di sessione alla pagina profile.jsp
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        HttpSession session = req.getSession(true);
        Integer id = ((User)session.getAttribute("user")).getId();
        USER_TYPE type = ((User)session.getAttribute("user")).getType();
        
        if(type==USER_TYPE.R){
            List<Restaurant> results = null;
            try {
                results = manager.getRestaurantsByOwnerID(id);
            } catch (SQLException ex) {
                Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            req.setAttribute("results",results);
        }
        
        
        RequestDispatcher rd = req.getRequestDispatcher("profile.jsp");
        
        rd.forward(req, resp);
    }
}