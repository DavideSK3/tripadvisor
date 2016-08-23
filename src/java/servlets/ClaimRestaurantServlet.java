/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gabriele
 */
public class ClaimRestaurantServlet extends HttpServlet {
    
    private DBManager manager;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        Integer ID = null;
        Integer user = null;
        
        try{
            ID = Integer.parseInt(request.getParameter("restaurantID"));
        }catch (NumberFormatException e){
            
        }
        user = ((User)(session.getAttribute("user"))).getId();
        
        
        
        if(((User)(session.getAttribute("user"))).getCharType() == 'a'){
            response.sendRedirect(response.encodeRedirectURL(""));
        }else{
            String message;
            if(ID != null){
                
                Integer id_owner = null;

                try {
                    id_owner = manager.getRestaurantOwner(ID);
                } catch (SQLException ex) {
                    Logger.getLogger(ClaimRestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                if(id_owner != null){
                    if(id_owner >=0){
                        message = "Questo ristorante è già di proprietà di un utente. Non puoi reclamarlo.";
                    }else{
                        message ="Ristorante non esistente";
                    }
                }else{
                    try {
                        manager.richiestaReclamoRistorante(user,ID);
                        message = "Richiesta inoltrata agli Amministratori!";
                    } catch (SQLException ex) {
                        message = "La richiesta non è andata a buon fine!";
                        Logger.getLogger(ClaimRestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }else{
                message = "ID ristorante non valido";
            }



            request.setAttribute("message", message);
            RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
            rd.forward(request, response);
        }
    }
}
