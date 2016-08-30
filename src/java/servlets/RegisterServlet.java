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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class RegisterServlet extends HttpServlet {

     private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application

        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    /**
     * Si occupa di registrare un nuovo utente nel database con i valori contenuti nei campi di register.html
     * Gestisce errori del database o in caso di incoerenza tra i campi password e conferma password.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String password2 = req.getParameter("password_two");
        
        
        String message;
        if(password!=null && password.equals(password2)){
            User user = null;
            
            try {
                manager.registerUser(name, surname,email, password);

                message = "Registrazione effettuata con successo!\nEffettua il login per continuare.";
            } catch (SQLException ex) {
                message = "La registrazione non è andata a buon fine...";
                throw new ServletException(ex);

            }
        }else{
            message = "La registrazione non è andata a buon fine, le passsword devono coincidere!";
        }
        

        
        req.setAttribute("message", message);
        RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
        rd.forward(req, resp);

    }
}
