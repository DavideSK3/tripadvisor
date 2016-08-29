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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davide
 */
public class EditProfileServlet extends HttpServlet {

    private DBManager manager;
    
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    /**
     * Chiama il metodo editProfile che andrà ad aggiornare la tupla nel database corrispondente all'utente corrente, gestendo un eventuale errore nell'esecuzione della query.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(true);
        
        User user = ((User)session.getAttribute("user"));
        
        String email = req.getParameter("email");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        
        synchronized(user){
            try{
                manager.editProfile(user.getId(), name, surname, email);
                if(name != null) user.setName(name);
                if(email != null) user.setEmail(email);
                if(surname != null) user.setSurname(surname);
                req.setAttribute("message", "Profilo aggiornato");
            } catch (SQLException ex) {
                Logger.getLogger(EditProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
                req.setAttribute("message", "C'è stato un errore nell'aggiornamento del suo profilo.");
                
            }
        }
        

        
        req.getRequestDispatcher("message.jsp").forward(req, resp);
        
        

    }
    

}
