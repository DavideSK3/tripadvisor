/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.Restaurant;
import db.Util;
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

    public static final int LIMIT = 2;
    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        manageRequest(req, resp);
    }
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        manageRequest(req, resp);
    }
    
    protected void manageRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String r_query = req.getParameter("r_query");
        String p_query = req.getParameter("place");
        
        if(r_query == null) r_query = "";
        else r_query = r_query.trim();
        
        if(p_query == null) p_query = "";
        else{  p_query = p_query.trim();  }
        
        
        String url = "RestaurantsList?r_query=" + URLEncoder.encode(r_query, "UTF-8") + "&place=" + URLEncoder.encode(p_query, "UTF-8");
        
        req.setAttribute("r_query", r_query);
        req.setAttribute("place", p_query);
        
        String query_id = req.getParameter("query_id");
        req.setAttribute("query_id", query_id);
                
        HttpSession session = req.getSession(false);
        
        
        String button = req.getParameter("button");
        
        if(button == null || button.equals("search") || session == null || query_id == null){
            if(req.getMethod().equalsIgnoreCase("GET")){
                newSimpleSearch(req, resp);
            }else{
                newAdvancedResearch(req, resp);
            }
        }else{
            manageResults(req, resp);
        }
        
        Integer page = (Integer) req.getAttribute("page");
        if(page == null){
            page = 0;    
            req.setAttribute("page", page);
        }
        
        session = req.getSession();
        query_id = (String) req.getAttribute("query_id");
        
        List<Restaurant> results = (List<Restaurant>) session.getAttribute(query_id);
        if(results == null){
            results = new ArrayList<>();
        }
        
        
        
        req.setAttribute("results", results.subList(page*LIMIT, Math.min((page+1)*LIMIT, results.size())));
        req.setAttribute("redirectURL", url);

        RequestDispatcher rd = req.getRequestDispatcher("result_list.jsp");
        rd.forward(req, resp);
    }
    
    
    
    protected void newAdvancedResearch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String r_query = (String) req.getAttribute("r_query");
        String p_query = (String) req.getAttribute("place");
        
        if(!p_query.isEmpty()){
            try {
                p_query = manager.getPlaceBySimilarity(p_query).toString();
            } catch (SQLException ex) {
                Logger.getLogger(RestaurantsListServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        String m = req.getParameter("min_price");
        String M = req.getParameter("max_price");
        
        Integer minPrice = null;
        try{
            minPrice = Integer.parseInt(m);
        }catch (NumberFormatException e){
            minPrice = null;
        }
        
        Integer maxPrice = null;
        try{
            maxPrice = Integer.parseInt(M);
        }catch (NumberFormatException e){
            maxPrice = null;
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
        }
        
        
        String distance = req.getParameter("distance");
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");
        
        
        
                        
        HttpSession session = req.getSession(true);
        
        String query_id = UUID.randomUUID().toString();
        while(session.getAttribute(query_id) != null){
            query_id = UUID.randomUUID().toString();
        }
        req.setAttribute("query_id", query_id);
        
        
        List<Restaurant> results;
        
        long inizio = new Date().getTime();
        try{
            results = manager.getRestaurantsFilteredOrderedBy(r_query, p_query, "position", minPrice, maxPrice, cuisines, val);
        }catch (SQLException ex){
            results = new ArrayList<>();
            Logger.getLogger(PasswordRecoveryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        long fine = new Date().getTime();
        System.out.println("Result size = " +results.size());
        System.out.println("Risultati calcolati in " + (fine - inizio)/1000.0 + " secondi");

        if(distance != null && longitude != null && latitude != null){
            results = filterByDistance(results, distance, longitude, latitude);
        }


        session.setAttribute(query_id, results);
        req.setAttribute("page", 0);
        
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
        req.setAttribute("page", 0);
    }
    
    
    
    protected void newSimpleSearch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String r_query = (String) req.getAttribute("r_query");
        String p_query = (String) req.getAttribute("place");
        
        if(!p_query.isEmpty()){
            try {
                p_query = manager.getPlaceBySimilarity(p_query).toString();
            } catch (SQLException ex) {
                Logger.getLogger(RestaurantsListServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
                
        HttpSession session = req.getSession(true);
        
        String query_id = UUID.randomUUID().toString();
        while(session.getAttribute(query_id) != null){
            query_id = UUID.randomUUID().toString();
        }
        req.setAttribute("query_id", query_id);
        
        
        List<Restaurant> results;
        
        long inizio = new Date().getTime();
        try{
            if(!r_query.isEmpty() && !p_query.isEmpty()){
                results = manager.getRestaurantsOrderedBy(r_query, p_query, "position");

            }else if(!r_query.isEmpty() && p_query.isEmpty()){

                results = manager.getRestaurantsByNameSimilarityOrderedBy(r_query, "position");

            }else if(r_query.isEmpty() && !p_query.isEmpty()){
                results = manager.getRestaurantsByPlaceOrderedBy(p_query, "position");

            }else{
                //TODO - scegliere cosa fare se non è specificata alcuna richiesta 
                results = new ArrayList<>();
            }
        }catch (SQLException ex){
            results = new ArrayList<>();
            Logger.getLogger(PasswordRecoveryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Restaurant r : results){
            try{
                manager.getRestaurantFirstPhoto(r);
            } catch (SQLException ex) {
                Logger.getLogger(RestaurantsListServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        long fine = new Date().getTime();
        System.out.println("Result size = " +results.size());
        System.out.println("Risultati calcolati in " + (fine - inizio)/1000.0 + " secondi");

        
        
        
        session.setAttribute(query_id, results);
        
        req.setAttribute("page", 0);
        
    }
    
    
    
    protected void manageResults(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
       
        
        
        String query_id = req.getParameter("query_id");
                
        HttpSession session = req.getSession(false);
        
        List<Restaurant> results = (List<Restaurant>)session.getAttribute(query_id);
        
        String p = req.getParameter("page");

        int page = 0;
        if(p != null){ 
            try{
                page = Integer.parseInt(p);
            }catch (NumberFormatException e){
                page = 0;
            }
        }


        String order = "";

        String button = req.getParameter("button");
        switch (button) {
            case "Next":
                page++;
                break;
            case "Previous":
                page--;
                break;
            case "Price":
                order ="price";
                page = 0;
                break;
            case "Name":
                order = "name";
                page = 0;
                break;
            case "Position":
                order = "position";
                page = 0;
                break;
            default:
                page = 0;
                order = "";
                break;
        }

        

        if(page >= Math.floor(results.size()/(double)LIMIT)){
            page = (int)Math.floor(results.size()/(double)LIMIT)-1;
        }
        if(page < 0) page = 0;
        
        switch(order){
            case "position":
                Util.sortByValue(results);
                break;
            case "name":
                Util.sortByName(results);
                break;
            case "price":
                Util.sortByPrice(results);
                break;
            default:
                break;
        }


        req.setAttribute("page", page);
    }
    
    
    public static final List<Restaurant> filterByDistance(List<Restaurant> restaurants, String distance, String longitude, String latitude){
        System.out.println("Filtrazione per distanza");
        try{
            ArrayList<Restaurant> results = new ArrayList<>();
            
            System.out.println("d= " + distance + ";\n lo = " + longitude + "; lat = " + latitude);
            
            Double d = Double.parseDouble(distance);
            Double lo = Double.parseDouble(longitude);
            Double la = Double.parseDouble(latitude);

            for(Restaurant r :restaurants){
                if(r.getLongitude() != null && r.getLatitude() != null && Util.computeLinearDistance(lo, la, r.getLongitude(), r.getLatitude())*1000 <= d){
                    results.add(r);
                }
            }
            return results;
        }catch (NumberFormatException e){
            System.out.println("Filtrazione per distanza: number format exception");
            return restaurants;
        }
        
    }
}
