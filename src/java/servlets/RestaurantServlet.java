/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.Restaurant;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


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
        manageRequest(req, resp);
    }
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        manageRequest(req, resp);
    }
    
    
    /**
     * Metodo che gestisce ogni richiesta a questa servlet. Cerca il ristorante richiesto nel database e ne costruisce l'oggetto Java corrispondente.
     * Dopodichè reindirizza la richiesta alla pagina restaurant.jsp che costruisce la pagina con le informazioni contenute nell'oggetto Java passato.
     * Il metodo gestisce errori generate dalla query nel database o il caso di un ristorante non esistente
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    protected void manageRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd;
        
        String key = req.getParameter("restaurantID");
        
        Restaurant r = null;
        
        
        
        if(key != null){
            
            
            try{
                r = manager.getRestaurant(Integer.parseInt(key));
                
                if(r != null){
                    rd = req.getRequestDispatcher("/restaurant.jsp");
                }else{
                    req.setAttribute("message", "Ristorante non esistente");
                    rd = req.getRequestDispatcher("/message.jsp");
                }
                
            }catch (SQLException ex){
                req.setAttribute("message", "Ops, c'è stato un errore");
                rd = req.getRequestDispatcher("/message.jsp");
                Logger.getLogger(PasswordRecoveryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }catch(NumberFormatException ex){
                req.setAttribute("message", "Ristorante non esistente");
                rd = req.getRequestDispatcher("/message.jsp");
            }
            
        }else{
            req.setAttribute("message", "Nessun ristorante specificato");
            rd = req.getRequestDispatcher("/message.jsp");
        }
        
        if(r != null){
            try {
                manager.getRestaurantReviewValues(r);
            
                manager.getRestaurantPhotos(r);
            
                manager.getRestaurantTimes(r);
            
                manager.calcolaPosizioneInClassifica(r);
            
                manager.getCompletedReviews(r);
            }catch (SQLException ex) {
                Logger.getLogger(RestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(r.getQr_path() == null){
                r.setQr_path(Restaurant.buildQR(r.getId(), super.getServletContext().getInitParameter("qrDir"), getServletContext().getRealPath(""), manager));
            }
            req.setAttribute("restaurant", r);
            
        }
        
        rd.include(req, resp);
        
        
        
        
    }
    
    
    
}
