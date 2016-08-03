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
        
        if(key != null){
            Restaurant r = null;
            
            try{
                r = manager.getRestaurant(Integer.parseInt(key));
                req.setAttribute("restaurant", r);
                rd = req.getRequestDispatcher("/restaurant.jsp");
                
            }catch (SQLException ex){
                rd = req.getRequestDispatcher("/error_page.jsp");
                Logger.getLogger(PasswordRecoveryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }catch(NumberFormatException ex){
                req.setAttribute("error", key);
                rd = req.getRequestDispatcher("/error_page.jsp");
            }
            
            try{
                manager.getRestaurantPhotos(r);
            } catch (SQLException ex) {
                Logger.getLogger(RestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try{
                manager.getRestaurantTimes(r);
            } catch (SQLException ex) {
                Logger.getLogger(RestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try{
                manager.calcolaPosizioneInClassifica(r);
            } catch (SQLException ex) {
                Logger.getLogger(RestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try{
                manager.getReviews(r);
            }catch (SQLException ex) {
                Logger.getLogger(RestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }else{
            
            rd = req.getRequestDispatcher("/restaurant_list.jsp");
        }
        rd.forward(req, resp);
        
    }
}
