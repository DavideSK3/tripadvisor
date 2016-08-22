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
     
        HttpSession session = req.getSession(true);
        Integer ID=null;
        ID = Integer.parseInt(req.getParameter("restaurantID"));
        Restaurant results = null;
        try {
            results = manager.getRestaurant(ID);
        }catch (SQLException ex) {
            Logger.getLogger(ManageRestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setAttribute("results",results);


        RequestDispatcher rd = req.getRequestDispatcher("manage_restaurant.jsp");
        
        rd.forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String operation = req.getParameter("modifica");
        if (operation.equals("modifica_ristorante")){
            ModificaRistorante(req,resp);
        } else if (operation.equals("inserisci_orario")){
            InserisciRistorante(req,resp);
        }
        
        

    }
    
    void ModificaRistorante(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String description = req.getParameter("description");
        String url = req.getParameter("url");
        String address = req.getParameter("address");
        Integer min_price = null;
        if(req.getParameter("min_price")!=""){
            min_price = Integer.parseInt(req.getParameter("min_price"));
        }
        Integer max_price = null;
        if(req.getParameter("max_price")!=""){
            max_price = Integer.parseInt(req.getParameter("max_price"));
        }
        Integer id = null;
        if(req.getParameter("id")!=""){
            id = Integer.parseInt(req.getParameter("id"));
        }
        String message;
        try {
            manager.manageRestaurant(id,description, url, address, min_price, max_price);
            message = "Modifica del Ristorante completata con successo!";
        } catch (SQLException ex) {
            message = "La modifica non è andata a buon fine...";
            throw new ServletException(ex);
        }
        req.setAttribute("message", message);
        RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
        rd.forward(req, resp);
    }
    
    void InserisciRistorante(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ora_apertura = req.getParameter("ora_apertura");
        String minuti_apertura = req.getParameter("minuti_apertura");
        String ora_chiusura = req.getParameter("ora_chiusura");
        String minuti_chiusura = req.getParameter("minuti_chiusura");
        Integer day = Integer.parseInt(req.getParameter("giorno"));
        Integer id = Integer.parseInt(req.getParameter("id"));
        
        String message;
        try {
            manager.inserisciOrario(id,day,ora_apertura,minuti_apertura,ora_chiusura,minuti_chiusura);
            message = "Aggiunta orario completata con successo!";
        } catch (SQLException ex) {
            message = "La modifica non è andata a buon fine...";
            throw new ServletException(ex);
        }
        req.setAttribute("message", message);
        RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
        rd.forward(req, resp);
    }

}
