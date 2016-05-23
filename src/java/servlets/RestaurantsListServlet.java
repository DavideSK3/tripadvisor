/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.Restaurant;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gabriele
 */
public class RestaurantsListServlet extends HttpServlet {

     private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
       
        
        String r_query = req.getParameter("restaurant");
        String p_query = req.getParameter("place");
        
        
        
        List<Restaurant> results;
        
        long inizio = new Date().getTime();
        try{
            if(!r_query.equals("") && !p_query.equals("")){
                results = manager.getRestaurants(r_query, p_query);
            }else if(!r_query.equals("") && p_query.equals("")){
                results = manager.getRestaurantsByNameSimilarity2(r_query);
            }else if(r_query.equals("") && !p_query.equals("")){
                results = manager.getRestaurantsByPlace(p_query);
            }else{
                //TODO - scegliere cosa fare se non è specificata alcuna richiesta 
                results = new ArrayList<>();
            }
        }catch (SQLException ex){
            results = new ArrayList<>();
            Logger.getLogger(PasswordRecoveryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        long fine = new Date().getTime();
        
        if(r_query == null) r_query = "";
        if(p_query == null) p_query = "";
        
        String url = getServletContext().getContextPath()+"/RestaurantsList?restaurant=" + r_query + "&place=" + p_query;
        
        
        System.out.println("Result size = " +results.size());
        System.out.println("Risultati calcolati in " + (fine - inizio)/1000.0 + " secondi");
        
        
        req.setAttribute("restaurantsList", results);
        req.setAttribute("redirectURL", url);
        RequestDispatcher rd = req.getRequestDispatcher("result_list.jsp");
        rd.forward(req, resp);
    }
}
