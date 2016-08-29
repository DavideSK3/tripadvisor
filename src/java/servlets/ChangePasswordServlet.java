/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
public class ChangePasswordServlet extends HttpServlet {

    private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    /**
     * Si accede a questa servlet tramite il link presente nella mail di recovery password
     * Se il token presente nella request corrisponde a un token esistente nella corrispondente tabella, viene creata una nuova sessione 
     * con l'user appaiato a questo token in database.
     * Si rimanda quindi a change_password.jsp per effettuare il cambiamento effettivo della password, effettuato infine da ValidateChangePasswordServlet.java
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        final String token = request.getParameter("t");
        
        if(token == null){
            request.setAttribute("message", "Errore, indirizzo non esistente.");
            RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
            rd.forward(request, response);
        }else{
            
            User user = null;
            try {
                user = manager.getUserByToken(token);
            } catch (SQLException ex) {
                Logger.getLogger(ChangePasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(user == null){
                request.setAttribute("message", "Errore, token non esistente.");
                RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
                rd.forward(request, response);
            }else{
                try{
                    manager.removeToken(user.getId(), token);
                } catch (SQLException ex) {
                    Logger.getLogger(ChangePasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                HttpSession session = request.getSession();
                session.invalidate();
                
                session = request.getSession();
                session.setAttribute("c_user", user);
                RequestDispatcher rd = request.getRequestDispatcher("/change_password.jsp");
                rd.forward(request, response);
                
            }
            
        }
        
        
    }
}
