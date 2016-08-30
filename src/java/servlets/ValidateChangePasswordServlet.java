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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ValidateChangePasswordServlet extends HttpServlet {

    private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    /**
     * Gestisce il cambiamento password da parte di un utente notificando errori avvenuti in caso di: 
     * password vecchia errata,
     * utente inesistente in database o sessione vuota,
     * vecchia password e conferma password non coincidenti.
     * A cambiamento avvenuto con successo, la sessione corrente viene terminata e l'utente notificato del completamento con esito positivo dell'operazione.
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
            
            String password1 = request.getParameter("password1");
            String password2 = request.getParameter("password2");
            String oldPassword = request.getParameter("old_password");
            
            User user = (User)session.getAttribute("c_user");
            
            if(user == null){
                user = (User)session.getAttribute("user");
                try {
                    if(user != null && (oldPassword == null || !manager.isPasswordCorrect(user.getId(), oldPassword))){
                        request.setAttribute("message", "Password errata!");
                        RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
                        rd.forward(request, response);
                    }
                } catch (SQLException ex) {
                    user = null;
                    Logger.getLogger(ValidateChangePasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(user == null){
                request.setAttribute("message", "Non c'è nessun utente");
                RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
                rd.forward(request, response);
            }else if(!password1.equals(password2)){
                request.setAttribute("message", "Le due password non coincidono!");
                RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
                rd.forward(request, response);
            }else{
                try {
                    manager.changePassword(user.getId(), password1);
                } catch (SQLException ex) {
                    Logger.getLogger(ValidateChangePasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                session.removeAttribute("c_user");
                session.removeAttribute("user");
                session.invalidate();
                
                request.setAttribute("message", "Password cambiata, rieffettua il login");
                RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
                rd.forward(request, response);
            }
        }else{
            request.setAttribute("message", "Non c'è nessun utente");
            RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
            rd.forward(request, response);
        }
        
        
        
    }
}
