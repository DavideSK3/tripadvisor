/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import db.DBManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author gabriele
 */
@Path("autocomplete")
public class AutocompleteResource {
    /*
    private DBManager manager;
    */
    @Context
    private UriInfo context;
    
    @Context
    private ServletContext servletContext; 
    
    @Context
    private HttpServletRequest request;

    /**
     * Creates a new instance of AutocompleteResource
     */
    public AutocompleteResource() {
        
    }

    
    /**
     * Retrieves representation of an instance of
     * services.AutoCompleteService
     *
     * @param term
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/restaurants/{term}")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String getRestaurants(@PathParam("term") String term) {
        
        
        DBManager manager = (DBManager)servletContext.getAttribute("dbmanager");
        
        List<String> results = null;
        try {
            results = manager.getRestaurantsNamesByTerm(term.toLowerCase(), 10);
        } catch (SQLException ex) {
            Logger.getLogger(AutocompleteResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(results != null){
            Gson gson = new Gson();
            System.out.println(results.size());
            return gson.toJson(results);
        }else{
            return null;
        }
        
    }
    
    
    /**
     * Retrieves representation of an instance of
     * services.AutoCompleteService
     *
     * @param term
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/places/{term}")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String getPlaces(@PathParam("term") String term) {
        System.out.println(term);
        DBManager manager = (DBManager)servletContext.getAttribute("dbmanager");
        
        List<String> results = null;
        try {
            results = manager.getPlaces(term.toLowerCase());
        } catch (SQLException ex) {
            Logger.getLogger(AutocompleteResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if(results != null){
            /*if(results.isEmpty()){
                results.add("");
            }*/
            Gson gson = new Gson();
            return gson.toJson(results);
        }else{
            return null;
        }
        
    }
    
    
    /**
     * Retrieves representation of an instance of services.AutocompleteResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        
        
        return null;
        
    }

    /**
     * PUT method for updating or creating an instance of AutocompleteResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
