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
     * Handles the HTTP <code>GET</code> method.
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
                
                HttpSession session = request.getSession(true);
                session.setAttribute("change_password_user", user);
                RequestDispatcher rd = request.getRequestDispatcher("/change_password.jsp");
                rd.forward(request, response);
                
            }
            
        }
        
        
    }
}
