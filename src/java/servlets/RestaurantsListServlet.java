/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.Place;
import db.Restaurant;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
public class RestaurantsListServlet extends HttpServlet {

    private DBManager manager;

    public static final int LIMIT = 10;
    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
       
        String r_query = req.getParameter("r_query");
        String p_query = req.getParameter("place");
        
        String order = req.getParameter("order");
        if(order == null){ order = "position"; }
        
        if(r_query == null) r_query = "";
        else r_query = r_query.trim();
        
        if(p_query == null) p_query = "";
        else{
            p_query = p_query.trim();
            if(!p_query.isEmpty()){
                try {
                    p_query = manager.getPlaceBySimilarity(p_query).toString();
                } catch (SQLException ex) {
                    Logger.getLogger(RestaurantsListServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        String url = "RestaurantsList?r_query=" + URLEncoder.encode(r_query, "UTF-8") + "&place=" + URLEncoder.encode(p_query, "UTF-8") + "&order=" + order;
        
        
        
        
                
        HttpSession session = req.getSession(true);
        
        List<Restaurant> results;
        
        long inizio = new Date().getTime();
        try{
            if(!r_query.isEmpty() && !p_query.isEmpty()){
                results = manager.getRestaurantsOrderedBy(r_query, p_query, order);

            }else if(!r_query.isEmpty() && p_query.isEmpty()){

                results = manager.getRestaurantsByNameSimilarityOrderedBy(r_query, order);

            }else if(r_query.isEmpty() && !p_query.isEmpty()){
                results = manager.getRestaurantsByPlaceOrderedBy(p_query, order);

            }else{
                //TODO - scegliere cosa fare se non è specificata alcuna richiesta 
                results = new ArrayList<>();
            }
        }catch (SQLException ex){
            results = new ArrayList<>();
            Logger.getLogger(PasswordRecoveryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        long fine = new Date().getTime();
        System.out.println("Result size = " +results.size());
        System.out.println("Risultati calcolati in " + (fine - inizio)/1000.0 + " secondi");

        
        String query_id = UUID.randomUUID().toString();
        while(session.getAttribute(query_id) != null){
            query_id = UUID.randomUUID().toString();
        }
        
        session.setAttribute(query_id, results);
        
        
        try {
            req.setAttribute("cuisines", manager.getCuisines());
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantsListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setAttribute("r_query", r_query);
        req.setAttribute("place", p_query);
        req.setAttribute("page", 0);
        req.setAttribute("query_id", query_id);
        req.setAttribute("results", results.subList(0, Math.min(LIMIT, results.size())));
        req.setAttribute("redirectURL", url);
        
        RequestDispatcher rd = req.getRequestDispatcher("result_list.jsp");
        rd.forward(req, resp);
    }
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
       
        String r_query = req.getParameter("r_query");
        String p_query = req.getParameter("place");
        
        String order = req.getParameter("order");
        if(order == null){ order = "position"; }
        
        if(r_query == null) r_query = "";
        else r_query = r_query.trim();
        
        if(p_query == null) p_query = "";
        else{  p_query = p_query.trim();  }
        
        String url = "RestaurantsList?r_query=" + URLEncoder.encode(r_query, "UTF-8") + "&place=" + URLEncoder.encode(p_query, "UTF-8") + "&order=" + order;
        
        
        
        
        String query_id = req.getParameter("query_id");
                
        HttpSession session = req.getSession(true);
        
        List<Restaurant> results = (List<Restaurant>)session.getAttribute(query_id);
        
        
        String p = req.getParameter("page");
        
        int page = 0;
        if(p != null){ page = Integer.parseInt(p); }
        
        
        
        String button = req.getParameter("changePageButton");
        switch (button) {
            case "Next":
                page++;
                break;
            case "Previous":
                page--;
                break;
            default:
                page = 0;
                break;
        }
        
        if(page < 0) page = 0;
        
        if(page > Math.floor(results.size()/(double)LIMIT)){
            page = (int)Math.floor(results.size()/(double)LIMIT);
        }
        
        try {
            req.setAttribute("cuisines", manager.getCuisines());
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantsListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setAttribute("r_query", r_query);
        req.setAttribute("place", p_query);
        req.setAttribute("page", page);
        req.setAttribute("query_id", query_id);
        req.setAttribute("results", results.subList(page*LIMIT, Math.min((page+1)*LIMIT, results.size())));
        req.setAttribute("redirectURL", url);
        
        RequestDispatcher rd = req.getRequestDispatcher("result_list.jsp");
        rd.forward(req, resp);
    }
}
