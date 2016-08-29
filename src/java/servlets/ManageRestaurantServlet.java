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
import java.sql.SQLException;
import java.util.Objects;
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
 * @author Davide
 */
public class ManageRestaurantServlet extends HttpServlet {

    private DBManager manager;
    
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        preparePage(req, resp);
    }
    
    /**
     * Ricava dal database i dati del ristorante per l'id in questione, generando un errore se l'id non è valido, il ristorante non viene trovato
     * o l'utente della sessione corrente non è il proprietario del ristorante.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    protected void preparePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     
        HttpSession session = req.getSession(true);
        Integer ID=null;
        try{
            ID = Integer.parseInt(req.getParameter("restaurantID"));
        }catch(NumberFormatException e){
            req.setAttribute("message", "ID ristorante non valido");
            req.getRequestDispatcher("message.jsp").forward(req, resp);
        }
        
        Restaurant results = null;
        try {
            results = manager.getRestaurant(ID);
        }catch (SQLException ex) {
            Logger.getLogger(ManageRestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(results == null){
            req.setAttribute("message", "Ristorante non trovato");
            req.getRequestDispatcher("message.jsp").forward(req, resp);
        }else if(!Objects.equals(results.getId_owner(), ((User)session.getAttribute("user")).getId()) && ((User)session.getAttribute("user")).getCharType() != 'a'){
            req.setAttribute("message", "Questo ristorante non ti appartiene, non puoi modificarne le informazioni");
            req.getRequestDispatcher("message.jsp").forward(req, resp);
        }else{
            try{
                manager.getRestaurantTimes(results);
                manager.getRestaurantCuisines(results);
            } catch (SQLException ex) {
                Logger.getLogger(ManageRestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            req.setAttribute("results",results);
        }
        
        RequestDispatcher rd = req.getRequestDispatcher("manage_restaurant.jsp");
        
        rd.forward(req, resp);
    }
    
     /**
     * Ricava dal database i dati del ristorante per l'id in questione, generando un errore se l'id non è valido, il ristorante non viene trovato
     * o l'utente della sessione corrente non è il proprietario del ristorante.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String operation = req.getParameter("modifica");
        if(operation == null){
            preparePage(req, resp);
        }else switch (operation) {
            case "modifica_ristorante":
                ModificaRistorante(req,resp);
                break;
            case "inserisci_orario":
                InserisciOrario(req,resp);
                break;
            case "rimuovi_orario":
                RimuoviOrario(req,resp);
                break;
            case "inserisci_cucina":
                InserisciCucina(req,resp);
                break;
            case "rimuovi_cucina":
                RimuoviCucina(req,resp);
                break;
            default:
                preparePage(req, resp);
                break;
        }
        
        

    }
    
    /**
     * Gestisce le modifiche ai dati del ristorante (esclusi orari e cucina) tramite i campi in manage_restaurant.jsp
     * Riconosce e notifica vari tipi di errori
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    void ModificaRistorante(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String url = req.getParameter("url");
        String address = req.getParameter("address");
        Integer min_price = null;
        if(req.getParameter("min_price")!=null && !req.getParameter("min_price").isEmpty()){
            try{
                min_price = Integer.parseInt(req.getParameter("min_price"));
            }catch(NumberFormatException e){
                
            }
        }
        Integer max_price = null;
        if(req.getParameter("max_price")!=null && !req.getParameter("max_price").isEmpty()){
            try{
                max_price = Integer.parseInt(req.getParameter("max_price"));
            }catch(NumberFormatException e){
                
            }
        }
        Integer id = null;
        if(req.getParameter("restaurantID")!=null && !req.getParameter("restaurantID").isEmpty()){
            try{
                id = Integer.parseInt(req.getParameter("restaurantID"));
            }catch(NumberFormatException e){
                
            }
        }
        
        String message;
        try {
            manager.manageRestaurant(id, name, description, url, address, min_price, max_price);
            message = "Modifica del Ristorante completata con successo!";
        } catch (SQLException ex) {
            message = "La modifica non è andata a buon fine...";
            throw new ServletException(ex);
        }
        
        if(address != null && id != null){
            Restaurant.buildQR(id, super.getServletContext().getInitParameter("qrDir"), getServletContext().getRealPath(""), manager);
        }
        
        req.setAttribute("message", message);
        RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
        rd.forward(req, resp);
    }
    
    /**
     * Inserisce un orario (apertura e chiusura) in cui è aperto il ristorante nella tabella ORARI del database
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    void InserisciOrario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ora_apertura = req.getParameter("ora_apertura");
        String minuti_apertura = req.getParameter("minuti_apertura");
        String ora_chiusura = req.getParameter("ora_chiusura");
        String minuti_chiusura = req.getParameter("minuti_chiusura");
        Integer day = null;
        try{
            day = Integer.parseInt(req.getParameter("giorno"));
        }catch (NumberFormatException e){
            
        }
        Integer id = null;
        try{
            id = Integer.parseInt(req.getParameter("restaurantID"));
        }catch (NumberFormatException e){
            
        }
        String message;
        try {
            boolean risp = manager.inserisciOrario(id,day,ora_apertura,minuti_apertura,ora_chiusura,minuti_chiusura);
            if(!risp){
                message = "Orario già inserito";
                req.setAttribute("message", message);
                RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
                rd.forward(req, resp);
            }else{
                
                Restaurant.buildQR(id, super.getServletContext().getInitParameter("qrDir"), getServletContext().getRealPath(""), manager);
                resp.sendRedirect(resp.encodeRedirectURL("ManageRestaurant?restaurantID=" + id));
            }
        } catch (SQLException ex) {
            message = "La modifica non è andata a buon fine...";
            req.setAttribute("message", message);
            RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
            rd.forward(req, resp);
            throw new ServletException(ex);
        }
        
    }
    
    /**
     * Si occupa della rimozione di un orario in cui il ristorante è aperto dalla tabella ORARI del database
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    void RimuoviOrario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String apertura = req.getParameter("apertura");
        String chiusura = req.getParameter("chiusura");
        Integer day = null;
        try{
            day = Integer.parseInt(req.getParameter("giorno"));
        }catch (NumberFormatException e){
            
        }
        Integer id = null;
        try{
            id = Integer.parseInt(req.getParameter("restaurantID"));
        }catch (NumberFormatException e){
            
        }
        
        String message;
        try {
            manager.rimuoviOrario(id,day,apertura,chiusura);
            Restaurant.buildQR(id, super.getServletContext().getInitParameter("qrDir"), getServletContext().getRealPath(""), manager);
            resp.sendRedirect(resp.encodeRedirectURL("ManageRestaurant?restaurantID=" + id));
        } catch (SQLException ex) {
            message = "La modifica non è andata a buon fine...";
            req.setAttribute("message", message);
            RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
            rd.forward(req, resp);
            throw new ServletException(ex);
        }
        
    }
    
    /**
     * Si occupa della rimozione di una tipologia di cucina del ristorante dalla tabella CUISINES del database
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    void RimuoviCucina(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String cuisine = req.getParameter("cucina");
        
        Integer id = null;
        try{
            id = Integer.parseInt(req.getParameter("restaurantID"));
        }catch (NumberFormatException e){
            
        }
        
        String message;
        try {
            manager.rimuoviCucina(id, cuisine);
            
            resp.sendRedirect(resp.encodeRedirectURL("ManageRestaurant?restaurantID=" + id));
        } catch (SQLException ex) {
            message = "La modifica non è andata a buon fine...";
            req.setAttribute("message", message);
            RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
            rd.forward(req, resp);
            throw new ServletException(ex);
        }
        
    }
    
    /**
     * Inserisce una tipologia di cucina per il ristorante nella tabella CUISINES del database
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    void InserisciCucina(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Integer cuisine = null;
        try{
            cuisine = Integer.parseInt(req.getParameter("cucina"));
        }catch (NumberFormatException e){
            
        }
        Integer id = null;
        try{
            id = Integer.parseInt(req.getParameter("restaurantID"));
        }catch (NumberFormatException e){
            
        }
        String message;
        try {
            boolean risp = manager.inserisciCucina(id, cuisine);
            if(!risp){
                message = "Cucina già inserita";
                req.setAttribute("message", message);
                RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
                rd.forward(req, resp);
            }else{
                resp.sendRedirect(resp.encodeRedirectURL("ManageRestaurant?restaurantID=" + id));
            }
        } catch (SQLException ex) {
            message = "La modifica non è andata a buon fine...";
            req.setAttribute("message", message);
            RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
            rd.forward(req, resp);
            throw new ServletException(ex);
        }
        
    }
    
    

}
