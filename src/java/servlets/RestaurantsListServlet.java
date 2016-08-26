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
        
        
        String query_id = req.getParameter("query_id");
        req.setAttribute("query_id", query_id);
                
        HttpSession session = req.getSession(false);
        
        String button = req.getParameter("button");
        
        if(session == null || query_id == null || session.getAttribute(query_id) == null){
            newSearch(req, resp);
        }else{
            switch(button){
                case "Next":
                case "Previous":
                case "Price":
                case "Name":
                case "Position":
                    manageResults(req, resp);
                    break;
                case "Search":
                default:
                    newSearch(req, resp);
                    break;
            }
            
        }
        
        Integer page = (Integer) req.getAttribute("page");
        if(page == null){
            page = 0;    
            req.setAttribute("page", page);
        }
        
        session = req.getSession();
        query_id = (String) req.getAttribute("query_id");
        
        Research research = (Research) session.getAttribute(query_id);
        if(research == null){
            research = new Research();
            research.setQueryID(query_id);
            research.setpQuery((String) req.getAttribute("p_query"));
            research.setrQuery((String) req.getAttribute("r_query"));
            research.setResults(new ArrayList<>());
        }
        
        synchronized(research){
            if(research.getpQuery() != null){
                req.setAttribute("place", research.getpQuery());
            }
            if(research.getrQuery() != null){
                req.setAttribute("r_query", research.getrQuery());
            }
            if(research.getResults() != null){
                req.setAttribute("results", research.getResults().subList(page*LIMIT, Math.min((page+1)*LIMIT, research.getResults().size())));
                req.setAttribute("resultsDim", research.getResults().size());
            }

            if(research.getMinPrice()!= null) req.setAttribute("min_price", research.getMinPrice());
            if(research.getMaxPrice() != null) req.setAttribute("max_price", research.getMaxPrice());

            if(research.getCuisines() != null){
                for(String s :research.getCuisines()){
                    req.setAttribute(s, true);
                }
            }
            if(research.getValutazioni() != null){
                for(String s: research.getValutazioni()){
                    req.setAttribute("v"+s, true);
                }
            }
            if(research.getDistance() != null) req.setAttribute("distance", research.getDistance());
        }
        
        
        
        RequestDispatcher rd = req.getRequestDispatcher("results_list.jsp");
        rd.forward(req, resp);
    }
    
    
    protected void newSearch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String r_query = req.getParameter("r_query");
        String p_query = req.getParameter("place");

        if(r_query == null) r_query = "";
        else r_query = r_query.trim();

        if(p_query == null) p_query = "";
        else{  p_query = p_query.trim();  }


        req.setAttribute("r_query", r_query);
        req.setAttribute("place", p_query);
        if(!p_query.isEmpty()){
            try {
                p_query = manager.getPlaceBySimilarity(p_query).toString();
                req.setAttribute("place", p_query);
            } catch (SQLException ex) {
                Logger.getLogger(RestaurantsListServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        HttpSession session = req.getSession();

        String query_id = UUID.randomUUID().toString();
        while(session.getAttribute(query_id) != null){
            query_id = UUID.randomUUID().toString();
        }
        req.setAttribute("query_id", query_id);


        if(req.getMethod().equalsIgnoreCase("GET")){
            newSimpleSearch(req, resp);
        }else{
            newAdvancedResearch(req, resp);
        }
    }
    
    protected void newAdvancedResearch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String r_query = (String) req.getAttribute("r_query");
        String p_query = (String) req.getAttribute("place");
        
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
        
        String[] cuisines = req.getParameterValues("cuisines");
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
        
        Double d = null;
        Double lo = null, la = null;
        
        boolean d_error = false;
        try{
            d = Double.parseDouble(distance);
            lo = Double.parseDouble(longitude);
            la = Double.parseDouble(latitude);
        }catch (NumberFormatException e){
            d_error = true;
        }
        
        List<Restaurant> results;
        
        long inizio = new Date().getTime();
        try{
            results = manager.getRestaurantsFilteredOrderedBy(r_query, p_query, "position", minPrice, maxPrice, cuisines, val);
        }catch (SQLException ex){
            results = new ArrayList<>();
            Logger.getLogger(PasswordRecoveryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!d_error && distance != null && longitude != null && latitude != null){
            results = filterByDistance(results, d, lo, la);
        }
        int p=1;
        if(results.size()>0) results.get(0).setPosizione(p);
        for(int i=1; i<results.size(); i++){
            Restaurant r = results.get(i);
            if(r.getGlobal_review() < results.get(i-1).getGlobal_review()){
                p = i+1;
            }
            r.setPosizione(p);
        }
        
        long fine = new Date().getTime();
        System.out.println("Result size = " +results.size());
        System.out.println("Risultati calcolati in " + (fine - inizio)/1000.0 + " secondi");

        String query_id = (String) req.getAttribute("query_id");
        
        Research research = new Research();
        research.setQueryID(query_id);
        research.setpQuery(p_query);
        research.setrQuery(r_query);
        research.setCuisines(cuisines);
        research.setValutazioni(valutazioni);
        research.setDistance(d);
        research.setMaxPrice(maxPrice);
        research.setMinPrice(minPrice);
        research.setResults(results);

        
        HttpSession session = req.getSession(true);
        session.setAttribute(query_id, research);
        
        req.setAttribute("page", 0);
        
        
    }
    
    
    
    protected void newSimpleSearch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String r_query = (String) req.getAttribute("r_query");
        String p_query = (String) req.getAttribute("place");
        
        
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
        
        int p=1;
        if(results.size()>0) results.get(0).setPosizione(p);
        for(int i=1; i<results.size(); i++){
            Restaurant r = results.get(i);
            if(r.getGlobal_review() < results.get(i-1).getGlobal_review()){
                p = i+1;
            }
            r.setPosizione(p);
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

        String query_id = (String) req.getAttribute("query_id");
        
        Research research = new Research();
        research.setQueryID(query_id);
        research.setpQuery(p_query);
        research.setrQuery(r_query);
        research.setResults(results);

        HttpSession session = req.getSession(true);
        session.setAttribute(query_id, research);
        
        req.setAttribute("page", 0);
        
    }
    
    
    
    protected void manageResults(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String query_id = req.getParameter("query_id");
        HttpSession session = req.getSession();
        
        String p = req.getParameter("page");
        int page = 0;
        if(p != null){ 
            try{
                page = Integer.parseInt(p);
            }catch (NumberFormatException e){
                page = 0;
            }
        }
        
        Research research;
        List<Restaurant> results;
        
        research = (Research)session.getAttribute(query_id);
        
        synchronized(research){
            try{
                results = research.getResults();
            }catch (NullPointerException e){
                research = new Research();
                results = new ArrayList<>();

                research.setResults(results);
                research.setQueryID(query_id);

                session.setAttribute(query_id, research);
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


            if(page >= Math.ceil(results.size()/(double)LIMIT)){
                page = (int)Math.ceil(results.size()/(double)LIMIT)-1;
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
        }

        req.setAttribute("page", page);
    }
    
    
    public static final List<Restaurant> filterByDistance(List<Restaurant> restaurants, double d, double lo, double la){
        
        ArrayList<Restaurant> results = new ArrayList<>();

        for(Restaurant r :restaurants){
            if(r.getLongitude() != null && r.getLatitude() != null && Util.computeLinearDistance(lo, la, r.getLongitude(), r.getLatitude()) <= d){
                results.add(r);
            }
        }
        return results;
        
    }
    
    
    
    private class Research {
        
        private List<Restaurant> results = null;
        
        private String queryID = null;
        
        private String pQuery = null;
        private String rQuery = null;
        
        
        private Integer minPrice = null;
        private Integer maxPrice = null;
        
        private String[] cuisines = null;
        private String[] valutazioni = null;
        
        private Double distance = null;

        /**
         * @return the results
         */
        public List<Restaurant> getResults() {
            return results;
        }

        /**
         * @param results the results to set
         */
        public void setResults(List<Restaurant> results) {
            this.results = results;
        }

        /**
         * @return the queryID
         */
        public String getQueryID() {
            return queryID;
        }

        /**
         * @param queryID the queryID to set
         */
        public void setQueryID(String queryID) {
            this.queryID = queryID;
        }

        /**
         * @return the pQuery
         */
        public String getpQuery() {
            return pQuery;
        }

        /**
         * @param pQuery the pQuery to set
         */
        public void setpQuery(String pQuery) {
            this.pQuery = pQuery;
        }

        /**
         * @return the rQuery
         */
        public String getrQuery() {
            return rQuery;
        }

        /**
         * @param rQuery the rQuery to set
         */
        public void setrQuery(String rQuery) {
            this.rQuery = rQuery;
        }

        /**
         * @return the minPrice
         */
        public Integer getMinPrice() {
            return minPrice;
        }

        /**
         * @param minPrice the minPrice to set
         */
        public void setMinPrice(Integer minPrice) {
            this.minPrice = minPrice;
        }

        /**
         * @return the maxPrice
         */
        public Integer getMaxPrice() {
            return maxPrice;
        }

        /**
         * @param maxPrice the maxPrice to set
         */
        public void setMaxPrice(Integer maxPrice) {
            this.maxPrice = maxPrice;
        }

        /**
         * @return the cuisines
         */
        public String[] getCuisines() {
            return cuisines;
        }

        /**
         * @param cuisines the cuisines to set
         */
        public void setCuisines(String[] cuisines) {
            this.cuisines = cuisines;
        }

        /**
         * @return the valutazioni
         */
        public String[] getValutazioni() {
            return valutazioni;
        }

        /**
         * @param valutazioni the valutazioni to set
         */
        public void setValutazioni(String[] valutazioni) {
            this.valutazioni = valutazioni;
        }

        /**
         * @return the distance
         */
        public Double getDistance() {
            return distance;
        }

        /**
         * @param distance the distance to set
         */
        public void setDistance(Double distance) {
            this.distance = distance;
        }

        
    }
}
