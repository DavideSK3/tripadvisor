/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.Restaurant;
import db.User;
import java.io.IOException;
import java.io.PrintWriter;
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
public class RestaurantServlet extends HttpServlet {

    private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd;
        
        String key = req.getParameter("restaurantID");
        /*
        if(key != null){
            Restaurant r = null;
            
            try{
                r = manager.getRestaurant((Integer)req.getAttribute("r_key"));
            }catch (SQLException ex){
                
            }
            
            req.setAttribute("restaurant", r);
            rd = req.getRequestDispatcher("/restaurant.jsp");
            //TODO - se la query sul database non trova il ristorante??????
            
            
        }else{
            
            rd = req.getRequestDispatcher("/restaurant_list.jsp");
        }
        */
        //if(key == null) key ="";
        
        String url = getServletContext().getContextPath()+"/Restaurant?restaurantID=" + key;
        req.setAttribute("redirectURL", url);
        req.setAttribute("restaurantID", key);
        rd = req.getRequestDispatcher("/restaurant.jsp");
        
        rd.forward(req, resp);
    }
}
