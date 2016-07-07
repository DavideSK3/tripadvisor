/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.Restaurant;
import java.io.IOException;
import java.net.URLEncoder;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author gabriele
 */
public class AdvancedResearchServlet extends HttpServlet {

    private DBManager manager;

    public static final int LIMIT = 10;
    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    private List<Restaurant> filter(HttpServletRequest req, List<Restaurant> list){
        
        
        return list;
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
       
        
        String r_query = req.getParameter("restaurant");
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
        
        
        String m = req.getParameter("min_price");
        String M = req.getParameter("max_price");
        
        Integer minPrice = null;
        try{
            minPrice = Integer.parseInt(m);
        }catch (NumberFormatException e){
            minPrice = null;
            System.out.println("min null");
        }
        
        Integer maxPrice = null;
        try{
            maxPrice = Integer.parseInt(M);
        }catch (NumberFormatException e){
            maxPrice = null;
            System.out.println("max null");
        }
        
        String[] cuisines = req.getParameterValues("cusines");
        String[] valutazioni = req.getParameterValues("valutazione");
        
        ArrayList<Integer> val = new ArrayList<>();
        if(valutazioni != null){
            for(String v : valutazioni){
                try{
                    val.add(Integer.parseInt(v));
                }catch (NumberFormatException e) {}
            }
        }else{
            System.out.println("valutazioni null");
        }
        
        if(cuisines == null){
            System.out.println("cuisines null");
        }
        
        
        String distance = req.getParameter("distance");
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");
        
        String url = getServletContext().getContextPath()+"/RestaurantsList?restaurant=" + URLEncoder.encode(r_query, "UTF-8") + "&place=" + URLEncoder.encode(p_query, "UTF-8") + "&order=" + order;
        
        String query = r_query + ";" + p_query + ";" +(m!=null ? m  +";" : "") + (M!=null ? M +";": "");
        if(cuisines != null) for(String s :cuisines) query+=s + ";";
        if(valutazioni != null) for(String s :valutazioni) query+=s+ ";";
        
        if(distance != null) query += distance+ ";";
        if(longitude != null) query += longitude+ ";";
        if(latitude != null) query += latitude+ ";";
        
                
        HttpSession session = req.getSession(true);
        
        List<Restaurant> results = (List<Restaurant>)session.getAttribute(query);
        String p = req.getParameter("page");
        
        
        if(results == null || p == null){
            long inizio = new Date().getTime();
            try{
                if(!r_query.isEmpty() && !p_query.isEmpty()){
                    results = manager.getRestaurantsOrderedBy(r_query, p_query, order);
                }else if(!r_query.isEmpty() && p_query.isEmpty()){
                    results = manager.getRestaurantsByNameSimilarityFilteredOrderedBy(r_query, order, minPrice, maxPrice, cuisines, val);
                }else if(r_query.isEmpty() && !p_query.isEmpty()){
                    results = manager.getRestaurantsByPlaceOrderedBy(p_query, order);
                }else{
                    results = manager.getRestaurantsFilteredOrderedBy(order, minPrice, maxPrice, cuisines, val);
                }
            }catch (SQLException ex){
                results = new ArrayList<>();
                Logger.getLogger(PasswordRecoveryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            long fine = new Date().getTime();
            System.out.println("Result size = " +results.size());
            System.out.println("Risultati calcolati in " + (fine - inizio)/1000.0 + " secondi");
            
            session.setAttribute(query, results);
            
        }
        
        
        int page = 0;
        
        if(p != null){ page = Integer.parseInt(p); }
        
        if(page < 0) page = 0;
        
        if(page > Math.ceil(results.size()/(double)LIMIT)){
            page = (int)Math.ceil(results.size()/(double)LIMIT);
        }
        
        try {
            req.setAttribute("cuisines", manager.getCuisines());
        } catch (SQLException ex) {
            Logger.getLogger(RestaurantsListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setAttribute("restaurant", r_query);
        req.setAttribute("place", p_query);
        
        req.setAttribute("min_price", minPrice);
        req.setAttribute("max_price", maxPrice);
        
        if(cuisines != null){
            for(String s :cuisines){
                req.setAttribute(s, true);
            }
        }
        if(valutazioni != null){
            for(String s: valutazioni){
                req.setAttribute("v"+s, true);
            }
        }
        req.setAttribute("distance", distance);
        
        req.setAttribute("page", page);
        req.setAttribute("results", results.subList(page*LIMIT, Math.min((page+1)*LIMIT, results.size())));
        req.setAttribute("redirectURL", url);
        
        RequestDispatcher rd = req.getRequestDispatcher("result_list.jsp");
        rd.forward(req, resp);
    }
}
