/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.Restaurant;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
        
        
        
        List<Restaurant> results = new ArrayList<>(4);
        results.add(new Restaurant("Sushi", "via del sushi"));
        results.add(new Restaurant("Pizza", "via della pizza"));
        results.add(new Restaurant("Gelato", "via del gelato"));
        results.add(new Restaurant("Pasta", "via dellla pasta"));
        
        
        /*try{
            if(r_query != null && p_query != null){
                results = manager.getRestaurant(r_query, p_query);
            }else if(r_query != null && p_query == null){
                results = manager.getRestaurantByName(r_query);
            }else if(r_query == null && p_query != null){
                results = manager.getRestaurantByPlace(p_query);
            }else{
                //TODO - scegliere cosa fare se non è specificata alcuna richiesta 
                results = new ArrayList<>();
            }
        }catch (SQLException ex){
            
        }*/
        if(r_query == null) r_query = "";
        if(p_query == null) p_query = "";
        
        String url = getServletContext().getContextPath()+"/RestaurantsList?restaurant=" + r_query + "&place=" + p_query;
        
        
        
        
        req.setAttribute("restaurantsList", results);
        req.setAttribute("redirectURL", url);
        RequestDispatcher rd = req.getRequestDispatcher("result_list.jsp");
        rd.forward(req, resp);
    }
}
