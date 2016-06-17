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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gabriele
 */
public class LoginServlet extends HttpServlet {

    private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application

        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        //String redirectPage = req.getParameter("redirectPage");
        
        
        
        String email = req.getParameter("email");

        String password = req.getParameter("password");
        
        
        // controllo nel DB se esiste un utente con lo stesso username + password

        User user = null;

        try {

            user = manager.authenticate(email, password);

        } catch (SQLException ex) {

            throw new ServletException(ex);

        }
        
        

        
        HttpSession session = req.getSession(true);

        session.setAttribute("user", user);
        
        
        // mando un redirect alla servlet che carica i prodotti
        /*if(redirectPage != null){
            resp.sendRedirect(resp.encodeRedirectURL(redirectPage));
        }else{
            resp.sendRedirect(resp.encodeRedirectURL(getServletContext().getContextPath()));
        }*/
        
        resp.sendRedirect(resp.encodeRedirectURL(getServletContext().getContextPath()));

    }
}
