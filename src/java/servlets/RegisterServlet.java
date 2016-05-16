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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gabriele
 */
public class RegisterServlet extends HttpServlet {

     private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application

        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");

        String password = req.getParameter("password");
        
        String user_type = req.getParameter("user_type");
        // controllo nel DB se esiste un utente con lo stesso username + password

        User user = null;

        try {
            if(user_type.equals("Restaurant")){
                user = manager.registerResturant(username, password);
            }else{
                user = manager.registerUser(username, password);
            }
            

        } catch (SQLException ex) {

            throw new ServletException(ex);

        }
        
        

        /*
        HttpSession session = req.getSession(true);

        session.setAttribute("user", user);
        
        */
        
        // mando un redirect alla servlet che carica i prodotti

        resp.sendRedirect(getServletContext().getContextPath());

    }
}
