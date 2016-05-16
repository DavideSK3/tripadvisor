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
public class ValidateChangePasswordServlet extends HttpServlet {

    private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        HttpSession session = request.getSession(false);
        if(session != null){
            
            User user = (User)session.getAttribute("change_password_user");
            
            String password1 = request.getParameter("password1");
            String password2 = request.getParameter("password2");
            if(user == null){
                //TODO - cosa fare se non cè un utente!?!?!!?
                request.setAttribute("message", "Non c'è nessun utente");
                RequestDispatcher rd = request.getRequestDispatcher("/change_password.jsp");
                rd.forward(request, response);
            }else if(!password1.equals(password2)){
                request.setAttribute("message", "Le due password non coincidono!");
                RequestDispatcher rd = request.getRequestDispatcher("/change_password.jsp");
                rd.forward(request, response);
            }else{
                try {
                    manager.changePassword(user.getUsername(), password1);
                } catch (SQLException ex) {
                    Logger.getLogger(ValidateChangePasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //TODO - cosa fare se c'era già un login!?!?!?!?
                session.removeAttribute("change_password_user");
                session.invalidate();
                
                
                request.setAttribute("message", "Password cambiata, rieffettua il login");
                RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
                rd.forward(request, response);
            }
        }else{
            //TODO - cosa fare se non esiste la sessione!?!??!?!!
            
        }
        
        
        
    }
}
